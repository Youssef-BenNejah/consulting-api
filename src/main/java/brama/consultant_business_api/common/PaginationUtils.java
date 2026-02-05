package brama.consultant_business_api.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {
    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 200;

    private PaginationUtils() {
    }

    public static Pageable toPageable(final Integer page, final Integer size, final Sort sort) {
        final int normalizedPage = normalizePage(page);
        final int normalizedSize = normalizeSize(size);
        return PageRequest.of(normalizedPage - 1, normalizedSize, sort == null ? Sort.unsorted() : sort);
    }

    public static int normalizePage(final Integer page) {
        if (page == null || page < 1) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    public static int normalizeSize(final Integer size) {
        if (size == null || size < 1) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }
}
