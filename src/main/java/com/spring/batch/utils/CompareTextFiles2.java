package com.spring.batch.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CompareTextFiles2 {
    public static void main(final String[] args) throws IOException {
        final Path firstFile = Paths.get("a.txt");
        final Path secondFile = Paths.get("b.txt");
        final List<String> firstFileContent = Files.readAllLines(firstFile,
            Charset.defaultCharset());
        final List<String> secondFileContent = Files.readAllLines(secondFile,
            Charset.defaultCharset());

        System.out.println(diffFiles(firstFileContent, secondFileContent));
        System.out.println(diffFiles(secondFileContent, firstFileContent));
    }

    private static List<String> diffFiles(final List<String> firstFileContent,
        final List<String> secondFileContent) {
        final List<String> diff = new ArrayList<String>();
        for (final String line : firstFileContent) {
            if (!secondFileContent.contains(line)) {
//                diff.add(line);
                diff.add((firstFileContent.indexOf(line) + 1) + " " + line);
            }
        }
        return diff;
    }
}