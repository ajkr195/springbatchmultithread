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

//public void javaStream() {
//List<String> list = new ArrayList<>();
//try (Stream<String> stream = Files.lines(Paths.get(FILENAME))) {
//	// 1. filter line 3 //2. convert all content to upper case //3. convert it into
//	// a List
//	list = stream.filter(line -> !line.startsWith("line3")).map(String::toUpperCase)
//			.collect(Collectors.toList());
//
//} catch (IOException e) {
//	e.printStackTrace();
//}
//list.forEach(System.out::println);
//}
