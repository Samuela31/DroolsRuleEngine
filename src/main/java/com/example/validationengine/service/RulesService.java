package com.example.validationengine.service;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.validationengine.dto.Person;



@Service
public class RulesService {

    @Autowired
    private KieSession kieSession;

    public String runRule(Person person) {        
        kieSession.insert(person);
        kieSession.fireAllRules();
        return person.getName();
    }
}
