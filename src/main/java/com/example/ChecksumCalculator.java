package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChecksumCalculator {

    /**
     * Assignment: pa02 - Calculating an 8, 16, or 32 bit checksum on an ASCII input file
     * Author: Your name here
     * Language: Java
     * <p>
     * To Compile: javac pa02.java
     * To Execute: java pa02 inputFile.txt 8
     * where inputFile.txt is an ASCII input file
     * and the number 8 could also be 16 or 32
     * which are the valid checksum sizes, all
     * other values are rejected with an error message
     * and program termination
     * <p>
     * Note: All input files are simple 8 bit ASCII input
     * <p>
     * Class: CIS3360 - Security in Computing - Summer 2023
     * Instructor: McAlpin
     * Due Date: per assignment
     */

    public static void main(String[] args) {
        if (args.length < 2) {
            printErrorMessage("Usage: java com.example.ChecksumCalculator <filename> <checksum size>");
            return;
        }

        String filename = args[0];
        int checksumSize = Integer.parseInt(args[1]);

        if (!isValidChecksumSize(checksumSize)) {
            printErrorMessage("Valid checksum sizes are 8, 16, or 32");
            return;
        }

        try {
            String content = readFileContent(filename);
            String paddedContent = padContent(content, checksumSize);
            long checksum = calculateChecksum(paddedContent, checksumSize);
            int characterCount = content.length();

            printContentWithLineLimit(paddedContent);
            System.out.printf("%2d bit checksum is %8x for all %4d chars\n", checksumSize, checksum, characterCount);
        } catch (IOException e) {
            printErrorMessage("Error reading the file: " + e.getMessage());
        }
    }

    private static void printErrorMessage(String message) {
        System.err.println(message);
    }

    private static boolean isValidChecksumSize(int checksumSize) {
        return checksumSize == 8 || checksumSize == 16 || checksumSize == 32;
    }

    private static String readFileContent(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String padContent(String content, int checksumSize) {
        int desiredSizeInBytes = checksumSize / 8;
        int dataSizeInBytes = content.length();
        int paddingSize = desiredSizeInBytes - (dataSizeInBytes % desiredSizeInBytes);
        if (paddingSize != desiredSizeInBytes) {
            return content + "X".repeat(Math.max(0, paddingSize));
        }
        return content;
    }

    private static long calculateChecksum(String content, int checksumSize) {
        long checksum = 0;
        long bitMask = (1L << checksumSize) - 1;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            checksum = (checksum << 8) + (int) c;
            checksum = (checksum & bitMask) + (checksum >> checksumSize);
        }

        checksum = checksum % (bitMask + 1);

        return checksum;
    }

    private static void printContentWithLineLimit(String content) {
        int start = 0;
        int end = Math.min(80, content.length());
        while (start < content.length()) {
            System.out.println(content.substring(start, end));
            start = end;
            end = Math.min(end + 80, content.length());
        }
    }
}


/*=============================================================================
| I [your name] ([your NID]) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+============================================================================*/

