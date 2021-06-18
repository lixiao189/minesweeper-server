package team.minesweeper.service.game;

import team.minesweeper.service.Config;
import team.minesweeper.service.game.containers.PlayerMap;
import team.minesweeper.service.game.handlers.AcceptInvitation;
import team.minesweeper.service.game.handlers.GetPlayerList;
import team.minesweeper.service.game.handlers.InvitePlayer;
import team.minesweeper.service.game.handlers.SetPlayer;
import team.minesweeper.service.game.handlers.SyncData;
import team.minesweeper.service.network.Engine;

public class Game {
	private PlayerMap players;
	private Engine engine; // 游戏网络引擎

	public Game() {
		this.engine = new Engine();
		this.players = new PlayerMap();
	}

	public void gameStart() {
		Config config = new Config();
		int port = Integer.parseInt(config.getProperty("port"));
		engine.startListen(port); // 启动后台网络引擎
	}

	public void addHandlers() {
		engine.addHandler(0x0A, new SetPlayer(this.players));
		engine.addHandler(0x0B, new GetPlayerList(this.players));
		engine.addHandler(0x0C, new InvitePlayer(this.players));
		engine.addHandler(0x0D, new AcceptInvitation(this.players));	
		engine.addHandler(0x0E, new SyncData(this.players));
	}
}
