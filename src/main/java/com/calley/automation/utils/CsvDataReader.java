package com.calley.automation.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generic CSV reader used to feed TestNG @DataProvider methods
 * (user registration data, agent data, login data) and to hand the
 * raw Sample_File_in_.csv path to the Power Import upload test.
 */
public class CsvDataReader {

    private CsvDataReader() {
        // utility class
    }

    /**
     * Reads a CSV file (with header row) and returns each data row as a Map<column, value>.
     */
    public static List<Map<String, String>> readCsvAsMaps(String filePath) {
        List<Map<String, String>> rows = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {
                rows.add(record.toMap());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        return rows;
    }

    /**
     * Converts a list of row-maps into a TestNG-friendly Object[][] for @DataProvider use.
     */
    public static Object[][] toDataProviderArray(List<Map<String, String>> rows) {
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }
}
