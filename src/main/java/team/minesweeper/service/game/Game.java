package team.minesweeper.service.game;

import java.util.concurrent.ConcurrentHashMap;
import team.minesweeper.service.Config;
import team.minesweeper.service.network.Engine;
import team.minesweeper.service.game.models.*;
import team.minesweeper.service.game.handlers.*;

public class Game {
	private ConcurrentHashMap<String, Player> players;	
	private Engine engine; // 游戏网络引擎	
	
	public Game() {
		this.engine = new Engine();
		this.players = new ConcurrentHashMap<>();	
	}

	public void gameStart() {
		Config config = new Config();
		int port = Integer.parseInt(config.getProperty("port"));
		engine.startListen(port); // 启动后台网络引擎
	}

	public void addHandlers() {
		engine.addHandler(0x0A, new SetName(this.players));		
		engine.addHandler(0x0B, new GetPlayerList(this.players));
	}
}
