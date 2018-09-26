package util;

import domain.DataLine;
import validator.DataLineValidator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the input file with error handling.
 */
public class InputReader {

    /**
     * Reads the input file and handles bad input cases.
     * @param filePath path to the input file
     * @return list of DataLine objects
     * @throws IOException
     */
    public static List<DataLine> readInputFile(String filePath) throws IOException {
        List<DataLine> resultList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                // Skip bad lines if exist
                if (!DataLineValidator.getInstance().validate(line))
                    continue;
                try {
                    resultList.add(new DataLine(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultList;
    }
}
