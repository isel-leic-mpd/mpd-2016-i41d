/*
 * Copyright (C) 2015 Miguel Gamboa at CCISEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package monitrgw;

import monitrgw.domain.async.MonitrServiceAsyncNio;
import util.HttpServer;

import java.io.IOException;
import java.util.function.Supplier;
import static java.util.stream.Collectors.joining;


public class App {

    public static void main(String[] args) throws Exception {
        try(MonitrController ctr = new MonitrController(new MonitrServiceAsyncNio())) {
            new HttpServer(3000)
                    .addHandler("/news", req -> measure(() -> ctr.getNews(req)))
                    .addHandler("/stock/*", req -> measure(() -> ctr.getStock(req)))
                    .run();

        }
    }

    private static void sleep(int dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T measure(SupplierThrowable<T> action) throws IOException {
        long start = System.nanoTime();
        T res = action.get();
        long duration = (System.nanoTime() - start) / 1_000; // micro seconds
        System.out.println( "[" + duration + " us]");
        return res;
    }

    interface SupplierThrowable<T> { public T get() throws IOException; }
}
