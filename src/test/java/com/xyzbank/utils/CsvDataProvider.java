package com.xyzbank.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

public class CsvDataProvider {

    private static final Logger log = Logger.getLogger(CsvDataProvider.class.getName());

    /**
     * Reads a CSV from the test classpath (src/test/resources) and returns rows
     * as a 2D Object array for use with TestNG {@code @DataProvider}.
     * The first row (header) is skipped.
     *
     * @param classpathResource path relative to classpath root, e.g. "testdata/add_customer_data.csv"
     */
    public static Object[][] read(String classpathResource) {
        InputStream is = CsvDataProvider.class.getClassLoader().getResourceAsStream(classpathResource);
        if (is == null) {
            throw new RuntimeException("CSV not found on classpath: " + classpathResource);
        }
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // skip header row
            Object[][] data = new Object[rows.size()][];
            for (int i = 0; i < rows.size(); i++) {
                data[i] = rows.get(i);
            }
            log.info("Loaded " + rows.size() + " rows from classpath: " + classpathResource);
            return data;
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Failed to read CSV: " + classpathResource, e);
        }
    }
}
