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

import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.async.MonitrServiceAsyncNio;

import java.util.function.Supplier;
import java.util.stream.Stream;


public class App {

    public static void main(String[] args) throws Exception {
        try(MonitrServiceAsyncNio service = new MonitrServiceAsyncNio()) {
            System.out.println("########### Async approach.... ");
            final Stream<MonitrMarketData> data3 = measure(() -> service.GetLastNews()); // 1 Http request + 1 Http request
            final MonitrMarketData first3 = measure(() -> data3.findFirst().get());
            System.out.println("Doing stuff....");
            sleep(2000); // Doing stuff....
            measure(() -> first3.getStockDetails());
            measure(() -> first3.getStockDetails().getAnalysis()); // 1 Http request
            System.out.println("########### Async approach.... ");
            measure(() -> first3);
            measure(() -> first3.getStockDetails()); // 1 Http request
            measure(() -> first3.getStockDetails().getAnalysis()); // 1 Http request
        }
    }

    private static void sleep(int dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T measure(Supplier<T> action) {
        long start = System.nanoTime();
        T res = action.get();
        long duration = (System.nanoTime() - start) / 1_000; // micro seconds
        System.out.println( "[" + duration + " us] " + res);
        return res;
    }
}
