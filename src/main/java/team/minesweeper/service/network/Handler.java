/**
 * 对应请求绑定的 handler
 * */

package team.minesweeper.service.network;

import java.net.Socket;
import java.util.ArrayList;

public interface Handler {
	public void handler(Socket conn, ArrayList<String> sargs, ArrayList<Integer> iargs);
}
