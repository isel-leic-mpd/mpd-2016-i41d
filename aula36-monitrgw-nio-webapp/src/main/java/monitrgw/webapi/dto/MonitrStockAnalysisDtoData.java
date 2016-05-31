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

package monitrgw.webapi.dto;

public class MonitrStockAnalysisDtoData {
    public final int mentions;
    public final double totalSentiment;
    public final double averageSentiment;
    public final int positive;
    public final int negative;

    public MonitrStockAnalysisDtoData(
            int mentions,
            double totalSentiment,
            double averageSentiment,
            int positive,
            int negative)
    {
        this.mentions = mentions;
        this.totalSentiment = totalSentiment;
        this.averageSentiment = averageSentiment;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public String toString() {
        return "MonitrStockAnalysisDtoData{" +
                "mentions=" + mentions +
                ", totalSentiment=" + totalSentiment +
                ", averageSentiment=" + averageSentiment +
                ", positive=" + positive +
                ", negative=" + negative +
                '}';
    }
}
