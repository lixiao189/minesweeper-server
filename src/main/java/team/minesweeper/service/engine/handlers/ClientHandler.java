/**
 * 处理客户端请求
 * */

package team.minesweeper.service.engine.handlers;

import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket conn;

	public ClientHandler(Socket conn) {
		this.conn = conn;
	}

	@Override
	public void run() {
		while (true) {

		}
	}
}
