package com.luizalabs.provalabs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.config.CustomException;
import com.luizalabs.provalabs.service.LogReader;
import com.luizalabs.provalabs.service.QuakeLogParser;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.entity.Player;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuakeLogParserImpl implements QuakeLogParser {

	@Autowired
	private GamesRepository repo;

	@Autowired
	private LogReader fileReader;

	private static Game gameState;

	private static final String USER_WORLD = "<world>";
	private static final String REGEX_PATTERN_END_GAME = "^.*\\d*:\\d*\\s(-*)$";
	private static final String REGEX_PATTERN_START_GAME = "^\\d*:\\d*\\sInitGame:.*$";
	private static final String REGEX_PATTERN_PLAYER_ENTERED = "^\\d*:\\d*\\sClientUserinfoChanged:\\s\\d.*";
	private static final String REGEX_PATTERN_KILL = "^\\d*:\\d*\\sKill:.*";

	@Override
	public void parse(String filePath) throws IOException, CustomException {
		fileReader.setFile(filePath);
		try {
			String line = fileReader.nextLine();
			while (line != null) {
				analyseLogLine(line.trim());
				line = fileReader.nextLine();
			}
		} catch (CustomException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			fileReader.close();
		}
	}

	private void analyseLogLine(String line) throws CustomException {
		if (line.matches(REGEX_PATTERN_START_GAME)) {
			createNewGame();
		} else if (line.matches(REGEX_PATTERN_PLAYER_ENTERED)) {
			addPlayer(line);
		} else if (line.matches(REGEX_PATTERN_KILL)) {
			addKill(line);
		} else if (line.matches(REGEX_PATTERN_END_GAME)) {
			finishGame(line);
		}
	}

	private void finishGame(String line) throws CustomException {
		if (QuakeLogParserImpl.gameState != null) {
			repo.save(QuakeLogParserImpl.gameState);
			QuakeLogParserImpl.gameState = null;
			log.info("FINISH GAME: " + line);
		}
	}

	private void addPlayer(String line) {
		Game game = QuakeLogParserImpl.gameState;

		String[] bruteLogWords = line.split(" n\\\\");
		String playerName = bruteLogWords[1].split("\\\\t")[0];

		Optional<Player> player = findPlayer(playerName);
		if (!player.isPresent()) {
			Player newPlayer = new Player(playerName);
			game.savePlayer(newPlayer);
		}
	}

	private void addKill(String line) throws CustomException {
		Game game = QuakeLogParserImpl.gameState;
		game.setTotalKills(QuakeLogParserImpl.gameState.getTotalKills() + 1);

		Player killer = getPlayerFromLog(line, game, EnumPlayer.killer);

		if (USER_WORLD.equalsIgnoreCase(killer.getName())) {
			Player dead = getPlayerFromLog(line, game, EnumPlayer.dead);
			dead.removeKill();
			game.savePlayer(dead);
		} else {
			killer.addKill();
			game.savePlayer(killer);
		}
	}

	private Player getPlayerFromLog(String line, Game game, EnumPlayer selectedPlayer) throws CustomException {
		String[] bruteLogWords = line.split("^\\d*:\\d*\\sKill:\\s\\d*\\s\\d*\\s\\d*:\\s");
		String[] killedLog = bruteLogWords[1].split("\\skilled\\s(?!=killed\\sby)");
		String killerPlayerName = killedLog[0].trim();
		String deadPlayerName = killedLog[1].split("\\sby\\sMOD_.*$")[0].trim();

		String playerNameFiltered = killerPlayerName;

		if (selectedPlayer == EnumPlayer.dead) {
			playerNameFiltered = deadPlayerName;
		}
		Optional<Player> player = findPlayer(playerNameFiltered);
		if (!player.isPresent()) {
			if (USER_WORLD.equalsIgnoreCase(playerNameFiltered)) {
				return new Player(USER_WORLD);
			}
			throw new CustomException("Player not recognized was present in kill log: " + playerNameFiltered);
		}
		return player.get();
	}

	private Optional<Player> findPlayer(final String name) {
		Optional<Player> player = QuakeLogParserImpl.gameState.getPlayers().stream()
				.filter(x -> x.getName().equalsIgnoreCase(name)).findAny();
		return player;
	}

	private void createNewGame() {
		QuakeLogParserImpl.gameState = Game.builder().id(repo.count() + (long) 1).players(new ArrayList<Player>())
				.build();
	}

	public enum EnumPlayer {
		dead, killer
	}
}
