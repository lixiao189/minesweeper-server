package team.minesweeper.service.game.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import team.minesweeper.service.game.models.Player;
import team.minesweeper.service.network.Handler;

public class GetPlayerList implements Handler {
	private ConcurrentHashMap<String, Player> players;

	public GetPlayerList(ConcurrentHashMap<String, Player> players) {
		this.players = players;
	}

	@Override
	public void execute(OutputStream outputStream, ArrayList<String> sargs, ArrayList<Integer> iargs) {
		byte status;
		ArrayList<Byte> result = new ArrayList<>();
		result.add((byte) 0x06);
		for (String name : this.players.keySet()) {
			status = this.players.get(name).getStatus();
			result.add((byte) 0x03);
			for (int j = 0; j < name.length(); j++)
				result.add((byte) name.charAt(j));
			result.add((byte) 0x05);
			result.add((byte) 0x03);
			if (status != 0) // 如果这个人非空闲
				result.add((byte) 0x01);
			else // 如果这个人空闲
				result.add((byte) 0x00);
			result.add((byte) 0x05);
		}
		result.add((byte) 0x04);

		// 返回响应
		byte[] resultBuffer = new byte[result.size()];
		for (int i = 0; i < result.size(); i++)
			resultBuffer[i] = result.get(i);
		try {
			outputStream.write(resultBuffer);
		} catch (IOException e) {
			System.out.println("The output has a error");
		}
	}
}
