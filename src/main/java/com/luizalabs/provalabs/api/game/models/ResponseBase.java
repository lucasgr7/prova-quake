package com.luizalabs.provalabs.api.game.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import com.luizalabs.provalabs.storage.entity.Game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ResponseBase{

	private Meta meta;
	private List<?> records;	
		
	public void setRecords(List<?> records, Integer offset, Integer limit, Integer totalRecords) {
		this.records = records;
		this.meta.setRecordsCount(records.size());
		this.meta.setOffset(offset);
		this.meta.setLimit(limit);
		this.meta.setTotalRecords(totalRecords);
	}
	
	public ResponseBase() {
		this.meta = new Meta();
		try {
			this.meta.setHostname(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
		}
	}
	public void setRecords(Game game) {
		this.records = Arrays.asList(game);
	}
}
