package kits.crypto.base;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FrequencyMap<T> {

    private Map<T, Integer> map = new HashMap<>();

    public void put(T elem) {
        map.put(elem, map.getOrDefault(elem, 0) + 1);
    }

    public T getMostFrequent() {
        final int max = map.values().stream().max(Comparator.naturalOrder()).orElse(-1);
        return map.keySet().stream().filter(elem -> map.get(elem) == max).findFirst().get();
    }

    public List<Entry<T, Integer>> sortedEntries() {
        List<Entry<T, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (e1, e2) -> e2.getValue() - e1.getValue());
        return Collections.unmodifiableList(list);
    }

    public Integer frequency(T elem) {
        return map.getOrDefault(elem, 0);
    }

    @Override
    public String toString() {
        return map.keySet().stream().map(elem -> elem + " " + map.get(elem)).collect(Collectors.joining("\n"));
    }

}
