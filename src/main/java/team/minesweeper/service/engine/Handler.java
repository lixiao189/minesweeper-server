/**
 * 对应请求绑定的 handler
 * */

package team.minesweeper.service.engine;

import java.net.Socket;
import java.util.ArrayList;

interface Handler {
	public void handler(Socket conn, ArrayList<String> sargs, ArrayList<Integer> iargs);
}
