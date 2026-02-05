package brama.consultant_business_api.common;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collection;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static void addRegexOrCriteria(final Query query, final String search, final String... fields) {
        if (search == null || search.isBlank() || fields == null || fields.length == 0) {
            return;
        }
        final Criteria[] criteria = new Criteria[fields.length];
        for (int i = 0; i < fields.length; i++) {
            criteria[i] = Criteria.where(fields[i]).regex(search, "i");
        }
        query.addCriteria(new Criteria().orOperator(criteria));
    }

    public static void addIfNotBlank(final Query query, final String field, final String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        query.addCriteria(Criteria.where(field).is(value));
    }

    public static void addIfNotNull(final Query query, final String field, final Object value) {
        if (value == null) {
            return;
        }
        query.addCriteria(Criteria.where(field).is(value));
    }

    public static void addIfNotEmpty(final Query query, final String field, final Collection<?> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        query.addCriteria(Criteria.where(field).in(values));
    }

    public static void addDateRange(final Query query, final String field, final LocalDate from, final LocalDate to) {
        if (from == null && to == null) {
            return;
        }
        final Criteria criteria = Criteria.where(field);
        if (from != null && to != null) {
            query.addCriteria(criteria.gte(from).lte(to));
            return;
        }
        if (from != null) {
            query.addCriteria(criteria.gte(from));
            return;
        }
        query.addCriteria(criteria.lte(to));
    }

    public static void addInstantRange(final Query query, final String field, final Instant from, final Instant to) {
        if (from == null && to == null) {
            return;
        }
        final Criteria criteria = Criteria.where(field);
        if (from != null && to != null) {
            query.addCriteria(criteria.gte(from).lte(to));
            return;
        }
        if (from != null) {
            query.addCriteria(criteria.gte(from));
            return;
        }
        query.addCriteria(criteria.lte(to));
    }

    public static Instant startOfDayUtc(final LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    public static Instant endOfDayUtc(final LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
    }
}
