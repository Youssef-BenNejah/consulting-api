package brama.consultant_business_api.common;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public final class SortUtils {
    private SortUtils() {
    }

    public static Sort parseSort(final String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }
        final List<Sort.Order> orders = new ArrayList<>();
        final String[] parts = sort.split(";");
        for (final String part : parts) {
            final String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            final String[] tokens = trimmed.split(",");
            final String field = tokens[0].trim();
            if (field.isEmpty()) {
                continue;
            }
            Sort.Direction direction = Sort.Direction.ASC;
            if (tokens.length > 1) {
                final String dirToken = tokens[1].trim();
                if (dirToken.equalsIgnoreCase("desc")) {
                    direction = Sort.Direction.DESC;
                }
            }
            orders.add(new Sort.Order(direction, field));
        }
        if (orders.isEmpty()) {
            return Sort.unsorted();
        }
        return Sort.by(orders);
    }
}
