package com.luizalabs.provalabs.api.game.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.luizalabs.provalabs.storage.entity.Game;

import lombok.Data;

@Data
public class ResponseBase{

	private Meta meta;
	private List<?> records;	
	private List<String> errorMessages;

	@JsonInclude(content=Include.NON_EMPTY)
	public List<String> getErrorMessages(){
		return this.errorMessages;
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setRecords(Game game) {
		this.records = Arrays.asList(game);
	}
}
