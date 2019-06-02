package com.luizalabs.provalabs.storage;

import java.util.List;

import com.luizalabs.provalabs.storage.entity.Game;

public interface GamesRepository {

	List<Game> findAll(Integer offset, Integer limit);
	List<Game> findAll();
	int count();
	
	Game getById(int id);
	List<Game> findByPlayerName(String player, Integer offset, Integer limit);
	void save(Game game) throws Exception;
	void clearBase();
	
}
