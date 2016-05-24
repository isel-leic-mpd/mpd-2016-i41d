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
package monitrgw.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class HttpGetter {

    public static <T> T httpGet(String path, Function<String, T> conv){
        try (InputStream in = new URL(path).openStream()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String content = reader.lines().collect(joining());
                return conv.apply(content);
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
