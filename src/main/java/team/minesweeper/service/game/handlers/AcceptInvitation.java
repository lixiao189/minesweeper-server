package team.minesweeper.service.game.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import team.minesweeper.service.game.containers.PlayerMap;
import team.minesweeper.service.game.models.Player;
import team.minesweeper.service.game.models.WaitLock;
import team.minesweeper.service.network.Handler;

public class AcceptInvitation implements Handler {
	private PlayerMap playerList;

	public AcceptInvitation(PlayerMap playerList) {
		this.playerList = playerList;
	}

	@Override
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) {
		Player player1 = this.playerList.get(sargs.get(0));
		Player player2 = this.playerList.get(sargs.get(1));
		int ifAccepted = iargs.get(0);

		if (player1.getStatus() == 2) {
			// 邀请者真的在等待邀请的状态
			if (ifAccepted == 1) { // 接受了邀请
				player1.setStatus((byte) 1);
				player2.setStatus((byte) 1);
				// 互相成为对手
				player1.setCompetitor(player2);
				player2.setCompetitor(player1);
			
				// 给两个人上同一个联机锁
				WaitLock lock = new WaitLock();
				player1.setLock(lock);
				player2.setLock(lock); 

				try {
					player1.getOutputStream().write(new byte[] { 0x03, 0x01, 0x04 });
				} catch (IOException e) {
					System.out.println("The player1 is offline now");
					try {
						outputStream.write(new byte[] { 0x03, 0x00, 0x04 });
					} catch (IOException e1) {
						System.out.println("The player2 is offline now");
					}
				}
			} else { // 没有接受邀请
				try {
					player1.getOutputStream().write(new byte[] { 0x03, 0x02, 0x04 });
				} catch (IOException e) {
					System.out.println("The player1 is offline now");
					try {
						outputStream.write(new byte[] { 0x03, 0x00, 0x04 });
					} catch (IOException e1) {
						System.out.println("The player2 is offline now");
					}
				}
			}
		}
	}
}
