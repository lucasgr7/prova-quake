package com.luizalabs.provalabs.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.config.CustomException;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;

@Service
public class GamesRepositoryImpl implements GamesRepository {

	private static List<Game> games;
	
	public GamesRepositoryImpl() {
		this.clearBase();
	}
	
	@Override
	public List<Game> findAll() {
		return getGames();
	}
	
	@Override
	public List<Game> findAll(Integer offset, Integer limit) {
		List<Game> allGames = getGames();
		return subList(offset, limit, allGames);
	}

	private List<Game> subList(Integer offset, Integer limit, List<Game> allGames) {
		if(offset == null) {
			offset = 0;
		}
		if(limit == null) {
			limit = 50;
		}
		int max = allGames.size();
		if(offset > max) {
			return new ArrayList<>();
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
	public Optional<Game> getById(int id) {
		return GamesRepositoryImpl.games.stream().filter(x -> x.getId() == id).findFirst();
	}

	@Override
	public void save(Game game) throws CustomException {
		if(game == null || game.getId() <= 0) {
			throw new CustomException("Game is invalid for persist! Please validate");
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
	public List<Game> findByPlayerName(String playerName, Integer offset, Integer limit) {				
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

	@Override
	public void clearBase() {
		setGames(new ArrayList<>());
	}
	
	private static void setGames(List<Game> games) {
		GamesRepositoryImpl.games = games;
	}
	
	private List<Game> getGames() {
		return GamesRepositoryImpl.games;
	}


}
