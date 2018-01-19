package com.ote.user.rights.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path implements Iterable<String> {

    private static final String Separator = "/";

    private final String[] path;

    private Path(List<String> path) {
        this(path.toArray(new String[0]));
    }

    @Override
    public String toString() {
        return stream().collect(Collectors.joining(Separator));
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(path).iterator();
    }

    public Stream<String> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    //region Parser
    public static class Parser implements Supplier<Path> {

        private final List<String> path = new ArrayList<>();

        public Parser(String element) {
            String[] s = element.split(Separator);
            Arrays.stream(s).forEach(p -> path.add(p));
        }

        public Path get() {
            return new Path(path);
        }
    }
    //endregion

    //region Builder
    @NoArgsConstructor
    public static class Builder implements Supplier<Path> {

        private final List<String> path = new ArrayList<>();

        public Builder(String element) {
            this.path.add(element);
        }

        public Builder then(String element) {
            this.path.add(element);
            return this;
        }

        public Path get() {
            return new Path(path);
        }
    }
    //endregion
}
