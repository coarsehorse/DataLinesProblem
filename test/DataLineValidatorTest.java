import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import validator.DataLineValidator;

import java.util.Arrays;
import java.util.List;

public class DataLineValidatorTest extends Assert {

    private String dataLine;
    private String queryLine;
    private String badInput1;
    private String badInput2;
    private String badInput3;
    private String badInput4;
    private String badInput5;
    private List<String> badInputs;

    @Before
    public void setupData() {
        dataLine = "C 1 10.1 P 01.12.2012 65";
        queryLine = "D 1.1 8 P 01.01.2012-01.12.2012";
        badInput1 = "";
        badInput2 = "qwe";
        badInput3 = "C 1.10.10 10.1 P 01.12.2012 65";
        badInput4 = "C 1 10.1 F 01.12.2012 65";
        badInput5 = "C 1 .1.1 N 01.12.2012 65";
        badInputs = Arrays.asList(badInput1, badInput2, badInput3, badInput4, badInput5);
    }

    @Test
    public void testDataLineValidator() {
        assertTrue("Input \"" + dataLine + "\" should pass the validator test",
                DataLineValidator.getInstance().validate(dataLine));
        assertTrue("Input \"" + queryLine + "\" should pass the validator test",
                DataLineValidator.getInstance().validate(queryLine));
        for (String badCase : badInputs)
            assertFalse("Input \"" + badCase + "\" should not pass the validator test",
                    DataLineValidator.getInstance().validate(badCase));
    }
}
