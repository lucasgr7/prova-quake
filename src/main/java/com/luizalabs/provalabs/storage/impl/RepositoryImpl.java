package com.luizalabs.provalabs.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.storage.Repository;
import com.luizalabs.provalabs.storage.entity.Game;

@Service
public class RepositoryImpl implements Repository {

	private static List<Game> games;
	
	public RepositoryImpl() {
		// TODO Auto-generated constructor stub
		RepositoryImpl.games = new ArrayList<Game> ();
	}
	
	@Override
	public List<Game> getAllGames() {
		return RepositoryImpl.games;
	}

	@Override
	public Game getById(int id) {
		return RepositoryImpl.games.get(id);
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
	public Game getByPlayerName(String player) {
		// TODO Auto-generated method stub
		return null;
	}

}
