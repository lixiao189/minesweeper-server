/**
 * 这个包是游戏网络引擎包
 * @author Node Sans
 * */

package team.minesweeper.service.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import team.minesweeper.service.network.handler.Handler;

public class Engine {
	private ServerSocket listener; // socket 监听器
	private ExecutorService executorService; // 后端执行器（其实就是线程池）
	private Map<Integer, Handler> handlers;

	public Engine(int port) {
		try {
			this.handlers = new HashMap<>(); // 线程安全的 map
			this.listener = new ServerSocket(port); // 监听服务
			this.executorService = Executors.newCachedThreadPool(); // 自动增长线程数的线程池
		} catch (IOException e) {
			System.out.println("Engine starting error");
			e.printStackTrace();
		}
	}

	public void startListen() {
		// 监听用户的连接
		this.executorService.execute(() -> {
			while (true) {
				try {
					Socket conn = this.listener.accept();
					this.executorService.execute(new ClientReceiver(conn, this.handlers, this.executorService));
				} catch (IOException e) {
					break;
				}
			}
		});

		// 结束事件
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				this.executorService.shutdown();
				this.listener.close();
				System.out.println("\nThe programme is stopped");
			} catch (IOException e) {
				System.out.println("Qutting error");
				e.printStackTrace();
			}
		}));
	}

	/**
	 * 向整个系统绑定操作码和操作函数
	 */
	public void addHandler(int operate, Handler handler) {
		this.handlers.put(Integer.valueOf(operate), handler);
	}
}
