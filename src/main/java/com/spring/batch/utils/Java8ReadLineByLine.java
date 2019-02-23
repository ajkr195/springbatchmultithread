package com.spring.batch.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Java8ReadLineByLine {
	public static void main(String[] args) throws IOException {
		Files.lines(new File("100sales.csv").toPath()).map(s -> s.trim())
				// .filter(s -> s.startsWith("xyz"))
				.forEach(System.out::println);
	}
}
