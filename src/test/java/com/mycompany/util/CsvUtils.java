package com.mycompany.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CsvUtils {


    public static List<CSVRecord> readAll(String filePath) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            return csvParser.getRecords();
        }
    }


    public static Optional<CSVRecord> findRowByProperty(String filePath, String columnName, String value) throws IOException {
        return findRowsByProperty(filePath, columnName, value).stream().findFirst();
    }


    public static List<CSVRecord> findRowsByProperty(String filePath, String columnName, String value) throws IOException {
        List<CSVRecord> records = readAll(filePath);
        return records.stream()
                .filter(record -> value.equals(record.get(columnName)))
                .collect(Collectors.toList());
    }


    public static List<CSVRecord> findRowsByPredicate(String filePath, Predicate<CSVRecord> predicate) throws IOException {
        List<CSVRecord> records = readAll(filePath);
        return records.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
//            List<CSVRecord> records = CsvUtils.readAll("src/test/resources/owners-data.csv");
//            records.forEach(record -> System.out.println(record.toMap()));

            Optional<CSVRecord> recordOpt = CsvUtils.findRowByProperty("src/test/resources/owners-data.csv", "id", "7");
            recordOpt.ifPresent(record -> System.out.println("Found record: " + record));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}