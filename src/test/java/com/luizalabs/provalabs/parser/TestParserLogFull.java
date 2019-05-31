package com.luizalabs.provalabs.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.luizalabs.provalabs.parser.impl.GameLogReader;
import com.luizalabs.provalabs.storage.Repository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.impl.RepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GameLogReader.class, RepositoryImpl.class})
@Slf4j
public class TestParserLogFull {

	@Autowired
	LogReader logReader;
	
	@Autowired
	Repository repo;
	
	private final String LOG_START_GAME = "InitGame:", LOG_KILL = "Kill:";

	@Test
	public void test_full_log_read() {
		String FILE_PATH = new File("games.log").getAbsolutePath();
		
		Integer countGames = 0;
		Integer[] gamesCountKills = new Integer[50];
		
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
			
			//Execute the logreader main method
			logReader.parse(FILE_PATH);
		} catch (Exception e) {
			fail();
		}
		List<Game> savedGames = repo.getAllGames();
		assertTrue("The games in repository is null",  savedGames!= null);
		assertTrue("The games list is empty", !savedGames.isEmpty());
		assertTrue("There isn't the same count of games as expected", savedGames.size() == countGames);

		for(int x = 0; x < countGames; x++) {
			int arrayPosition = x+1;
			Game game = savedGames.get(x);
			int testCount = gamesCountKills[arrayPosition] == null ? 0 : gamesCountKills[arrayPosition];
			assertTrue(String.format("Game %s kills don't match  (game data) %s != (test count) %s", x, game.getTotalKills(), testCount), 
					game.getTotalKills() == testCount);
		}
	}
}
