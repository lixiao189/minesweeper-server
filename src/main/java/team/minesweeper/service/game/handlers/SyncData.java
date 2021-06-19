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
		OutputStream targetOutputStream = targetPlayer.getOutputStream();
		// 发送者的消息已经送达
		sourcePlayer.getLock().receiveData();
		// 等待数据接收完成
		sourcePlayer.getLock().waitForData();

		byte[] result = new byte[20];
		for (int i = 0; i < 6; i++) {
			result[3 * i + 0] = 0x03;
			result[3 * i + 1] = (byte) iargs.get(i).intValue();
			result[3 * i + 2] = 0x05;
		}
		result[18] = 0x04;
		try {
			sourcePlayer.getLock().setDataNum(0); // 清空发包数量记录
			targetOutputStream.write(result);
		} catch (IOException e) {
			System.out.println("Target player is offline");
		}
	}
}
