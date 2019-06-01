package com.luizalabs.provalabs.api.models.base;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.luizalabs.provalabs.storage.entity.Game;

import lombok.Data;

@Data
public class ResponseList {
	private Meta meta;
	private List<Game> records;
	
	public ResponseList() {
		this.meta = new Meta();
		try {
			this.meta.setHostname(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
