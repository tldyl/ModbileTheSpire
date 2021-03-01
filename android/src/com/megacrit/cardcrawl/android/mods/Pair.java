package com.megacrit.cardcrawl.android.mods;

import java.util.Objects;

public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        } else {
            Pair<K, V> other = (Pair<K, V>) o;
            return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
        }
    }

    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }
}
