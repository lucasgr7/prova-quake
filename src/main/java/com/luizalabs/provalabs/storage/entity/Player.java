package com.luizalabs.provalabs.storage.entity;

import lombok.Data;

@Data
public class Player {
	private String name;
	private int totalKills;
	
	public Player(String name) {
		this.name = name;
		this.totalKills = 0;
	}
	
	public void removeKill() {
		this.totalKills -= 1;
	}
	
	public void addKill() {
		this.totalKills += 1;
	}
}
