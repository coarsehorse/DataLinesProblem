package validator;

import java.util.regex.Pattern;

/**
 * Validator for the DataLine class constructor.
 * Provides the validation of the input string.
 * Singleton implementation prevents multiple patterns compilation.
 */
public class DataLineValidator {
    private static DataLineValidator _instance = null;
    private Pattern timeLinePattern = Pattern.compile(
            "^(C|D) (\\d(\\d)?(\\.\\d)?|\\*) (\\d(\\d)?(\\.\\d(\\d)?(\\.\\d)?)?|\\*) (P|N) \\d\\d\\.\\d\\d\\.\\d{4} \\d+$");
    private Pattern queryPattern = Pattern.compile(
            "^(C|D) (\\d(\\d)?(\\.\\d)?|\\*) (\\d(\\d)?(\\.\\d(\\d)?(\\.\\d)?)?|\\*) (P|N) \\d\\d\\.\\d\\d\\.\\d{4}(-\\d\\d\\.\\d\\d\\.\\d{4})?$");

    private DataLineValidator() {
    }

    public static DataLineValidator getInstance() {
        if (_instance == null)
            _instance = new DataLineValidator();
        return _instance;
    }

    /**
     * Validates the input for DataLine class constructor.
     *
     * @param input string with data
     * @return <code>true</code> if input is valid, <code>false</code> otherwise
     */
    public Boolean validate(String input) {
        return timeLinePattern.matcher(input).matches() || queryPattern.matcher(input).matches();
    }
}
