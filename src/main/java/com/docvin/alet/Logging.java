package com.docvin.alet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Logging {


    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    private static final List<Log> logs = new ArrayList<>();
    private static int maxWidth = 0;

    public static void begin() {
        logs.clear();
        maxWidth = 0;
    }

    public static void write(Level level, String... lines) {
        logs.add(new Log(level, lines));
        maxWidth = getMaxLength(lines);
    }

    public static void build() {
        String line = "+" + fill('-', maxWidth + 2) + "+";
        LOGGER.info(line);
        for (Log log : logs)
            LOGGER.log(log.level, log.lines);
        LOGGER.info(line);
    }

    private static int getMaxLength(String... strings) {
        int length = maxWidth;
        for (String str : strings)
            length = Math.max(str.length(), length);

        return length;
    }

    private static String padString(String str, int len) {
        return str + fill(' ', len - str.length());
    }

    private static String fill(char c, int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(c);

        return sb.toString();
    }

    public static void printBox(Level level, String... strings) {
        StringBuilder padding = new StringBuilder();
        for (String str : strings)
            padding.append(String.format("| %s |", padString(str, maxWidth)));

        LOGGER.info(padding.toString());
    }

    private static class Log {
        String[] lines;
        Level level;

        Log(Level level, String... lines) {
            this.level = level;
            this.lines = lines;
        }
    }

}
