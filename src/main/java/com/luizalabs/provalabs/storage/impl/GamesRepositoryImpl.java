package com.luizalabs.provalabs.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;

@Service
public class GamesRepositoryImpl implements GamesRepository {

	private static List<Game> games;
	
	public GamesRepositoryImpl() {
		// TODO Auto-generated constructor stub
		GamesRepositoryImpl.games = new ArrayList<Game> ();
	}
	
	@Override
	public List<Game> findAll() {
		List<Game> allGames = GamesRepositoryImpl.games;
		return allGames;
	}
	
	@Override
	public List<Game> findAll(int offset, int limit) {
		List<Game> allGames = GamesRepositoryImpl.games;
		return subList(offset, limit, allGames);
	}

	private List<Game> subList(int offset, int limit, List<Game> allGames) {
		int max = allGames.size();
		if(offset > max) {
			return null;
		}
		if(limit > max) {
			limit = max - offset;
		}
		if((offset + limit) > max) {
			limit = max - offset;
		}
		return allGames.subList(offset, offset+limit);
	}
	@Override
	public Game getById(int id) {
		return GamesRepositoryImpl.games.stream().filter(x -> x.getId() == id).findFirst().get();
	}

	@Override
	public void save(Game game) throws Exception {
		if(game == null || game.getId() <= 0) {
			throw new Exception("Game is invalid for persist! Please validate");
		}
		Optional<Game> gamePersisted = games.stream().filter(x -> x.getId() == game.getId()).findFirst();
		if(!gamePersisted.isPresent()) {
			games.add(game);
		}else {
			games.remove(gamePersisted.get());
			games.add(game);
		}
	}

	@Override
	public List<Game> findByPlayerName(String playerName, int offset, int limit) {				
		List<Game> filteredGames = GamesRepositoryImpl.games.stream()
				.filter(x -> x.getPlayers().stream().anyMatch(player -> player.getName().toLowerCase().contains(playerName.toLowerCase()))).collect(Collectors.toList());
		return subList(offset, limit, filteredGames);
	}

	@Override
	public int count() {
		if(GamesRepositoryImpl.games == null) {
			return 0;
		}
		return GamesRepositoryImpl.games.size();
	}


}
