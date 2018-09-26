import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataLineFilteringTest extends Assert {

    @Test
    public void testSeviceFiltering() {
        try {
            File temp = File.createTempFile("temp-file-name", ".tmp");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp.getAbsolutePath()))) {
                bw.write("C 1.1 8.15.1 P 15.10.2012 83\n");
                bw.write("C 1.1 8.15.1 P 15.10.2012 83\n");
                bw.write("C 1 8.15.1 P 15.10.2012 83\n");
                bw.write("D 1.1 * P 01.01.2012\n");
            }

            System.out.println(temp.getAbsolutePath());

            // Test filtering

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
