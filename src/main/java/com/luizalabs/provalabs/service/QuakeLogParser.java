package com.luizalabs.provalabs.service;

import java.io.IOException;

import com.luizalabs.provalabs.config.CustomException;

/**
 * Start a new game in the parser, this parser works as Railway.
 * It starts a new cycle of game.
 * Register all the players retrieved
 * Detect the end of the game, and stores the object in a static list.
 * Will do that until reach the end of the log file.
 * @author Lucas Ribeiro
 *
 */
public interface QuakeLogParser {

	/**
	 * Parse all the contents of the log file.
	 * Will read the log until the end, then storage all the data on static lists of the applications.
	 * Living the entire cycle of the app.
	 * @param filePath to the log file
	 * @throws IOException
	 * @throws CustomException
	 */
	public void parse(String filePath) throws IOException, CustomException;
}
