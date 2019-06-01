package com.luizalabs.provalabs.api.game.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class Meta {
	@JsonInclude(content=Include.NON_NULL)
	private int totalRecords;
	@JsonInclude(content=Include.NON_NULL)
	private int recordsCount;
	@JsonInclude(content=Include.NON_NULL)
	private String hostname;
	@JsonInclude(content=Include.NON_NULL)
	private long offset;
	@JsonInclude(content=Include.NON_NULL)
	private long limit;
}
