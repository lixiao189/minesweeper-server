package team.minesweeper.service.game.models;

public class WaitLock {
	private int DataNum; // 上传了的数据的份数
	private final int MaxNum; // 一个房间的最大人数

	public WaitLock() {
		this.DataNum = 0;
		this.MaxNum = 2;
	}

	public synchronized int getDataNum() {
		return DataNum;
	}

	public synchronized void setDataNum(int dataNum) {
		DataNum = dataNum;
	}

	public synchronized void receiveData() {
		this.DataNum++;
	}

	public synchronized void waitForData() {
		if (this.DataNum < this.MaxNum) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Locking error");	
			}	
		}
		this.notifyAll();
	}
}
