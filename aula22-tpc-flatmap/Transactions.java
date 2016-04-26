import java.util.*;
import java.util.stream.*;

class Trader{
    public final String name;
    public  final String city;
    public Trader(String n, String c){
        this.name = n;
        this.city = c;
    }
    public String toString(){
        return "Trader:"+this.name + " in " + this.city;
    }
}

class Transaction{
    public final Trader[] traders;
    public  final int year;
    public  final int value;
    public Transaction(int year, int value, Trader...traders){
        this.traders = traders;
        this.year = year;
        this.value = value;
    }
    public String toString(){
        return "{" + this.traders + ", " + "year: "+this.year+", " + "value:" + this.value +"}";
    }
}

public class Transactions{
    public static Stream<Transaction> init(){
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        return Stream.of(
            new Transaction(2011, 300, brian, raoul),
            new Transaction(2012, 1000, raoul),
            new Transaction(2011, 400, raoul, brian, mario),
            new Transaction(2012, 710, mario, alan),
            new Transaction(2012, 700),
            new Transaction(2012, 950, brian, mario)
        );
    }
    public static void main(String [] args){
        
        init()
            .map(t -> Arrays.stream(t.traders))
            .forEach(traders -> traders.forEach(tr -> System.out.print(tr.name + " ")));
        
        System.out.println();
        
        init()
            .flatMap(t -> Arrays.stream(t.traders))
            .forEach(tr -> System.out.print(tr.name + " "));
        
        /*
        
        System.out.println("\n2. What are all the unique cities where the traders work?");
        
        System.out.println("\n3. Find all traders from Cambridge and sort them by name.");
        
        System.out.println("\n4. Return a string of all traders’ names sorted alphabetically.");
          
        System.out.println("\n5. Are any traders based in Milan?");
        
        System.out.println("\n6. Print all transactions’ values from the traders living in Cambridge.");
 */       
    }
}