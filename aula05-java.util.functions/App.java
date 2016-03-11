import java.util.function.*;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static <T> T fetch(Supplier<T> s){ return s.get(); }
    public static String fetchString(Supplier<String> s){ return s.get(); }
    public static void execute(Runnable r){ r.run(); }
    public static Supplier<Integer> getter() { return () -> {return 5;}; }

    public static void main(String [] args){
        System.out.println(fetchString(() -> {return "Ola";})); // Passagem de Lambda por parametro
        // System.out.println( // Erro de compilaçao
        //    execute( () -> {} /* instancia de Runnable */ )
        // ); // Passagem de Lambda por parametro
        // Predicate<WeatherInfo> evalTempC = (WeatherInfo w) -> w.tempC; // afectacao de Lambda a variavel
        if(!getter().equals(5)) // BUG != getter().get().equals(5)
            System.out.println("Getter not evaluating to 5 number"); // Lambda como retorno do método
    }
}

class WeatherInfo{
    public final int tempC;
    public WeatherInfo(int tempC){
        this.tempC = tempC;
    }
}