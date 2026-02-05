package brama.consultant_business_api.common;

import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 200;

    private PaginationUtils() {
    }

    public static Pageable toPageable(final Integer page, final Integer size, final Sort sort) {
        final int normalizedPage = normalizePage(page);
        final int normalizedSize = normalizeSize(size);
        return PageRequest.of(normalizedPage, normalizedSize, sort == null ? Sort.unsorted() : sort);
    }

    public static int normalizePage(final Integer page) {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        if (page < 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "page must be >= 0");
        }
        return page;
    }

    public static int normalizeSize(final Integer size) {
        if (size == null) {
            return DEFAULT_SIZE;
        }
        if (size < 1) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "size must be >= 1");
        }
        return Math.min(size, MAX_SIZE);
    }
}
