package io.smart.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pair<E1, E2> implements Serializable {
    private E1 first;
    private E2 second;

    public Pair() {
    }

    public Pair(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E1 first() {
        return this.first;
    }

    public E2 second() {
        return this.second;
    }

    public void setFirst(E1 first) {
        this.first = first;
    }

    public void setSecond(E2 second) {
        this.second = second;
    }

    public Pair<E1, E2> copy() {
        return new Pair<>(this.first, this.second);
    }

    public Map<E1, E2> toHashMap() {
        HashMap<E1, E2> hashMap = new HashMap<>();
        hashMap.put(this.first, this.second);
        return hashMap;
    }

    public static <E1, E2> Pair<E1, E2> of(E1 first, E2 second) {
        return new Pair<>(first, second);
    }

    public String toString() {
        return "Pair{" + "first=" + this.first +
                ", second=" + this.second +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Pair)) {
            return false;
        } else {
            Pair<?, ?> pair = (Pair<?, ?>)o;
            return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
        }
    }

    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @SafeVarargs
    public static <E1, E2> Map<E1, E2> toMap(Pair<E1, E2>... pairs) {
        return (Map) Stream.of(pairs).collect(Collectors.toMap(Pair::first, Pair::second));
    }
}

