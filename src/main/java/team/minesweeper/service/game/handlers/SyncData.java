package team.minesweeper.service.game.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import team.minesweeper.service.game.containers.PlayerMap;
import team.minesweeper.service.game.models.Player;
import team.minesweeper.service.network.Handler;

public class SyncData implements Handler {
	private PlayerMap players;

	public SyncData(PlayerMap players) {
		this.players = players;
	}

	@Override
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) {
		Player sourcePlayer = this.players.get(sargs.get(0));
		Player targetPlayer = sourcePlayer.getCompetitor();

		// 发送者的消息已经送达
		sourcePlayer.getLock().receiveData();
		// 等待数据接收完成
		sourcePlayer.getLock().waitForData();

		OutputStream targetOutputStream = targetPlayer.getOutputStream();
		try {
			for (int i = 0; i < 6; i++) 
				targetOutputStream.write(new byte[] { 0x03, (byte) iargs.get(i).intValue(), 0x05 });
			targetOutputStream.write(0x04);
		} catch (IOException e) {
			System.out.println("Target player is offline");
		}
	}
}
