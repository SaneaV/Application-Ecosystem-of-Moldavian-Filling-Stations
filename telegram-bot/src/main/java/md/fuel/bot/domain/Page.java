package md.fuel.bot.domain;

import java.io.Serializable;
import java.util.List;

public record Page<T>(
    int totalResults,
    List<T> items) implements Serializable {

}
