package com.example.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ListUtil {

    private ListUtil() {
    }

    public static <T> List<T> add(List<T> list, T item) {
        var l = new ArrayList<>(list);
        l.add(item);
        return l;
    }

    public static <T, K> List<T> replace(List<T> list, T newItem, Function<T, K> keyMapper) {
        var newList = new ArrayList<>(list);
        var newKey = keyMapper.apply(newItem);
        for (var i = 0; i < newList.size(); i++) {
            var oldItem = newList.get(i);
            if (newKey.equals(keyMapper.apply(oldItem))) {
                newList.remove(i);
                newList.add(i, newItem);
                break;
            }
        }
        return newList;
    }

    public static <T> List<T> remove(List<T> list, T item) {
        var l = new ArrayList<>(list);
        l.remove(item);
        return l;
    }
}
