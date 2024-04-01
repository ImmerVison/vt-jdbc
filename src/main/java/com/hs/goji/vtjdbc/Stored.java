package com.hs.goji.vtjdbc;

public final class Stored<T> {
    private final long _id;
    private final T _value;

    private Stored(final long id, final T value) {
        _id = id;
        _value = value;
    }

    public long getID() {
        return _id;
    }

    public T getValue() {
        return _value;
    }


    public String toString() {
        return String.format("Stored[id=%d, %s]", _id, _value);
    }

    public static <T> Stored<T> of(final long id, final T value) {
        return new Stored<T>(id, value);
    }





}
