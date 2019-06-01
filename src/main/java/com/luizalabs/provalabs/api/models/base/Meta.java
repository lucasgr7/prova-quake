package com.luizalabs.provalabs.api.models.base;

import lombok.Data;

@Data
public class Meta {
	private int totalRecords;
	private int recordsCount;
	private String hostname;
	private long offset;
	private long limit;
}
