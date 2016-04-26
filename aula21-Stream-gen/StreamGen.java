import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamGen {

    public static void main(String [] args) {
        
        Stream
            .generate(() -> Math.random())
            .limit(5)
            .forEach(System.out::println);
    }
}
