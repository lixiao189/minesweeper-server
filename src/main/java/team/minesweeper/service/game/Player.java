/**
 * 定义玩家
 * @author Node Sans
 * */

package team.minesweeper.service.game;

import java.util.ArrayList;

public class Player {
	private String name; // 用户名
	private boolean status; // 游戏状态
	private Player competitor; // 对手
	private int score; // 当前用户的分数
	private double blood; // 当前用户的血量
	private ArrayList<Weapon> weapons; // 用户持有的武器

	public Player(String name) {
		this.name = name;
		this.status = false;
		this.competitor = null;
		this.score = 0;
		this.blood = 0.0;
		this.weapons = null;
	}
}
