package team.minesweeper.service;

import team.minesweeper.service.engine.Engine;

/**
 * 入口程序
 */

public class App {
	public static void main(String[] args) {
		Engine engine = new Engine(8080);
		engine.startListen();
	}
}
