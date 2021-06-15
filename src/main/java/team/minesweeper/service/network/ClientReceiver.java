/**
 * 处理客户端请求
 * */
package team.minesweeper.service.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;

class ClientReceiver implements Runnable {
	private Socket conn;
	private Map<Integer, Handler> handlers;
	private ExecutorService service;

	public ClientReceiver(Socket conn, Map<Integer, Handler> handlers, ExecutorService service) {
		this.conn = conn;
		this.handlers = handlers;
		this.service = service;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[30];
		InputStream inputStream = null;
		boolean shouldReadOperationCode = false;
		boolean shouldReadIntegerArg = false;
		boolean shouldReadStringArg = false;
		int operateCode = 0;
		int integerArg = 0;
		ArrayList<Byte> stringArg = new ArrayList<>();
		ArrayList<Integer> integerArgs = new ArrayList<>();
		ArrayList<String> stringArgs = new ArrayList<>();

		while (true) {
			for (int i = 0; i < buffer.length; i++) // 清空 buffer
				buffer[i] = 0;

			try {
				inputStream = this.conn.getInputStream();
				inputStream.read(buffer);
			} catch (IOException e) {
				System.out.println("Socket IO stream error");
				e.printStackTrace();
				break;
			}

			for (int i = 0; i < buffer.length; i++) {
				boolean shouldRead = shouldReadOperationCode || shouldReadIntegerArg || shouldReadStringArg;
				if (buffer[i] == 3 && !shouldRead)
					shouldReadOperationCode = true;
				else if (buffer[i] == 1 && !shouldRead)
					shouldReadIntegerArg = true;
				else if (buffer[i] == 2 && !shouldRead)
					shouldReadStringArg = true;
				else if (buffer[i] == 5) {
					// 某块数据的读取结束
					if (shouldReadOperationCode)
						shouldReadOperationCode = false;
					else if (shouldReadIntegerArg) {
						integerArgs.add(Integer.valueOf(integerArg));
						integerArg = 0;
						shouldReadIntegerArg = false;
					} else if (shouldReadStringArg) {
						byte[] tmpArg = new byte[stringArg.size()];
						for (int j = 0; j < tmpArg.length; j++)
							tmpArg[j] = stringArg.get(j);
						try {
							stringArgs.add(new String(tmpArg, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							System.out.println("unsupprted encoding exception in ClientReceiver.java: 75");
						}
						stringArg.clear();
						shouldReadStringArg = false;
					}
				} else if (buffer[i] == 4) { // 一个包的数据处理结束
					// 调试信息
					// TODO debug
					System.out.println("The user data");
					System.out.print(operateCode + " ");
					System.out.print(integerArgs.toString() + " ");
					System.out.println(stringArgs.toString());

					Handler handler = this.handlers.get(Integer.valueOf(operateCode));
					ArrayList<String> tmpStringArgs = new ArrayList<>();
					ArrayList<Integer> tmpIntegerArgs = new ArrayList<>();
					tmpStringArgs.addAll(stringArgs);
					tmpIntegerArgs.addAll(integerArgs);
					this.service.execute(() -> {
						try {
							handler.execute(this.conn.getOutputStream(), tmpStringArgs, tmpIntegerArgs);
						} catch (IOException e) {
							System.out.println("Connection output stream error");
						}
					});
					integerArgs.clear();
					stringArgs.clear();
					operateCode = 0;
				} else {
					if (shouldReadOperationCode) {
						operateCode = (operateCode << 8) + (int) (buffer[i] & 0xff);
					} else if (shouldReadIntegerArg) {
						integerArg = (integerArg << 8) + (int) (buffer[i] & 0xff);
					} else if (shouldReadStringArg) {
						stringArg.add(buffer[i]);
					}
				}
			}
		}
	}
}
