package com.luizalabs.provalabs.service;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This interface will work as a BufferedReader, set the file using the method setFile.
 * Call the method nextLine() so will return each line of the log.
 * 
 * Reason: This interface was built to help inject a mock with the project, so that i could mock a file
 * without the need of having a static log file, for every test.
 * Except the test {@link TestLogData} which i actually read the original game.log
 * @author Lucas Ribeiro
 *
 */
public interface LogReader {
	/**
	 * Set the path to the log file to be read
	 * @param path to the log file
	 * @throws FileNotFoundException
	 */
	public void setFile(String path) throws FileNotFoundException;
	
	/**
	 * Read the next line in the log
	 * @return a string containg all the data from the log file
	 * @throws IOException
	 */
	public String nextLine() throws IOException;
	
	/**
	 * Closes the stream and releases any system resources associated withit. 
	 * Once the stream has been closed, further read(), ready(),mark(), reset(), or skip() 
	 * invocations will throw an IOException.Closing a previously closed stream has no effect.
	 * {@inheritDoc close} 
	 * @throws IOException
	 */
	public void close() throws IOException;
}
