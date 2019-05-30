package com.luizalabs.provalabs.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizalabs.provalabs.api.models.Pong;

@RestController("/health")
public class HealthController {
	
	@GetMapping("ping")
	public Pong getPing() {
		return new Pong();
	}
}
