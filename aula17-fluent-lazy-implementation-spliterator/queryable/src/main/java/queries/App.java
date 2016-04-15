package queries;

import java.util.Arrays;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 14-04-2016
 */
public class App {
    public static void main(String[] args) {
        List<String> src = Arrays.asList("isel", "ola", "isel", "ola", "super", "babel", "super");
        Queryable
                .of(src)
                .distinct()
                // .filter(item -> !item.equals("isel"))
                .map(String::length)
                .limit(3)
                .forEach(System.out::println);

    }
}
