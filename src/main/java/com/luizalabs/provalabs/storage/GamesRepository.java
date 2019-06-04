package com.luizalabs.provalabs.storage;

import java.util.List;
import java.util.Optional;

import com.luizalabs.provalabs.config.CustomException;
import com.luizalabs.provalabs.storage.entity.Game;

public interface GamesRepository {

	/**
	 * Method return all the objects stored after the parser read the logs
	 * @param offset
	 * @param limit
	 * @return a list of objects {@link Game}
	 */
	List<Game> findAll(Integer offset, Integer limit);
	
	/**
	 * Method return all the objects stored after the parser read the logs
	 * @return a list of objects {@link Game}
	 */
	List<Game> findAll();
	
	/**
	 * Count the total of elements stored
	 * @return an integer
	 */
	int count();
	
	/**
	 * Filter the list of stored elements and return the one that match the id
	 * @param id sequential of the game
	 * @return an {@link Optional} containing or not the object
	 */
	Optional<Game> getById(int id);
	
	/**
	 * Filters the list of stored games, and search for a player name within, returning all games that match the name
	 * @param player name
	 * @param offset
	 * @param limit
	 * @return a list of objects that match the containing name
	 */
	List<Game> findByPlayerName(String player, Integer offset, Integer limit);
	
	/**
	 * Saves a new entity of game in the storage
	 * @param game object of {@link Game}
	 * @throws CustomException
	 */
	void save(Game game) throws CustomException;
	
	/**
	 * Clears the storage list of the app, used in unit tests mostly
	 */
	void clearBase();
	
}
