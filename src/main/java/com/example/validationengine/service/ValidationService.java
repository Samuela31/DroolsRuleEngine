package com.example.validationengine.service;


import com.example.validationengine.dto.ValidationResult;
import com.example.validationengine.entity.Fund;
import com.example.validationengine.entity.OutboxEntity;
import com.example.validationengine.repository.CanonicalTradeRepository;
import com.example.validationengine.entity.CanonicalTrade;
import com.example.validationengine.entity.Client;
import com.example.validationengine.repository.FundRepository;
import com.example.validationengine.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import com.example.validationengine.repository.ClientRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ValidationService {
    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private CanonicalTradeRepository canonicalTradeRepository;

    @Autowired
	private ObjectMapper objectMapper;

    private final KieContainer kieContainer;
    private final String navCutoffTime;
    private final Double minimumInvestmentAmount;
    private final FundRepository fundRepository;
    private final ClientRepository clientRepository;

    public ValidationService(KieContainer kieContainer,
                             @Value("${validation.nav.cutoff:15:00}") String navCutoffTime,
                             @Value("${validation.minimum.amount:1000}") Double minimumInvestmentAmount,
                             FundRepository fundRepository,
                             ClientRepository clientRepository, CanonicalTradeRepository canonicalTradeRepository) {
        this.kieContainer = kieContainer;
        this.navCutoffTime = navCutoffTime;
        this.minimumInvestmentAmount = minimumInvestmentAmount;
        this.fundRepository = fundRepository;
        this.clientRepository = clientRepository;
        this.canonicalTradeRepository = canonicalTradeRepository;
    }



    public ValidationResult validate(CanonicalTrade ct) {
        KieSession kieSession = null;
        try {
            kieSession = kieContainer.newKieSession();
            ValidationResult result = new ValidationResult();

            // insert the CanonicalTrade fact (used by DRL now)
            kieSession.insert(ct);
            kieSession.insert(result);

            kieSession.setGlobal("navCutoffTime", LocalTime.parse(navCutoffTime));
            kieSession.setGlobal("minimumInvestmentAmount", minimumInvestmentAmount);
            kieSession.setGlobal("requestId", UUID.randomUUID().toString());

            setFundAndClientGlobals(kieSession); 

            kieSession.fireAllRules();
            return result;
        } finally {
            if (kieSession != null) kieSession.dispose();
        }
    }
    
    @Transactional
    public void storeValidOrders(CanonicalTrade trade) {

        // Persist trade (id will be generated)
        CanonicalTrade saved = canonicalTradeRepository.save(trade);

        // Convert saved trade to JSON for outbox payload
        final String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(saved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert trade to JSON", e);
        }

        OutboxEntity outbox = new OutboxEntity();
        // If OutboxEntity.aggregateId is UUID:
        outbox.setAggregateId(saved.getId());
        // If it's a string: outbox.setAggregateId(saved.getId().toString());

        outbox.setPayload(payloadJson);
        outbox.setStatus("ARRIVED");
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setRetryCount(0);
        outbox.setLastAttemptAt(null);

        outboxRepository.save(outbox);
    }

    private void setFundAndClientGlobals(KieSession kieSession) {
        // load all funds
        List<Fund> funds = fundRepository.findAll();
        Map<Integer, Map<String, Object>> fundData = new HashMap<>(funds.size());
        for (Fund f : funds) {
            Map<String, Object> fm = new HashMap<>();
            fm.put("scheme_code", f.getSchemeCode());
            fm.put("status", f.getStatus());
            // store numeric values as Number so DRL can cast
            fm.put("max_limit", f.getMaxLimit() != null ? f.getMaxLimit() : null);
            fm.put("min_limit", f.getMinLimit() != null ? f.getMinLimit() : null);
            fundData.put(f.getFundId(), fm);
        }

        // load all clients
        List<Client> clients = clientRepository.findAll();
        Map<Integer, Map<String, Object>> clientData = new HashMap<>(clients.size());
        for (Client c : clients) {
            Map<String, Object> cm = new HashMap<>();
            cm.put("kyc_status", c.getKycStatus());
            cm.put("pan_number", c.getPanNumber());
            cm.put("status", c.getStatus());
            cm.put("type", c.getType());
            clientData.put(c.getClientId(), cm);
        }

        // set as globals on KIE session
        kieSession.setGlobal("fundData", fundData);
        kieSession.setGlobal("clientData", clientData);
    }
}
