package com.luizalabs.provalabs.storage;

import java.util.List;

import com.luizalabs.provalabs.storage.entity.Game;

public interface Repository {

	List<Game> getAllGames();
	Game getById(int id);
	Game getByPlayerName(String player);
	void save(Game game) throws Exception;
	
}
