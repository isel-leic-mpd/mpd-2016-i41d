package queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 14-04-2016
 */
public class App {
    public static void main(String[] args) {
        List<String> src = Arrays.asList("isel", "ola", "isel", "ola", "super", "babel", "super");

        int count = 0;
        double sum = 0;
        for (String word: src) {
            count++;
            sum += word.length();
        }
        System.out.printf("Tamanho medio das palavras = %.2f\n", (sum/count));

        src.stream()
                .distinct()
                .filter(item -> !item.equals("isel"))
                .map(String::length)
                .limit(3)
                .forEach(System.out::println);

    }
}
