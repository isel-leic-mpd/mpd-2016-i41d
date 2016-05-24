import java.util.concurrent.*;
import java.util.*;

class App{

    public static void main(String [] args) throws Exception{
        System.out.println(calculatePrice("bag"));
        System.out.println(calculatePrice("tablet"));
    }
    
    private static double calculatePrice(String product) { 
        delay();
        double res = random.nextDouble() * product.charAt(0) + product.charAt(1);
        return ((int) (res * 100)) / 100.0;
    }
    
    private static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static final Random random = new Random();
    
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /****************************************************************/
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    public static long measurePerformance(Runnable action){
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 1; i++) {
            long start = System.nanoTime();
            action.run();
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println(duration + " ms");
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }
}