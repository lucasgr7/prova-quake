package com.luizalabs.provalabs.parser.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.parser.LogReader;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameLogReader implements LogReader{

	@Value("${game.log_file}")
	private String FILE_PATH;
	
	@Override
	public void execute() throws FileNotFoundException, IOException {
		if(FILE_PATH.isEmpty()) {
			FILE_PATH = new File("games.log").getAbsolutePath();
		}
		BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	log.info(line);
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    br.close();
		}
		
	}
}
