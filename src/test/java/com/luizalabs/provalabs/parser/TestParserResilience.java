package com.luizalabs.provalabs.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.luizalabs.provalabs.service.LogReader;
import com.luizalabs.provalabs.service.impl.LogReaderImpl;
import com.luizalabs.provalabs.service.impl.QuakeLogParserImpl;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;
import com.luizalabs.provalabs.storage.entity.Player;
import com.luizalabs.provalabs.storage.impl.GamesRepositoryImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {QuakeLogParserImpl.class, GamesRepositoryImpl.class, LogReaderImpl.class})
public class TestParserResilience {
	
	@Mock
	LogReader logReader;
	
	@InjectMocks
	@Autowired
	QuakeLogParserImpl quakeLogReader;
	
	@Autowired
	GamesRepository repo;
	
	String PLAYER_ONE = "killed", 
			PLAYER_TWO = "Kill:", 
			PLAYER_THREE = "22:30", 
			PLAYER_FOUR = ":";
	
	@Before
	public void init() {
		repo.clearBase();
	    MockitoAnnotations.initMocks(this);
		String[] mockedLog = new String[] {
				"  0:00 ------------------------------------------------------------\r\n", 
				"  0:00 InitGame: 12516516159", 
				" 15:00 Exit: Timelimit hit.\r\n", 
				" 20:38 ClientUserinfoChanged: 2 n\\"+PLAYER_ONE+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 20:38 ClientBegin: 2\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\"+PLAYER_TWO+"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 21:15 ClientUserinfoChanged: 2 n\\" + PLAYER_THREE +"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n",
				" 21:15 ClientUserinfoChanged: 2 n\\" + PLAYER_FOUR +"\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\r\n", 
				" 22:06 Kill: 2 3 7: "+PLAYER_ONE+" killed "+PLAYER_TWO+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 3 7: "+PLAYER_ONE+" killed "+PLAYER_TWO+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 3 7: "+PLAYER_TWO+" killed "+PLAYER_THREE+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 3 7: "+PLAYER_TWO+" killed "+PLAYER_THREE+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 3 7: "+PLAYER_THREE+" killed "+PLAYER_ONE+" by MOD_ROCKET_SPLASH\r\n",  
				" 22:06 Kill: 2 3 7: "+PLAYER_THREE+" killed "+PLAYER_ONE+" by MOD_ROCKET_SPLASH\r\n", 
				" 22:06 Kill: 2 3 7: <world> killed "+PLAYER_FOUR+" by MOD_ROCKET_SPLASH\r\n", 
				" 26  0:00 ------------------------------------------------------------\r\n"
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void should_read_log_with_players_using_special_characters() {
		
		try {
			quakeLogReader.parse("MOCK");
		} catch (Exception e) {
			fail();
		}
		assertTrue("Expected to have 1 game", repo.findAll().size() == 1);
		Game game = repo.findAll().get(0);
		assertNotNull("Game is null", game);
		assertTrue("Expected to have 4 players", game.getPlayers().size() == 4);
		assertTrue("Player isn't registered: " +PLAYER_ONE, game.getPlayers().stream().anyMatch(x -> x.getName().equalsIgnoreCase(PLAYER_ONE)));
		assertTrue("Player isn't registered: " +PLAYER_TWO, game.getPlayers().stream().anyMatch(x -> x.getName().equalsIgnoreCase(PLAYER_TWO)));
		assertTrue("Player isn't registered: " +PLAYER_THREE, game.getPlayers().stream().anyMatch(x -> x.getName().equalsIgnoreCase(PLAYER_THREE)));
		assertTrue("Player isn't registered: " +PLAYER_FOUR, game.getPlayers().stream().anyMatch(x -> x.getName().equalsIgnoreCase(PLAYER_FOUR)));
		Player player1 = game.getPlayers().get(0);
		Player player2 = game.getPlayers().get(1);
		Player player3 = game.getPlayers().get(2);
		Player player4 = game.getPlayers().get(3);
		assertTrue("Player one should have 2 kills", player1.getKills() == 2);
		assertTrue("Player two should have 2 kills", player2.getKills() == 2);
		assertTrue("Player three should have 2 kills", player3.getKills() == 2);
		assertTrue("Player four should have -1 kill", player4.getKills() == -1);
		
		assertTrue("Total kills in game should be 6", game.getTotalKills() == 7);
	}
}
