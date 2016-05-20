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
import monitrgw.domain.eager.MonitrServiceEager;
import monitrgw.domain.lazy.MonitrServiceLazy;
import monitrgw.util.HttpGetter;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;

import java.util.function.Supplier;


public class App {

    public static void main(String[] args) throws InterruptedException {
        MonitrMarketDto marketData = HttpGetter.httpGet("http://api.monitr.com/api/v1/market/news?apikey=1e3f8640-f754-11e3-97e9-179fff8a3cc5",
                content -> new Gson().fromJson(content, MonitrMarketDto.class));
        System.out.println( marketData);
    }
}
