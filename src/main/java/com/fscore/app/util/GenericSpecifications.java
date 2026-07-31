package com.fscore.app.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class GenericSpecifications {

    private static final Set<String> IGNORED_PARAMS = Set.of("page", "size", "sort");
    private static final String SEARCH_PARAM = "search";

    private GenericSpecifications() {}

    public static <T> Specification<T> build(Map<String, String> params, String[] searchPaths) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (params == null || params.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key == null || value == null || value.isBlank()) {
                    continue;
                }
                if (IGNORED_PARAMS.contains(key)) {
                    continue;
                }
                if (SEARCH_PARAM.equals(key)) {
                    addSearchPredicate(root, cb, predicates, value, searchPaths);
                    continue;
                }
                addFilterPredicate(root, cb, predicates, key, value);
            }
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void addSearchPredicate(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates,
                                               String value, String[] searchPaths) {
        if (searchPaths == null || searchPaths.length == 0) {
            return;
        }
        List<Predicate> likes = new ArrayList<>();
        for (String path : searchPaths) {
            try {
                Path<Object> resolved = resolve(root, path);
                Expression<String> lowered = cb.lower(resolved.as(String.class));
                likes.add(cb.like(lowered, "%" + value.toLowerCase(Locale.ROOT) + "%"));
            } catch (RuntimeException ignored) {
                // Path not resolvable or not string-compatible: skip it.
            }
        }
        if (!likes.isEmpty()) {
            predicates.add(cb.or(likes.toArray(new Predicate[0])));
        }
    }

    private static <T> void addFilterPredicate(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates,
                                               String key, String value) {
        try {
            Path<Object> path = resolveOrTranslate(root, key);
            Class<?> javaType = path.getModel().getBindableJavaType();
            if (value.contains(",")) {
                String[] parts = value.split(",");
                Object[] values = new Object[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isBlank()) {
                        values[i] = convert(parts[i].trim(), javaType);
                    }
                }
                predicates.add(path.in(values));
            } else {
                predicates.add(cb.equal(path, convert(value, javaType)));
            }
        } catch (RuntimeException ignored) {
            // Unknown attribute: ignore the filter instead of failing the query.
        }
    }

    private static <T> Path<Object> resolveOrTranslate(Root<T> root, String key) {
        try {
            return resolve(root, key);
        } catch (IllegalArgumentException ex) {
            if (key.endsWith("Id") && key.length() > 2) {
                String base = key.substring(0, key.length() - 2);
                return resolve(root, base + ".id");
            }
            throw ex;
        }
    }

    private static <T> Path<Object> resolve(Root<T> root, String path) {
        Path<?> current = root;
        for (String part : path.split("\\.")) {
            current = current.get(part);
        }
        @SuppressWarnings("unchecked")
        Path<Object> casted = (Path<Object>) current;
        return casted;
    }

    private static Object convert(String value, Class<?> javaType) {
        if (javaType == Boolean.class || javaType == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (javaType == Integer.class || javaType == int.class) {
            return Integer.valueOf(value);
        }
        if (javaType == Long.class || javaType == long.class) {
            return Long.valueOf(value);
        }
        if (javaType == Short.class || javaType == short.class) {
            return Short.valueOf(value);
        }
        if (javaType == Double.class || javaType == double.class) {
            return Double.valueOf(value);
        }
        if (javaType == Float.class || javaType == float.class) {
            return Float.valueOf(value);
        }
        if (javaType == BigDecimal.class) {
            return new BigDecimal(value);
        }
        return value;
    }
}
