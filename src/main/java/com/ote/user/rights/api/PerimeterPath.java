package com.ote.user.rights.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PerimeterPath implements Iterable<String> {

    @Getter
    private final String[] path;

    private PerimeterPath(List<String> path) {
        this(path.toArray(new String[0]));
    }

    @Override
    public String toString() {
        return Arrays.stream(path).collect(Collectors.joining("/"));
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(path).iterator();
    }

    //region Parser
    public static class Parser implements Supplier<PerimeterPath> {

        private final List<String> path = new ArrayList<>();

        public Parser(String element) {
            String[] s = element.split("/");
            Arrays.stream(s).forEach(p -> path.add(p));
        }

        public PerimeterPath get() {
            return new PerimeterPath(path);
        }
    }
    //endregion

    //region Builder
    public static class Builder implements Supplier<PerimeterPath> {

        private final List<String> path = new ArrayList<>();

        public Builder(String element) {
            this.path.add(element);
        }

        public Builder then(String element) {
            this.path.add(element);
            return this;
        }

        public PerimeterPath get() {
            return new PerimeterPath(path);
        }
    }
    //endregion
}
