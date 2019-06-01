package com.luizalabs.provalabs.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizalabs.provalabs.api.models.base.ResponseList;
import com.luizalabs.provalabs.storage.Repository;
import com.luizalabs.provalabs.storage.entity.Game;

@RestController
public class GameController {
	
	@Autowired
	private Repository repo;

	@GetMapping("games")
	public ResponseList getAll() {
		ResponseList response = new ResponseList();
		List<Game> allGames = repo.getAllGames();
		response.setRecords(allGames);
		return response;
	}
	@GetMapping("games/{:id}")
	public ResponseList getGame() {
		ResponseList response = new ResponseList();
		return response;
	}
}
