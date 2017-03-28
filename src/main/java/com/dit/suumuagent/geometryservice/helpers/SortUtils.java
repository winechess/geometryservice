package com.dit.suumuagent.geometryservice.helpers;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public final class SortUtils {

    private SortUtils() {
    }

    public static Sort getSort(List<String> sortBy) {

        if (sortBy == null || sortBy.isEmpty()) return null;
        List<Sort.Order> orders = new ArrayList<>();
        sortBy.forEach(s -> orders.add(buildSortOrderFromString(s)));
        return new Sort(orders);
    }

    private static Sort.Order buildSortOrderFromString(String sortOrder) {
        String[] split = sortOrder.split(":");
        if (split.length != 2) throw new IllegalArgumentException("Не верно переданы параметры сортировки.");
        return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
    }

}
