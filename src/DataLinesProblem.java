import domain.DataLine;
import javafx.util.Pair;
import validator.DataLineValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Application entry point.
 */
public class DataLinesProblem {

    /**
     * Solution to the problem.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        List<DataLine> buffer = new ArrayList<>();

        try (Stream<String> fileStream = Files.lines(Paths.get("resources/input2.txt"))) {
            fileStream
                    .filter(DataLineValidator.getInstance()::validate)
                    .map(DataLine::new)
                    .collect(ArrayList<Pair<DataLine, List<DataLine>>>::new, // group each query(D) with its lines(C)
                            (l, e) -> {
                                if (!e.getType().equalsIgnoreCase("d"))
                                    buffer.add(e);
                                else
                                    l.add(new Pair<>(e, new ArrayList<>(buffer))); // copy current buffer state
                            }, ArrayList::addAll)
                    .stream()
                    .map(pair -> { // process stream of (query, lines) and transform to the stream of results
                        DataLine query = pair.getKey();
                        List<DataLine> dLines = pair.getValue();

                        List<DataLine> filteredLines = dLines.stream() // filter out dLines
                                .filter(dLine -> { // service filter
                                    if (query.getServiceNumber() == 0)
                                        return true;
                                    else if (query.getServiceVariation() == 0)
                                        return query.getServiceNumber().equals(dLine.getServiceNumber());
                                    else
                                        return query.getServiceVariation().equals(dLine.getServiceVariation());
                                })
                                .filter(dLine -> { // question filter
                                    if (query.getQuestionType() == 0) // 0 == * in query
                                        return true;
                                    else if (query.getQuestionType().equals(dLine.getQuestionType())) {

                                        if (query.getQuestionCategory() == 0) // 0 == * in query
                                            return true;
                                        else if (query.getQuestionCategory().equals(dLine.getQuestionCategory())) {

                                            if (query.getQuestionSubCategory() == 0) // 0 == * in query
                                                return true;
                                            else return query.getQuestionSubCategory().equals(
                                                    dLine.getQuestionSubCategory());
                                        } else
                                            return false;
                                    } else
                                        return false;
                                })
                                .filter(dLine -> { // date filter
                                    LocalDate startDate = query.getResponseDate();
                                    LocalDate endDate = query.getResponseDate2();

                                    if (startDate.isEqual(endDate)) // endDate is omitted or equals
                                        return dLine.getResponseDate().isEqual(startDate);

                                    // Rule: dLine.date >= query.date1 && dLine.date <= query.date2
                                    return !(dLine.getResponseDate().isBefore(startDate) ||
                                            dLine.getResponseDate().isAfter(endDate));
                                })
                                .collect(Collectors.toList());

                        int timeSum = filteredLines.stream()
                                .map(DataLine::getTime)
                                .reduce(0, (acc, n) -> acc + n);
                        int filteredQuantity = filteredLines.size();

                        return timeSum == 0 ? "-" : String.valueOf(timeSum / filteredQuantity); // avgTime as result
                    })
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
