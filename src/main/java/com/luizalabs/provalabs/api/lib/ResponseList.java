package com.luizalabs.provalabs.api.lib;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseList {
	private Meta meta;
	private ResponseEntity<?> records;
}
