import java.util.*;

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
    public final Trader trader;
    public  final int year;
    public  final int value;
    public Transaction(Trader trader, int year, int value){
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
    public String toString(){
        return "{" + this.trader + ", " + "year: "+this.year+", " + "value:" + this.value +"}";
    }
}

public class Transactions{
    public static List<Transaction> init(){
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        return Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );
    }
    public static void main(String [] args){
        List<Transaction> trxs = init();
        
        /*
        System.out.println("1. Find all transactions in the year 2011 and sort them by value (small to high)");
        trxs.stream()
            .filter(t -> t.year == 2011)
            .sorted(Comparator.comparing(t -> t.value)) // Function<Transaction, Integer>
            .forEach(System.out::println);
        
        
        trxs.stream()
            .filter(t -> t.year == 2011)
            .mapToInt(t -> t.value) 
            .sorted()
            .forEach(System.out::println);
        
        System.out.println("\n2. What are all the unique cities where the traders work?");
        trxs.stream()
            .map(t -> t.trader.city)
            .distinct()
            .forEach(System.out::println);
        
        System.out.println("\n3. Find all traders from Cambridge and sort them by name.");
        trxs.stream()
            .map(t -> t.trader)
            // .distinct() // Usa o equals() 
            .filter(t -> t.city.equals("Cambridge"))
            .sorted(Comparator.comparing(t -> t.name))
            .forEach(System.out::println);
        
        
        System.out.println("\n4. Return a string of all traders’ names sorted alphabetically.");
        trxs.stream()
            .map(t -> t.trader.name)
            .distinct()
            .sorted(String::compareTo)
            .reduce((prev, curr) -> prev + " " + curr)
            .ifPresent(System.out::println);
          
        System.out.println("\n5. Are any traders based in Milan?");
        trxs.stream()
            .map(t -> t.trader)
            .filter(t -> t.city.equals("Milan"))
            .findAny()
            .ifPresent(System.out::println);
        
        System.out.println("\n6. Print all transactions’ values from the traders living in Cambridge.");
        trxs.stream()
            .filter(t -> t.trader.city.equals("Cambridge"))
            .map(t -> t.value)
            .forEach(System.out::println);
        */
        
        System.out.println("\n7.a What's the highest value of all the transactions?");
        trxs.stream()
            .map(t -> t.value) // boxing
            .sorted((v1, v2) -> v2 - v1)
            .findFirst()
            .ifPresent(System.out::println);
        
        System.out.println("\n7.b What's the highest value of all the transactions?");
        trxs.stream()
            .map(t -> t.value)        // boxing 
            .max((v1, v2) -> v1 - v2) // ++ eficiente
            .ifPresent(System.out::println);
        
        System.out.println("\n7.c What's the highest value of all the transactions?");
        trxs.stream()
            .map(t -> t.value)
            .reduce((prev, next) -> prev > next ? prev : next)
            .ifPresent(System.out::println);
            
        System.out.println("\n7.d What's the highest value of all the transactions?");
        trxs.stream()
            .map(t -> t.value)
            .reduce(Integer::max)
            .ifPresent(System.out::println);
        
        System.out.println("\n7.e What's the highest value of all the transactions?");
        trxs.stream()
            .mapToInt(t -> t.value)
            .max()
            .ifPresent(System.out::println);
        
        /*
        System.out.println("8. Find the transaction with the smallest value.");
        trxs.stream()
            .min((v1, v2) -> v1.value - v2.value)
            .ifPresent(System.out::println);
 */       
    }
}