package com.luizalabs.provalabs.e2e;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.luizalabs.provalabs.api.game.GameController;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class TestApiGet {

}
