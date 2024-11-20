package com.peeerawit.jobdataservice.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

@Service
public abstract class BaseSearchService {
    protected final JPAQueryFactory queryFactory;

    protected BaseSearchService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    protected List<Expression<?>> createExpressionClause(Map<String, Expression<?>> fieldsExpressionMap, List<String> fields) {
        return fields == null ? Collections.emptyList() : fields.stream()
          .filter(fieldsExpressionMap :: containsKey)
          .map(fieldsExpressionMap :: get)
          .collect(Collectors.toList());
    }

    protected void addStringWhereClause(BooleanBuilder whereClause, String value, StringPath field) {
        if (value != null && !value.trim().isEmpty()) {
            whereClause.and(field.equalsIgnoreCase(value.trim()));
        }
    }

    protected void addNumericWhereClause(BooleanBuilder whereClause, NumberPath<BigDecimal> field, String value,
                                         BiFunction<NumberPath<BigDecimal>, BigDecimal, BooleanExpression> operation) {
        try {
            BigDecimal val = new BigDecimal(value);
            whereClause.and(operation.apply(field, val));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
              String.format("Received invalid string value: %s. Can't be converted to number.", value), ex);
        }
    }

    protected BooleanExpression goe(NumberPath<BigDecimal> field, BigDecimal value) {
        return field.goe(value);
    }

    protected BooleanExpression loe(NumberPath<BigDecimal> field, BigDecimal value) {
        return field.loe(value);
    }

    protected BooleanExpression eq(NumberPath<BigDecimal> field, BigDecimal value) {
        return field.eq(value);
    }
}
