package wthr.model;

import java.time.LocalDate;

/**
 * @author Miguel Gamboa
 *         created on 18-03-2016
 */
public class HistoryArgs {
    public final String name;
    public final LocalDate start, end;

    public HistoryArgs(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
