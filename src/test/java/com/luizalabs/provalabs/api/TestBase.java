package com.luizalabs.provalabs.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestBase {

	@Test
	public void init() {
		assertTrue(!false);
	}
}
