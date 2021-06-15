/**
 * 处理客户端请求
 * */
package team.minesweeper.service.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

class ClientReceiver implements Runnable {
	private Socket conn;
	private Map<Integer, Handler> handlers;

	public ClientReceiver(Socket conn, Map<Integer, Handler> handlers) {
		this.conn = conn;
		this.handlers = handlers;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[100];
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
			try {
				inputStream = this.conn.getInputStream();
				inputStream.read(buffer);

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
							stringArgs.add(new String(tmpArg, "UTF-8"));
							stringArg.clear();
							shouldReadStringArg = false;
						}
					} else if (buffer[i] == 4) { // 一个包的数据处理结束
						// debug
						System.out.println("The user data");
						System.out.print(operateCode + " ");
						System.out.print(integerArgs.toString() + " ");
						System.out.println(stringArgs.toString());
						
						this.handlers.get(Integer.valueOf(operateCode)).handler(this.conn, stringArgs, integerArgs);	
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
			} catch (IOException e) {
				System.out.println("Socket input stream error");
				e.printStackTrace();
				break;
			}
		}
	}
}
