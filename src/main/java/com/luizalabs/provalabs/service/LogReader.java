package com.luizalabs.provalabs.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface LogReader {
	public void setFile(String path) throws FileNotFoundException;
	public String nextLine() throws IOException;
	public void close() throws IOException;
}
