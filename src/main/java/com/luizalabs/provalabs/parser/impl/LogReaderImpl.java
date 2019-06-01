package com.luizalabs.provalabs.parser.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.luizalabs.provalabs.parser.LogReader;

@Service
public class LogReaderImpl implements LogReader{

	BufferedReader br;
	
	@Override
	public void setFile(String path) throws FileNotFoundException {
		br = new BufferedReader(new FileReader(path));
	}

	@Override
	public String nextLine() throws IOException {
		return br.readLine();
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

}
