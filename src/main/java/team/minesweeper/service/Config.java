package team.minesweeper.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
	String loadPath = "/config.properties";

	public Config() {
		try {
			this.load(getClass().getResourceAsStream(loadPath));
		} catch (FileNotFoundException e) {
			System.out.println("No config file");
		} catch (IOException e) {
			System.out.println("File IO error");
		}
	}
}
