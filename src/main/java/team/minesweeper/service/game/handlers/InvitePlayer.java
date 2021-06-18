/**
 * 邀请用户的 handler
 * */

package team.minesweeper.service.game.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import team.minesweeper.service.game.containers.PlayerMap;
import team.minesweeper.service.game.models.Player;
import team.minesweeper.service.network.Handler;

public class InvitePlayer implements Handler {
	private PlayerMap players;

	public InvitePlayer(PlayerMap players) {
		this.players = players;
	}

	@Override
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) {
		String playerName1 = sargs.get(0);
		String playerName2 = sargs.get(1);

		// playerName1 邀请 playerName2 玩家
		Player player1 = this.players.get(playerName1);
		Player player2 = this.players.get(playerName2);
		try {
			if (player2 == null) {
				// 玩家下线
				outputStream.write(new byte[] { 0x03, 0x02, 0x04 });
			} else if (player2.getStatus() == 1 || player2.getStatus() == 2) {
				// 玩家在游戏中或者在邀请别人中
				outputStream.write(new byte[] { 0x03, 0x01, 0x04 });
			} else {
				// 可以邀请
				player2.getOutputStream().write(0x03);
				byte[] nameData = playerName1.getBytes("UTF-8");
				player2.getOutputStream().write(nameData);
				player2.getOutputStream().write(0x04);

				// 邀请者进入等待邀请的状态
				player1.setStatus((byte) 2);
			}
		} catch (IOException e) {
			System.out.println("Player logout");
		}
	}
}
