package com.luizalabs.provalabs.parser.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.parser.LogReader;
import com.luizalabs.provalabs.storage.Repository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.entity.Player;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameLogReader implements LogReader {

	@Autowired
	private Repository repo;

	private static Game GameInAnalisis;

	private final String USER_WORLD_GAME = "<world>", LOG_START_DEAD_PLAYER = "killed", LOG_END_DEAD_PLAYER = "by";
	private final String LOG_START_GAME = "InitGame:", LOG_PLAYER_INFO_CHANGE = "ClientUserinfoChanged", LOG_PLAYER_KILL = "Kill:", LOG_FINISH_GAME = "-----------";

	@Override
	public void parse(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
			String line = br.readLine();
			while (line != null) {
				log.info(line);
				analyseLogLine(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	private void analyseLogLine(String line) throws Exception {
		if (line.contains(LOG_START_GAME)) {
			createNewGame();
		} else if(line.contains(LOG_PLAYER_INFO_CHANGE)) {
			addPlayer(line);
		} else if (line.contains(LOG_PLAYER_KILL)) {
			addKill(line);
		} else if (line.contains(LOG_FINISH_GAME)) {
			finishGame(line);
		}
	}

	private void finishGame(String line) throws Exception {
		if (GameLogReader.GameInAnalisis != null) {
			repo.save(GameLogReader.GameInAnalisis);
			GameLogReader.GameInAnalisis = null;
		}
	}

	private void addPlayer(String line) {
		Game game = GameLogReader.GameInAnalisis;

		String[] bruteLogWords = line.split(" n\\\\");
		String playerName = bruteLogWords[1].split("\\\\t")[0];

		Optional<Player> player = findPlayer(playerName);
		if(!player.isPresent()) {
			Player newPlayer = new Player(playerName);
			game.savePlayer(newPlayer);
		}
	}
	
	private Optional<Player> findPlayer(final String name) {
		Optional<Player> player = GameLogReader.GameInAnalisis.getPlayers().stream().filter(x -> x.getName().equalsIgnoreCase(name))
				.findAny();
		return player;
	}

	private void addKill(String line) {
		Game game = GameLogReader.GameInAnalisis;
		game.setTotalKills(GameLogReader.GameInAnalisis.getTotalKills() + 1);

		Player killer = getPlayer(line, game, EnumPlayer.killer);

		if (USER_WORLD_GAME.equalsIgnoreCase(killer.getName())) {
			Player dead = getPlayer(line, game, EnumPlayer.dead);
			dead.removeKill();
			game.savePlayer(dead);
		}
		killer.addKill();
		game.savePlayer(killer);
	}

	private Player getPlayer(String line, Game game, EnumPlayer selectedPlayer) {

		String[] bruteLogWords = line.split(":");
		String[] killedLog = bruteLogWords[3].split(LOG_START_DEAD_PLAYER);
		String killerPlayerName = killedLog[0].trim();
		String deadPlayerName = killedLog[1].split(LOG_END_DEAD_PLAYER)[0].trim();

		if (selectedPlayer == EnumPlayer.killer) {
			Optional<Player> killer = findPlayer(killerPlayerName);
			return killer.orElse(new Player(killerPlayerName));
		} else {
			Optional<Player> dead = findPlayer(deadPlayerName);
			return dead.orElse(new Player(deadPlayerName));
		}
	}

	private void createNewGame() {
		GameLogReader.GameInAnalisis = Game.builder().id(repo.getAllGames().size() + 1).players(new ArrayList<Player>())
				.build();
	}

	public enum EnumPlayer {
		dead, killer
	}
}
