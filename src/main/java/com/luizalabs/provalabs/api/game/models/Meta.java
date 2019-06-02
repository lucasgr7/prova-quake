package com.luizalabs.provalabs.api.game.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class Meta {
	@JsonInclude(Include.NON_EMPTY)
	private int totalRecords;
	@JsonInclude(Include.NON_EMPTY)
	private int recordsCount;
	@JsonInclude(Include.NON_EMPTY)
	private String hostname;
	@JsonInclude(Include.NON_EMPTY)
	private long offset;
	@JsonInclude(Include.NON_EMPTY)
	private long limit;
}
