package com.luizalabs.provalabs.storage.entity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
	private long id;
	private List<Player> players;
	private long totalKills;

	public void savePlayer(Player player) {
		Optional<Player> savedPlayer = this.players.stream().filter(x -> x.getName().equalsIgnoreCase(player.getName()))
				.findAny();
		if (!savedPlayer.isPresent()) {
			players.add(player);
		} else {
			players.remove(savedPlayer.get());
			players.add(player);
		}
		players = players.stream().sorted().collect(Collectors.toList());
		
	}
}
