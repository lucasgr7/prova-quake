package com.luizalabs.provalabs.service;

import java.io.IOException;

import com.luizalabs.provalabs.config.CustomException;

public interface QuakeLogParser {

	public void parse(String filePath) throws IOException, CustomException;
}
