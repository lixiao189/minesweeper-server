package team.minesweeper.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import team.minesweeper.service.network.Engine;

/**
 * 入口程序
 */

public class App {
	public static void main(String[] args) {
		Engine engine = new Engine(8090);
		// 添加一个 handler
		engine.addHandler(9, (OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) -> {
			int a = iargs.get(0);
			int b = iargs.get(1);

			try {
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				writer.write(Integer.toString(a + b) + "\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		engine.startListen();
	}
}
