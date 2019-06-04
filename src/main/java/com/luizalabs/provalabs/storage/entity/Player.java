package com.luizalabs.provalabs.storage.entity;

import lombok.Data;

@Data
public class Player implements Comparable<Player>{
	private String name;
	private int kills;
	
	public Player(String name) {
		this.name = name;
		this.kills = 0;
	}
	
	public void removeKill() {
		this.kills -= 1;
	}
	
	public void addKill() {
		this.kills += 1;
	}
	
	@Override
	public int compareTo(Player o) {
		return this.getName().compareTo(o.getName());
	}
}
