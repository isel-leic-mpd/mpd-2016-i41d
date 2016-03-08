/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
 
@FunctionalInterface
interface Action { void apply(); } // Interface Funciona : SÓ tem 1 método
interface Supplier<T> { T get(); }
interface Func<T, R> { R apply(T arg); }

/* Erro de Compilação
@FunctionalInterface
interface FuncSpecial<T,R> extends Func<T, R> { String special(); } // Tem 2 métodos: apply + special
*/

@FunctionalInterface
interface Adder{
    int add(int a, int b);
}

/* Erro de Compilação pq tem 2 métodos
@FunctionalInterface
interface SmartAdder extends Adder{
    int add(double a, double b);
}
*/ 

interface Nothing{
}

@FunctionalInterface
interface Subtracter extends Nothing{
    int sub(int a, int b);
}


public class App {
    public static void main(String[] args) {
        Action a1 = () -> {};
        Supplier<String> f2 = () -> "Raoul";
        Supplier<String> f3 = () -> {return "Mario";};
        Func<Integer, String> f4 = (Integer i) -> { return "Alan" + i; };
        Func<String, String> f5 = (String s) -> "Iron Man";
        // FuncSpecial<String, String> f6 = () -> "Superman";
    }
}
