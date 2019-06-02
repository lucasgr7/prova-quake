package com.luizalabs.provalabs.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.luizalabs.provalabs.service.QuakeLogParser;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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
		} catch(Exception ex) {
			log.error(ex.getMessage());
		}
	}

}
