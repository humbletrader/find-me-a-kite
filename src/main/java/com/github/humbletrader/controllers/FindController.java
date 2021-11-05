package com.github.humbletrader.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/find-me-a-kite")
public class FindController {

    private static final Logger logger = LoggerFactory.getLogger(FindController.class);

    public FindController() {

    }

    //check below for a simple usage (shortcut GetMapping)
    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Product>> retrieveUsers() {
        List<Product> result = new ArrayList<>();
        result.add(new Product("Switchblade", "Cabrinha"));
        result.add(new Product("Lithium", "Airush"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}