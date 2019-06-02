package com.luizalabs.provalabs.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.luizalabs.provalabs.service.LogReader;
import com.luizalabs.provalabs.service.impl.LogReaderImpl;
import com.luizalabs.provalabs.service.impl.QuakeLogParserImpl;
import com.luizalabs.provalabs.storage.impl.GamesRepositoryImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {QuakeLogParserImpl.class, GamesRepositoryImpl.class, LogReaderImpl.class})
public class TestParserFailReadFile {

	@Mock
	LogReader logReader;
	
	@InjectMocks
	@Autowired
	QuakeLogParserImpl quakeLogReader;
	

	@Before
	public void init() {
	    MockitoAnnotations.initMocks(this);		
		try {
			Mockito.when(logReader.nextLine()).then(new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					throw new FileNotFoundException("File don't exists");
				}
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = FileNotFoundException.class)
	public void expect_throwable_exception_when_read() throws FileNotFoundException, IOException, Exception {
		quakeLogReader.parse("MOCK");
	}
}
