import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.ProblemSolverV1;
import util.ProblemSolverV2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProblemSolverTest extends Assert {

    private File temp;
    private String successResult;

    @Before
    public void prepareTempFile() {
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp.getAbsolutePath()))) {
                bw.write("7\n");
                bw.write("C 1.1 8.15.1 P 15.10.2012 83\n");
                bw.write("C 1 10.1 P 01.12.2012 65\n");
                bw.write("C 1.1 5.5.1 P 01.11.2012 117\n");
                bw.write("D 1.1 8 P 01.01.2012-01.12.2012\n");
                bw.write("C 3 10.2 N 02.10.2012 100\n");
                bw.write("D 1 * P 08.10.2012-20.11.2012\n");
                bw.write("D 3 10 P 01.12.2012\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        successResult = "83\n100\n-\n";
    }

    @Test
    public void testProblemSolverV1() {
        assertEquals("ProblemSolverV1 did not solve the problem",
                successResult,
                ProblemSolverV1.solve(temp.getAbsolutePath()));
    }

    @Test
    public void testProblemSolverV2() {
        assertEquals("ProblemSolverV2 did not solve the problem",
                successResult,
                ProblemSolverV2.solve(temp.getAbsolutePath()));
    }
}
