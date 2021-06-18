/**
 * 定义玩家
 * @author Node Sans
 * */

package team.minesweeper.service.game.models;

import java.io.OutputStream;

public class Player {
	private String name; // 用户名
	private OutputStream outputStream; // 该用户 socket 输出字节流
	private volatile byte status; // 游戏状态 status = 1 在游戏中/ 0 空闲 / 2 邀请别人中
	private volatile Player competitor; // 对手
	private WaitLock lock;	

	public WaitLock getLock() {
		return lock;
	}

	public void setLock(WaitLock lock) {
		this.lock = lock;
	}

	public Player getCompetitor() {
		return competitor;
	} 

	public void setCompetitor(Player competitor) {
		this.competitor = competitor;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public Player(String name, OutputStream outputStream) {
		this.name = name;
		this.status = 0;
		this.competitor = null;
		this.outputStream = outputStream;
	}
}
