package com.mozi.moziserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

    @Operation(summary = "API 확인 용도")
    @GetMapping("/health-check")
    public ResponseEntity<Object> getHealthCheck(
    ) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
