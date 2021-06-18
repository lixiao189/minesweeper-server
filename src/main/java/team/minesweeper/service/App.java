package team.minesweeper.service;

import team.minesweeper.service.game.Game;

/**
 * 入口程序
 */

public class App {
	public static void main(String[] args) {
		Game game = new Game();
		game.addHandlers();	
		game.gameStart();

		// package test
		if (args.length > 0) {
			if (args[0].equals("--test")) {
				System.out.println("package test ok!");
				System.exit(0);
			}
		}
	}
}
