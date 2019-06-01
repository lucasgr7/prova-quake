package com.luizalabs.provalabs.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.luizalabs.provalabs.parser.QuakeLogParser;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private String filepath;
	
	@Autowired
	private QuakeLogParser logReader;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if(filepath == null || filepath.isEmpty()) {
			filepath = new File("games.log").getAbsolutePath();
		}
        try {
			logReader.parse(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
