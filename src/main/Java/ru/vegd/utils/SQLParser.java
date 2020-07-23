package ru.vegd.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SQLParser {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLParser.class.getName());


    public static String parseSQLFileToString(String path) {
        String result = null;
        File file = new File(path);
        try {
            result = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Can't get data from sql script");
            throw new RuntimeException("Can't get data from sql script");
        }

        return result;
    }
}
