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

	private Game gameState;

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
			finishGame();
		} catch (CustomException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			fileReader.close();
		}
	}

	/**
	 * Using regex pattern recognition, it will detect if is a line of interest.
	 * It will categirize as four possible outcomes for the line:
	 * Start the game - It will create a new instance of object to store data from the next lines
	 * Player Entered the game - Will add the players to a list of players in match
	 * Kill - Retrieve the data of registered kill in the match
	 * End game - Stores the object and wait to open a new instance.
	 * @param line
	 * @throws CustomException
	 */
	private void analyseLogLine(String line) throws CustomException {
		if (line.matches(REGEX_PATTERN_START_GAME)) {
			createNewGame();
		} else if (line.matches(REGEX_PATTERN_PLAYER_ENTERED)) {
			addPlayer(line);
		} else if (line.matches(REGEX_PATTERN_KILL)) {
			addKill(line);
		} else if (line.matches(REGEX_PATTERN_END_GAME)) {
			finishGame();
		}
	}

	private void finishGame() throws CustomException {
		if (this.getGameState() != null) {
			repo.save(this.getGameState());
			this.setGameState(null);
		}
	}

	/**
	 * Split the string to retrieve the players name from the log file
	 * @param line
	 */
	private void addPlayer(String line) {
		Game game = this.getGameState();

		String[] bruteLogWords = line.split(" n\\\\");
		String playerName = bruteLogWords[1].split("\\\\t")[0];

		Optional<Player> player = findPlayer(playerName);
		if (!player.isPresent()) {
			Player newPlayer = new Player(playerName);
			game.savePlayer(newPlayer);
		}
	}

	/**
	 * Read the data from the line, extract the dead player and the killer
	 * Increment +1 to the killer's kill count.
	 * In case the player died from the <world> (enviroment of the game) the dead's player kill count will be decremented
	 * @param line
	 * @throws CustomException
	 */
	private void addKill(String line) throws CustomException {
		Game game = this.getGameState();
		game.setTotalKills(game.getTotalKills() + (long) 1);

		Player killer = getPlayerFromLog(line, EnumPlayer.KILLER);

		if (USER_WORLD.equalsIgnoreCase(killer.getName())) {
			Player dead = getPlayerFromLog(line, EnumPlayer.DEAD);
			dead.removeKill();
			game.savePlayer(dead);
		} else {
			killer.addKill();
			game.savePlayer(killer);
		}
	}

	/**
	 * Retrieve both players from a "Kill:" log line.
	 * @param line
	 * @param selectedPlayer an enum to select each shall be returned (dead player or assassin player)
	 * @return an object of the player
	 * @throws CustomException
	 */
	private Player getPlayerFromLog(String line, EnumPlayer selectedPlayer) throws CustomException {
		String[] bruteLogWords = line.split("^\\d*:\\d*\\sKill:\\s\\d*\\s\\d*\\s\\d*:\\s");
		String[] killedLog = bruteLogWords[1].split("\\skilled\\s(?!=killed\\sby)");
		String killerPlayerName = killedLog[0].trim();
		String deadPlayerName = killedLog[1].split("\\sby\\sMOD_.*$")[0].trim();

		String playerNameFiltered = killerPlayerName;

		if (selectedPlayer == EnumPlayer.DEAD) {
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
		return this.getGameState().getPlayers().stream()
				.filter(x -> x.getName().equalsIgnoreCase(name)).findAny();
	}

	/**
	 * Create a new global object to stores the game to be worked untill reach the end in the log
	 */
	private void createNewGame() {
		this.setGameState(Game.builder().id(repo.count() + (long) 1).players(new ArrayList<Player>())
				.build());
	}

	private void setGameState(Game game) {
		this.gameState = game;
	}
	private Game getGameState() {
		return this.gameState;
	}

	public enum EnumPlayer {
		DEAD, KILLER
	}
}
