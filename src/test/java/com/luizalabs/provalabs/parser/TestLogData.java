package com.luizalabs.provalabs.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.luizalabs.provalabs.service.QuakeLogParser;
import com.luizalabs.provalabs.service.impl.LogReaderImpl;
import com.luizalabs.provalabs.service.impl.QuakeLogParserImpl;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.impl.GamesRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {QuakeLogParserImpl.class, GamesRepositoryImpl.class, LogReaderImpl.class})
@WebAppConfiguration
@Slf4j
public class TestLogData {

	@Autowired
	QuakeLogParser GameLogReader;
	
	@Autowired
	GamesRepository repo;

	Integer countGames = 0;
	Integer[] gamesCountKills = new Integer[50];
	String FILE_PATH = new File("games.log").getAbsolutePath();
	
	private final String LOG_START_GAME = "InitGame:", LOG_KILL = "Kill:";
	
	@Before
	public void init() {
		repo.clearBase();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(FILE_PATH));
			String line = br.readLine();
			while (line != null) {
				if(line.contains(LOG_START_GAME)) {
					countGames++;
				}else if(line.contains(LOG_KILL)) {
					if(gamesCountKills[countGames] == null) {
						gamesCountKills[countGames] = 0;
					}
					gamesCountKills[countGames]++;
				}
				line = br.readLine();
				log.info(line);
			}
			
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void should_read_original_log_and_store_the_data() {
		//Execute the logreader main method
		try {
			GameLogReader.parse(FILE_PATH);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		List<Game> savedGames = repo.findAll();
		assertTrue("The games in repository is null",  savedGames!= null);
		assertTrue("The games list is empty", !savedGames.isEmpty());
		assertTrue(String.format("There isn't the same count of games (%s) as expected (%s)",savedGames.size(), countGames), savedGames.size() == countGames);

		for(int x = 0; x < countGames; x++) {
			int arrayPosition = x+1;
			Game game = savedGames.get(x);
			int testCount = gamesCountKills[arrayPosition] == null ? 0 : gamesCountKills[arrayPosition];
			assertTrue(String.format("Game %s kills don't match  (game data) %s != (test count) %s", x, game.getTotalKills(), testCount), 
					game.getTotalKills() == testCount);
		}
	}
}
