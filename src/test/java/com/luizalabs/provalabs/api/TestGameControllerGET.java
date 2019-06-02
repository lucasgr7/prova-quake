package com.luizalabs.provalabs.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import com.luizalabs.provalabs.api.game.GameController;
import com.luizalabs.provalabs.service.LogReader;
import com.luizalabs.provalabs.service.impl.LogReaderImpl;
import com.luizalabs.provalabs.service.impl.QuakeLogParserImpl;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.entity.Player;
import com.luizalabs.provalabs.storage.impl.GamesRepositoryImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {QuakeLogParserImpl.class, GamesRepositoryImpl.class, LogReaderImpl.class, GameController.class})
@WebAppConfiguration
public class TestGameControllerGET {
	@Mock
	LogReader logReader;
	
	@InjectMocks
	@Autowired
	QuakeLogParserImpl quakeLogReader;
	
	@Autowired
	GamesRepository repo;
	
	@Autowired
	GameController controller;
	
	private final int DEFAULT_OFFSET = 0, DEFAULT_LIMIT = 50;
	
	String PLAYER_ONE = "Abel", PLAYER_TWO = "Bain", PLAYER_THREE = "Cris evans", PLAYER_FOUR = "------------------";

	@Before
	public void init() {
		repo.clearBase();
	    MockitoAnnotations.initMocks(this);
		String[] mockedLog = new String[] {
				"  0:00 ------------------------------------------------------------\r\n", 
				"  0:00 InitGame: ", 
				" 15:00 Exit: Timelimit hit.\r\n", 
				" 20:38 ClientUserinfoChanged: 2 n\\"+PLAYER_ONE+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 20:38 ClientBegin: 2\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\"+PLAYER_TWO+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\" + PLAYER_THREE +"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n",
				" 21:15 ClientUserinfoChanged: 2 n\\" + PLAYER_FOUR +"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 20:38 ClientBegin: 4\r\n", 
				" RANDOM STRING\r\n",  
				" 22:06 Kill: 1 3 7: "+PLAYER_ONE+" killed "+PLAYER_TWO+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 2 7: "+PLAYER_TWO+" killed "+PLAYER_THREE+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 3 2 7: "+PLAYER_THREE+" killed "+PLAYER_ONE+" by MOD_ROCKET_SPLASH\r\n", 
				" 22:06 Kill: 4 4 7: "+PLAYER_FOUR+" killed "+PLAYER_FOUR+" by MOD_ROCKET_SPLASH\r\n", 
				" 22:06 Kill: 5 4 7: <world> killed "+PLAYER_FOUR+" by MOD_ROCKET_SPLASH\r\n", 
				"  0:00 ------------------------------------------------------------\r\n",
				"  0:00 ------------------------------------------------------------\r\n", 
				"  0:00 InitGame: ", 
				" 15:00 Exit: Timelimit hit.\r\n", 
				" 20:38 ClientUserinfoChanged: 2 n\\"+PLAYER_ONE+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 20:38 ClientBegin: 1\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\"+PLAYER_TWO+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n",  
				" 20:38 ClientBegin: 2\r\n", 
				" 22:06 Kill: 2 3 7: "+PLAYER_ONE+" killed "+PLAYER_TWO+" by MOD_ROCKET_SPLASH\r\n",
				"  0:00 ------------------------------------------------------------\r\n",
				"  0:00 ------------------------------------------------------------\r\n", 
				"  0:00 InitGame: ", 
				" 15:00 Exit: Timelimit hit.\r\n", 
				" 20:38 ClientUserinfoChanged: 2 n\\"+PLAYER_ONE+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 20:38 ClientBegin: 1\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\"+PLAYER_TWO+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n",  
				" 20:38 ClientBegin: 2\r\n", 
				" 22:06 Kill: 2 3 7: "+PLAYER_TWO+" killed "+PLAYER_ONE+" by MOD_ROCKET_SPLASH\r\n",
				"  0:00 ------------------------------------------------------------\r\n"
		};
		AtomicInteger count = new AtomicInteger(0);		
		try {
			Mockito.when(logReader.nextLine()).then(new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					try {
						String returnLine = mockedLog[count.getAndIncrement()];
						return returnLine;	
					}
					catch(Exception ex) {
						return null;
					}
				}
				
			});
			quakeLogReader.parse("MOCK");
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void should_return_all_three_games() {
		var response = controller.getAll(0, 50, null);
		assertNotNull("Response was null", response);
		assertTrue("Didn't parsed the three games from the logs", response.getRecords().size() == 3);
		assertNotNull("First game was null", response.getRecords().get(0));
		
		
		var game1 = (Game) response.getRecords().get(0);
		assertTrue(isValidGame1(game1));
		
		var game2 = (Game) response.getRecords().get(1);
		assertTrue(isValidGame2(game2));
		
		var game3 = (Game) response.getRecords().get(2);
		assertTrue(isValidGame3(game3));
	}
	
	@Test
	public void should_return_game_1_from_id() {
		var response = controller.getGame(1);
		assertNotNull("Response was null", response);
		assertTrue("Didn't return the game expected", response.getRecords().size() == 1);		
		
		var game1 = (Game) response.getRecords().get(0);
		assertTrue(isValidGame1(game1));
	}
	@Test
	public void should_return_game_2_from_id() {
		var response = controller.getGame(2);
		assertNotNull("Response was null", response);
		assertTrue("Didn't return the game expected", response.getRecords().size() == 1);		
		
		var game = (Game) response.getRecords().get(0);
		assertTrue(isValidGame2(game));
	}
	@Test
	public void should_return_game_3_from_id() {
		var response = controller.getGame(3);
		assertNotNull("Response was null", response);
		assertTrue("Didn't return the game expected", response.getRecords().size() == 1);		
		
		var game = (Game) response.getRecords().get(0);
		assertTrue(isValidGame3(game));
	}


	@Test(expected = ResponseStatusException.class)
	public void search_id_4_should_return_404() {
		var response = controller.getGame(4);
		assertNotNull(response);
	}
	
	@Test
	public void should_return_pagenated_responses() {
		
		int offset = 0, limit = 1;
		var response = controller.getAll(offset, limit, null);
		assertNotNull("Response was null" ,response);
		assertTrue("Didn't return a single game expected", response.getRecords().size() == 1);
		assertTrue("Meta offset didn't result as expected", response.getMeta().getOffset() == offset);
		assertTrue("Meta limit didn't result as expected", response.getMeta().getLimit() == limit);

		offset = 1;
		response = controller.getAll(offset, limit, null);
		assertNotNull("Response was null" ,response);
		assertTrue("Didn't return a single game expected", response.getRecords().size() == 1);
		assertTrue("Meta offset didn't result as expected", response.getMeta().getOffset() == offset);
		assertTrue("Meta limit didn't result as expected", response.getMeta().getLimit() == limit);

		offset = 2;
		response = controller.getAll(offset, limit, null);
		assertNotNull("Response was null" ,response);
		assertTrue("Didn't return a single game expected", response.getRecords().size() == 1);
		assertTrue("Meta offset didn't result as expected", response.getMeta().getOffset() == offset);
		assertTrue("Meta limit didn't result as expected", response.getMeta().getLimit() == limit);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void should_return_404_when_paginated_four_when_only_have_three_elements() {
		int offset = 3, limit = 1;
		var response = controller.getAll(offset, limit, null);
		assertNull("Response was null" ,response);
	}
	
	@Test
	public void should_return_games_containg_user_4() {
		var response = controller.getAll(DEFAULT_OFFSET, DEFAULT_LIMIT, PLAYER_FOUR.toUpperCase());
		assertNotNull("Response was null" ,response);
		assertTrue("Expected to return one game containing user 4", response.getRecords().size() == 1);
		var game = (Game) response.getRecords().get(0);
		assertTrue("The game contains the user 4 in it's players", game.getPlayers().stream().anyMatch(x -> x.getName().equalsIgnoreCase(PLAYER_FOUR)));
	}

	public boolean isValidGame1(Game game) {
		var allPlayers = List.of(PLAYER_ONE, PLAYER_TWO, PLAYER_THREE, PLAYER_FOUR);
		
		List<String> playersNames = game.getPlayers().stream().map(x -> x.getName()).collect(Collectors.toList());
		assertTrue("Game 1 should have returned 4 players", playersNames.size() == 4);
		assertTrue("Game 1 sDidn't return all the players", playersNames.containsAll(allPlayers));
		assertTrue(String.format("Returned %s expected %s", game.getTotalKills(), 5), game.getTotalKills() == 5);
		assertTrue("Game 1 sPlayer 1 should have one kill registered", game.getPlayers().get(0).getTotalKills() == 1);
		assertTrue("Game 1 sPlayer 2 should have one kill registered", game.getPlayers().get(1).getTotalKills() == 1);
		assertTrue("Game 1 sPlayer 3 should have one kill registered", game.getPlayers().get(2).getTotalKills() == 1);
		assertTrue("Game 1 sPlayer 4 should have zero kills registered", game.getPlayers().get(3).getTotalKills() == 0);
		return true;
	}
	public boolean isValidGame2(Game game) {
		var allPlayers = List.of(PLAYER_ONE, PLAYER_TWO);
		
		List<String> playersNames = game.getPlayers().stream().map(x -> x.getName()).collect(Collectors.toList());
		assertTrue("Game 2 Should have returned 2 players", playersNames.size() == 2);
		assertTrue("Game 2 Didn't return all expected players", playersNames.containsAll(allPlayers));
		assertTrue(String.format("Game 2 returned %s expected %s", game.getTotalKills(), 1), game.getTotalKills() == 1);

		Player abel = game.getPlayers().stream().filter(x -> x.getName().equalsIgnoreCase(PLAYER_ONE)).findAny().get();
		Player bain = game.getPlayers().stream().filter(x -> x.getName().equalsIgnoreCase(PLAYER_TWO)).findAny().get();
		
		assertTrue("Game 2 Player 1 should have one kill registered", abel.getTotalKills() == 1);
		assertTrue("Game 2 Player 2 should have none kill registered", bain.getTotalKills() == 0);
		return true;
	}

	public boolean isValidGame3(Game game) {
		var allPlayers = List.of(PLAYER_ONE, PLAYER_TWO);
		
		List<String> playersNames = game.getPlayers().stream().map(x -> x.getName()).collect(Collectors.toList());
		
		assertTrue("Game 3 should have returned 2 players", playersNames.size() == 2);
		assertTrue("Game 3 Didn't return all expected players", playersNames.containsAll(allPlayers));
		assertTrue(String.format("Game 3 returned %s expected %s", game.getTotalKills(), 1), game.getTotalKills() == 1);

		Player abel = game.getPlayers().stream().filter(x -> x.getName().equalsIgnoreCase(PLAYER_ONE)).findAny().get();
		Player bain = game.getPlayers().stream().filter(x -> x.getName().equalsIgnoreCase(PLAYER_TWO)).findAny().get();
		
		assertTrue("Game 3 Player 1 should have none kill registered", abel.getTotalKills() == 0);
		assertTrue("Game 3 Player 2 should have one kill registered", bain.getTotalKills() == 1);
		return true;
	}
}
