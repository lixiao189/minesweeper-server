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

public class Engine {
	private ServerSocket listener; // socket 监听器
	private ExecutorService executorService; // 后端执行器（其实就是线程池）
	private Map<Integer, Handler> handlers;

	/**
	 * @param port 网络引擎的端口
	 */
	public Engine() {
		this.handlers = new HashMap<>(); // 线程安全的 map
		this.executorService = Executors.newCachedThreadPool(); // 自动增长线程数的线程池
	}

	public void startListen(int port) {
		// 监听用户的连接
		try {
			this.listener = new ServerSocket(port); // 监听服务
		} catch (IOException e1) {
			System.out.println("Socket starting error");	
		}
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
