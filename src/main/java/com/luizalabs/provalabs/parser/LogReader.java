package com.luizalabs.provalabs.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface LogReader {

	public void parse(String filePath) throws FileNotFoundException, IOException;
}
