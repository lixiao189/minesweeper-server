package team.minesweeper.service.game.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import team.minesweeper.service.game.models.Player;
import team.minesweeper.service.network.Handler;

public class SetPlayer implements Handler {
	private ConcurrentHashMap<String, Player> players;

	public SetPlayer(ConcurrentHashMap<String, Player> players) {
		this.players = players;
	}

	@Override
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) {
		String name = sargs.get(0); // 玩家的用户名
		byte result = 0x00;
		if (this.players.containsKey(name)) {
			// 注册失败
			result = 0x02;
		} else {
			// 注册成功
			result = 0x01;
			this.players.put(name, new Player(name, outputStream));
		}

		try {
			outputStream.write(new byte[] { 0x03, result, 0x04 });
		} catch (IOException e) {
			System.out.println("Data send failed");
		}
	}
}
