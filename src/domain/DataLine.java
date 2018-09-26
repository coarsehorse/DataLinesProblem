package domain;

import validator.DataLineValidator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Represents one data row from the input file.
 * If some field is not present(for example questionSubCategory)
 * or wildcarded(*) its value will be 0.
 * If date range is not specified both dates will be equal.
 */
public class DataLine implements Serializable {

    private String type; // "C" (waiting time line) or "D" (query)
    private Integer serviceNumber;
    private Integer serviceVariation;
    private Integer questionType;
    private Integer questionCategory;
    private Integer questionSubCategory;
    private String responseType; // type of response: P (first answer) or N (next answer)
    private LocalDate responseDate; // date of response
    private LocalDate responseDate2; // used in data lines of type "D" (query)
    private Integer time; // in minutes

    private DataLine() {
    }

    /**
     * Constructs DataLine object by parsing input string.
     *
     * @param line input string
     * @throws Exception if input string is not valid
     */
    public DataLine(String line) throws Exception {
        if (!DataLineValidator.getInstance().validate(line)) {
            StringBuilder messBuilder = new StringBuilder();
            messBuilder.append("DataLine input validation error! Input line: \"");
            messBuilder.append(line);
            messBuilder.append("\"");

            throw new Exception(messBuilder.toString());
        }

        String[] inpParts = line.split(" ");
        String typePart = inpParts[0];
        String servicePart = inpParts[1];
        String questionPart = inpParts[2];
        String responseTypePart = inpParts[3];
        String responseDatePart = inpParts[4];

        // Parse common parts

        type = typePart;

        if (servicePart.equals("*")) { // parse service part
            serviceNumber = 0;
            serviceVariation = 0;
        } else {
            String[] servParts = servicePart.split("\\.");

            switch (servParts.length) {
                case 2:
                    serviceNumber = Integer.valueOf(servParts[0]);
                    serviceVariation = Integer.valueOf(servParts[1]);
                    break;
                case 1:
                    serviceNumber = Integer.valueOf(servParts[0]);
                    serviceVariation = 0;
                    break;
            }
        }

        if (questionPart.equals("*")) { // parse question part
            questionType = 0;
            questionCategory = 0;
            questionSubCategory = 0;
        } else {
            String[] questParts = questionPart.split("\\.");

            switch (questParts.length) {
                case 3:
                    questionType = Integer.valueOf(questParts[0]);
                    questionCategory = Integer.valueOf(questParts[1]);
                    questionSubCategory = Integer.valueOf(questParts[2]);
                    break;
                case 2:
                    questionType = Integer.valueOf(questParts[0]);
                    questionCategory = Integer.valueOf(questParts[1]);
                    questionSubCategory = 0;
                    break;
                case 1:
                    questionType = Integer.valueOf(questParts[0]);
                    questionCategory = 0;
                    questionSubCategory = 0;
                    break;
            }
        }

        responseType = responseTypePart;

        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder() // parse response date
                .append(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        DateTimeFormatter formatter = builder.toFormatter();

        // Parse uncommon parts
        // Other types are excluded by the validator(only C or D)
        if ("C".equals(typePart)) { // this type has time field and no second date
            String timePart = inpParts[5];

            responseDate = LocalDate.parse(responseDatePart, formatter);
            responseDate2 = responseDate;
            time = Integer.valueOf(timePart);
        } else if ("D".equals(typePart)) { // this type can have two dates
            String[] dateParts = responseDatePart.split("-");

            if (dateParts.length == 2) {
                responseDate = LocalDate.parse(dateParts[0], formatter);
                responseDate2 = LocalDate.parse(dateParts[1], formatter);
            }
            else if (dateParts.length == 1) {
                responseDate = LocalDate.parse(dateParts[0], formatter);
                responseDate2 = responseDate;
            }

            time = 0;
        }
    }

    public String getType() {
        return type;
    }

    public Integer getServiceNumber() {
        return serviceNumber;
    }

    public Integer getServiceVariation() {
        return serviceVariation;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public Integer getQuestionCategory() {
        return questionCategory;
    }

    public Integer getQuestionSubCategory() {
        return questionSubCategory;
    }

    public String getResponseType() {
        return responseType;
    }

    public LocalDate getResponseDate() {
        return responseDate;
    }

    public LocalDate getResponseDate2() {
        return responseDate2;
    }

    public Integer getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "DataLine{" +
                "type='" + type + '\'' +
                ", serviceNumber=" + serviceNumber +
                ", serviceVariation=" + serviceVariation +
                ", questionType=" + questionType +
                ", questionCategory=" + questionCategory +
                ", questionSubCategory=" + questionSubCategory +
                ", responseType='" + responseType + '\'' +
                ", responseDate=" + responseDate +
                ", responseDate2=" + responseDate2 +
                ", time=" + time +
                '}';
    }
}
