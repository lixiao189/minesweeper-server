package team.minesweeper.service.game.containers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

import team.minesweeper.service.game.models.Player;

public class PlayerMap extends ConcurrentHashMap<String, Player> {

	/**
	 * @return 如果玩家在线返回玩家，如果没有在线返回 null
	 */
	@Override
	public Player get(Object key) {
		Player result = super.get(key);
		if (result == null) // 一开始就不存在这个用户
			return null;
		OutputStream outputStream = result.getOutputStream();
		try {
			outputStream.write(0x00);
		} catch (IOException e) {
			this.remove(key); // 用户下线了就清除
			return null;
		}
		return result;
	}

	/**
	 * 这个函数不会自动清理未下线的用户
	 */
	public boolean isOnline(Object key) {
		Player player = this.get(key);
		OutputStream outputStream = player.getOutputStream();
		try {
			outputStream.write(0x00);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public KeySetView<String, Player> keySetView() {
		KeySetView<String, Player> result = super.keySet();
		for (String name : super.keySet()) {
			if (!this.isOnline(name)) {
				this.remove(name); // 同时清理未下线用户
				result.remove(name);
			}
		}
		return result;
	}

	/**
	 * @return 如果玩家在线返回 true, 如果不在线返回 false;
	 */
	public boolean containsKey(Object key) {
		if (super.containsKey(key)) {
			Player result = super.get(key);
			OutputStream outputStream = result.getOutputStream();
			try {
				outputStream.write(0x00);
			} catch (IOException e) {
				this.remove(key);
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
