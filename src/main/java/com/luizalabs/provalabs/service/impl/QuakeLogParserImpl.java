package com.luizalabs.provalabs.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	private static Game GameInAnalisis;

	private final String USER_WORLD = "<world>";
	private final String REGEX_PATTERN_END_GAME = "^.*\\d*:\\d*\\s(-*)$";
	private final String REGEX_PATTERN_START_GAME = "^\\d*:\\d*\\sInitGame:.*$";
	private final String REGEX_PATTERN_PLAYER_ENTERED = "^\\d*:\\d*\\sClientUserinfoChanged:\\s\\d.*";
	private final String REGEX_PATTERN_KILL = "^\\d*:\\d*\\sKill:.*";

	@Override
	public void parse(String filePath) throws FileNotFoundException, IOException, Exception {
		fileReader.setFile(filePath);
		try {
			String line = fileReader.nextLine();
			while (line != null) {
				log.info(line);
				analyseLogLine(line.trim());
				line = fileReader.nextLine();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			fileReader.close();
		}
	}

	private void analyseLogLine(String line) throws Exception {
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

	private void finishGame(String line) throws Exception {
		if (QuakeLogParserImpl.GameInAnalisis != null) {
			repo.save(QuakeLogParserImpl.GameInAnalisis);
			QuakeLogParserImpl.GameInAnalisis = null;
			log.info("FINISH GAME: " + line);
		}
	}

	private void addPlayer(String line) {
		Game game = QuakeLogParserImpl.GameInAnalisis;

		String[] bruteLogWords = line.split(" n\\\\");
		String playerName = bruteLogWords[1].split("\\\\t")[0];

		Optional<Player> player = findPlayer(playerName);
		if (!player.isPresent()) {
			Player newPlayer = new Player(playerName);
			game.savePlayer(newPlayer);
		}
	}

	private void addKill(String line) throws Exception {
		Game game = QuakeLogParserImpl.GameInAnalisis;
		game.setTotalKills(QuakeLogParserImpl.GameInAnalisis.getTotalKills() + 1);

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

	private Player getPlayerFromLog(String line, Game game, EnumPlayer selectedPlayer) throws Exception {
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
			throw new Exception("Player not recognized was present in kill log: " + playerNameFiltered);
		}
		return player.get();
	}

	private Optional<Player> findPlayer(final String name) {
		Optional<Player> player = QuakeLogParserImpl.GameInAnalisis.getPlayers().stream()
				.filter(x -> x.getName().equalsIgnoreCase(name)).findAny();
		return player;
	}

	private void createNewGame() {
		QuakeLogParserImpl.GameInAnalisis = Game.builder().id(repo.count() + 1).players(new ArrayList<Player>())
				.build();
	}

	public enum EnumPlayer {
		dead, killer
	}
}
