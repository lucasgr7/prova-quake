package com.luizalabs.provalabs.api.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizalabs.provalabs.api.health.models.ResponsePong;

@RestController("/health")
public class HealthController {
	
	@GetMapping("ping")
	public ResponsePong getPing() {
		return new ResponsePong();
	}
}
