/**
 * 对应请求绑定的 handler
 * */

package team.minesweeper.service.network;

import java.io.OutputStream;
import java.util.ArrayList;

public interface Handler {
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs);
}
