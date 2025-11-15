package repository.util;

import com.opencsv.CSVReader;
import repository.exceptions.DataProcessingException;

public class CsvUtils {

    private CsvUtils() {
    }

    public static void skipHeader(CSVReader reader) {
        try {
            reader.readNext();
        } catch (Exception e) {
            throw new DataProcessingException("Error skipping CSV header", e);
        }
    }
}