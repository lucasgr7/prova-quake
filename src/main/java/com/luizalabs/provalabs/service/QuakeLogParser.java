package com.luizalabs.provalabs.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface QuakeLogParser {

	public void parse(String filePath) throws FileNotFoundException, IOException, Exception;
}
