package com.example.validationengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.validationengine.dto.Person;
import com.example.validationengine.service.RulesService;



@RestController
@RequestMapping("/rules")
public class DroolsHelloWorld {

	@Autowired
    private RulesService rulesService;
	    
    @GetMapping("/run")
    public String runRules(Person person) {
    	rulesService.runRule(person);
        return "Rules executed!";
    }
}
