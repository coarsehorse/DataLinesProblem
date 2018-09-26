import domain.DataLine;
import util.InputReader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemSolver {
    public static void main(String[] args) {
        try {
            List<DataLine> lines = InputReader.readInputFile("resources/input.txt");
            List<DataLine> processedTimeLines = new ArrayList<>();

            for (DataLine line : lines) {
                if (line.getType().equalsIgnoreCase("C")) {
                    processedTimeLines.add(line);
                    continue;
                }

                List<DataLine> filtered = new ArrayList<>();

                // Service filtering
                if (line.getServiceNumber() == 0) // if *
                    filtered = processedTimeLines;
                else if (line.getServiceVariation() == 0) // if only serviceNumber available in query
                    filtered = processedTimeLines.stream()
                            .filter(x -> x.getServiceNumber().equals(line.getServiceNumber()))
                            .collect(Collectors.toList());
                else // if both serviceNumber and serviceVariation available in query
                    filtered = processedTimeLines.stream()
                            .filter(x -> x.getServiceNumber().equals(line.getServiceNumber()))
                            .filter(x -> x.getServiceVariation().equals(line.getServiceVariation()))
                            .collect(Collectors.toList());

                // Question filtering
                if (line.getQuestionType() != 0) { // if not * otherwise this filter not applied
                    filtered = filtered.stream()
                            .filter(x -> x.getQuestionType().equals(line.getQuestionType()))
                            .collect(Collectors.toList());

                    if (line.getQuestionCategory() != 0) { // if questionCategory available
                        filtered = filtered.stream()
                                .filter(x -> x.getQuestionCategory().equals(line.getQuestionCategory()))
                                .collect(Collectors.toList());

                        if (line.getQuestionSubCategory() != 0) // if questionSubCategory available
                            filtered = filtered.stream()
                                    .filter(x -> x.getQuestionSubCategory().equals(line.getQuestionSubCategory()))
                                    .collect(Collectors.toList());

                    }
                }

                // Date filtering
                filtered = filtered.stream()
                        .filter(x -> { // x date rule: date >= startDate && date <= endDate
                            LocalDate startDate = line.getResponseDate();
                            LocalDate endDate = line.getResponseDate2();

                            if (startDate.isEqual(endDate)) // endDate is omitted or equals
                                return x.getResponseDate().isEqual(startDate);
                            return !(x.getResponseDate().isBefore(startDate) || x.getResponseDate().isAfter(endDate));
                        })
                        .collect(Collectors.toList());

                //filtered.forEach(System.out::println);

                if (filtered.size() > 0) {
                    int timeSum = filtered.stream()
                            .map(DataLine::getTime)
                            .reduce(0, (acc, n) -> acc + n);

                    int avgTime = timeSum / filtered.size();

                    System.out.println("Result: " + avgTime);
                } else {
                    System.out.println("-");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
