package monitrgw.util;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Miguel Gamboa
 *         created on 27-05-2016
 */
public class Cache {

    static class Initializer<T, R> implements Function<T, R> {

        private Function<T, R> src;

        public Initializer(Function<T, R> s) {
            this.src = (arg) ->{
                R val = s.apply(arg);
                this.src = (a) -> val;
                return val;
            };
        }

        @Override
        public R apply(T arg) {
            return src.apply(arg);
        }
    }

    public static <T, R> Function<T, R> cache(Function<T, R> initialization){
        return new Initializer<>(initialization);
    }

    /*
    private static <T, R> Function<T, R> cache(Function<T, R> init){
        final Object[] c = {null};
        return arg -> {
            c[0] = c[0] != null ? c[0] : init.apply(arg);
            return (R) c[0];
        };
    }
    */
}
