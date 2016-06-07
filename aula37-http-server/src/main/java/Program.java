import java.time.LocalTime;
import java.util.Random;

/**
 * @author Miguel Gamboa
 *         created on 03-06-2016
 */
public class Program {
    public static void main(String[] args) throws Exception {
            new HttpServer()
                    .addHandler("/timev2", (req) -> String.format("Time is %s\nVersion:0.4.1", LocalTime.now().toString()))
                    .addHandler("/ola", (req) -> "isel " + Math.random())
                    .addHandler("/aluno/*", (req) -> "aluno nr: " + req.getPathInfo().substring(1) )
                    .run();
    }
}
