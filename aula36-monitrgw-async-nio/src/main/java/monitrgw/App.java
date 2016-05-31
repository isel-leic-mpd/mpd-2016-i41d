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

import com.google.gson.Gson;
import monitrgw.domain.IMonitrMarketData;
import monitrgw.domain.async.MonitrServiceAsync;
import monitrgw.domain.eager.MonitrServiceEager;
import monitrgw.domain.lazy.MonitrServiceLazy;
import monitrgw.util.HttpGetter;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;

import java.util.function.Supplier;
import java.util.stream.Stream;


public class App {

    public static void main(String[] args) throws InterruptedException {
        /*
        MonitrMarketDto marketData = HttpGetter.httpGet(
                "http://api.monitr.com/api/v1/market/news?apikey=1e3f8640-f754-11e3-97e9-179fff8a3cc5",
                content -> new Gson().fromJson(content, MonitrMarketDto.class));
        */

        // System.out.println( marketData);

        final Stream<IMonitrMarketData> data = new MonitrServiceEager().GetLastNews();

        System.out.println("########### Eager approach.... ");
        final IMonitrMarketData first = measure(() -> data.findFirst().get());  // 1 Http request + // 1 Http request
        measure(() -> first.getStockDetails());
        measure(() -> first.getStockDetails().getAnalysis());
        System.out.println("########### Eager approach.... ");
        measure(() -> first);
        measure(() -> first.getStockDetails());
        measure(() -> first.getStockDetails().getAnalysis());

        System.out.println("########### Lazy approach.... ");
        final Stream<IMonitrMarketData> data2 = new MonitrServiceLazy().GetLastNews();
        final IMonitrMarketData first2= measure(() -> data2.findFirst().get());
        measure(() -> first2.getStockDetails()); // 1 Http request
        measure(() -> first2.getStockDetails().getAnalysis()); // 1 Http request
        System.out.println("########### Lazy approach.... ");
        measure(() -> first2);
        measure(() -> first2.getStockDetails()); // 1 Http request
        measure(() -> first2.getStockDetails().getAnalysis()); // 1 Http request

        System.out.println("########### Async approach.... ");
        final Stream<IMonitrMarketData> data3 = new MonitrServiceAsync().GetLastNews(); // 1 Http request + 1 Http request
        final IMonitrMarketData first3 = measure(() -> data3.findFirst().get());
        sleep(200); // Doing stuff....
        measure(() -> first3.getStockDetails());
        measure(() -> first3.getStockDetails().getAnalysis()); // 1 Http request
        System.out.println("########### Async approach.... ");
        measure(() -> first3);
        measure(() -> first3.getStockDetails()); // 1 Http request
        measure(() -> first3.getStockDetails().getAnalysis()); // 1 Http request

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
