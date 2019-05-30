package com.luizalabs.provalabs.api.lib;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Meta {
	private int totalRecords;
	private int recordsCount;
	private String hostname;
	private long offset;
	private long limit;
}
