package com.luizalabs.provalabs.api.game;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.luizalabs.provalabs.api.game.models.ResponseBase;
import com.luizalabs.provalabs.storage.GamesRepository;
import com.luizalabs.provalabs.storage.entity.Game;

import io.swagger.annotations.ApiOperation;

@RestController
public class GameController {

    protected static final String DEFAULT_OFFSET = "0";
    protected static final String DEFAULT_LIMIT = "50";
    
	@Autowired
	private GamesRepository repo;

	@GetMapping("/v1/games")
	@ApiOperation(value = "Return the list of all the games data")
    @ResponseStatus(HttpStatus.OK)
	public ResponseBase getAll(
			@RequestParam(name="offset",defaultValue=DEFAULT_OFFSET,required = false) Integer offset, 
			@RequestParam(name="limit",defaultValue=DEFAULT_LIMIT,required = false) Integer limit, 
			@RequestParam(name="playerName",required = false) String name) {
		ResponseBase response = new ResponseBase();
		List<Game> allGames;
		if(name != null) {
			allGames = repo.findByPlayerName(name, offset, limit);	
		}else {
			allGames = repo.findAll(offset, limit);
		}
		if(allGames.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found");
		}
		response.setRecords(allGames, offset, limit, repo.count());
		return response;
	}
	@GetMapping("v1/games/{id}")
	@ApiOperation(value = "Return a single game by the id")
	public ResponseBase getGame(@PathVariable(name="id",required = true) int id) {
		ResponseBase response = new ResponseBase();
		Optional<Game> game = repo.getById(id);
		if(!game.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found");
		}
		response.setRecords(game.get());
		return response;
	}

}
