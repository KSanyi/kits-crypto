package kits.crypto.base;

public class Pair<T, S> {

    public final T first;
    
    public final S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        return String.format("[%s, %s]", first.toString(), second.toString());
    }
    
} 
