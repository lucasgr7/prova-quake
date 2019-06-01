package com.luizalabs.provalabs.api.game;

import java.util.List;
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

@RestController
public class GameController {

    protected static final String DEFAULT_OFFSET = "0";
    protected static final String DEFAULT_LIMIT = "50";
    
	@Autowired
	private GamesRepository repo;

	@GetMapping("games")
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
		if(allGames == null || allGames.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found");
		}
		response.setRecords(allGames, offset, limit, repo.count());
		return response;
	}
	@GetMapping("games/{id}")
	public ResponseBase getGame(@PathVariable(name="id",required = true) int id) {
		ResponseBase response = new ResponseBase();
		Game game = repo.getById(id);
		if(game == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found");
		}
		response.setRecords(game);
		return response;
	}

}
