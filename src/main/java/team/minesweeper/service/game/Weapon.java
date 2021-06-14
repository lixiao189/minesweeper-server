/**
 * 武器类
 * @author Node Sans
 * */

package team.minesweeper.service.game;

public class Weapon {
	private String name;
	private int coldDown; // 武器的冷却时间
	private double hurtValue; // 武器的伤害
	private int[] color; // 武器的激光束颜色

	public Weapon(String name) {
		this.name = name;
		this.coldDown = 0;
		this.hurtValue = 0;
		this.color = new int[] { 0, 0, 0 };
	}
}
