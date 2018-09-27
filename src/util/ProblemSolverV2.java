package util;

import domain.DataLine;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemSolverV2 {

    public static String solve(String inputFilePath) {
        String resultText = "-";

        try {
            List<DataLine> inputLines = InputReader.readInputFile(inputFilePath);
            HashMap<DataLine, List<DataLine>> queryLinesMap = inputLines.stream()
                    .collect(HashMap::new, (m, e) -> {
                        if (!e.getType().equalsIgnoreCase("d")) {
                            List<DataLine> prev = m.getOrDefault(null, new ArrayList<>());

                            if (prev.isEmpty()) {
                                prev.add(e);
                                m.put(null, prev);
                            } else
                                prev.add(e); // put is redundant, prev is a link
                        } else {
                            m.put(e, new ArrayList<>(m.getOrDefault(null, new ArrayList<>())));
                        }
                    }, (m1, m2) -> {
                        m1.putAll(m2);
                    });

            queryLinesMap.remove(null); // remove service pair

            Map<DataLine, String> queryResultMap = queryLinesMap.entrySet().stream()
                    .map(entry -> { // process each group of query => lines
                        DataLine query = entry.getKey();
                        List<DataLine> dLines = entry.getValue();
                        List<DataLine> filteredLines = dLines.stream()
                                .filter(dLine -> { // service filter
                                    if (query.getServiceNumber() == 0)
                                        return true;
                                    else if (query.getServiceVariation() == 0)
                                        return query.getServiceNumber().equals(dLine.getServiceNumber());
                                    else
                                        return query.getServiceVariation().equals(dLine.getServiceVariation());
                                })
                                .filter(dLine -> { // question filter
                                    if (query.getQuestionType() == 0) // 0 == *
                                        return true;
                                    else if (query.getQuestionType().equals(dLine.getQuestionType())) {
                                        if (query.getQuestionCategory() == 0) // 0 == *
                                            return true;
                                        else if (query.getQuestionCategory().equals(dLine.getQuestionCategory())) {
                                            if (query.getQuestionSubCategory() == 0) // 0 == *
                                                return true;
                                            else if (query.getQuestionSubCategory().equals(
                                                    dLine.getQuestionSubCategory())) {
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    } else
                                        return false;
                                })
                                .filter(dLine -> { // date filter, dLine.date >= startDate && dLine.date <= endDate
                                    LocalDate startDate = query.getResponseDate();
                                    LocalDate endDate = query.getResponseDate2();

                                    if (startDate.isEqual(endDate)) // endDate is omitted or equals
                                        return dLine.getResponseDate().isEqual(startDate);

                                    return !(dLine.getResponseDate().isBefore(startDate) ||
                                            dLine.getResponseDate().isAfter(endDate));
                                })
                                .collect(Collectors.toList());

                        int timeSum = filteredLines.stream()
                                .map(DataLine::getTime)
                                .reduce(0, (acc, n) -> acc + n);
                        int filteredQuantity = filteredLines.size();
                        String result = timeSum == 0 ? "-" : String.valueOf(timeSum / filteredQuantity);

                        return new Pair<>(query, result);
                    })
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            // Restore queries order before result constructing
            resultText = inputLines.stream()
                    .filter(x -> x.getType().equalsIgnoreCase("d"))
                    //.forEach(x -> System.out.println(queryResultMap.get(x)));
                    .map(queryResultMap::get)
                    .collect(Collectors.joining("\n", "", "\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultText;
    }
}