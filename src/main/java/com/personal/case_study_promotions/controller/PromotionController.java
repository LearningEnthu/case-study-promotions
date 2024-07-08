package com.personal.case_study_promotions.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.case_study_promotions.model.Item;
import com.personal.case_study_promotions.service.DataStoreService;


@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final DataStoreService dataStore;

    private ObjectMapper objectMapper = new ObjectMapper();
    public PromotionController(DataStoreService dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPromotion(@PathVariable String id) throws JsonProcessingException {
        Date startTime = new Date();
        Item result = dataStore.getData(id);
        if (result != null) {
            String resultJson = objectMapper.writeValueAsString(result);
            Date endTime = new Date();
            System.out.println("Time taken to fetch the record: " + (endTime.getTime() - startTime.getTime()) + "ms");
            return ResponseEntity.ok(resultJson);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record found for id: " + id);
        }
    }
}
