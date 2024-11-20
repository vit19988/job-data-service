package com.peeerawit.jobdataservice.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.peeerawit.jobdataservice.entity.JobData;
import com.peeerawit.jobdataservice.entity.QJobData;
import jakarta.persistence.EntityManager;

@Service
public class JobDataSearchService extends BaseSearchService {
    private static final Map<String, Expression<?>> FIELD_EXPRESSION_MAP = new HashMap<>();

    static {
        FIELD_EXPRESSION_MAP.put("job_title", QJobData.jobData.jobTitle);
        FIELD_EXPRESSION_MAP.put("timestamp", QJobData.jobData.timestamp);
        FIELD_EXPRESSION_MAP.put("gender", QJobData.jobData.gender);
        FIELD_EXPRESSION_MAP.put("salary", QJobData.jobData.salary);
    }

    public JobDataSearchService(EntityManager entityManager) {
        super(entityManager);
    }

    public List<JobData> getAllEmployees(Map<String, String> filters, List<String> fields, Map<String, String> sorts) {
        QJobData jobData = QJobData.jobData;
        List<Expression<?>> expressions = createExpressionClause(FIELD_EXPRESSION_MAP, fields);
        BooleanBuilder whereClause = createWhereClause(jobData, filters);
        List<OrderSpecifier<?>> orderClause = createSortClause(jobData, sorts);

        JPAQuery<JobData> query = expressions.isEmpty()
          ? queryFactory.selectFrom(jobData)
          : queryFactory
          .select(Projections.bean(JobData.class, expressions.toArray(new Expression[0])))
          .from(jobData);

        return query
          .where(whereClause)
          .orderBy(orderClause.toArray(new OrderSpecifier<?>[0]))
          .fetch();
    }

    private List<OrderSpecifier<?>> createSortClause(QJobData qJobData, Map<String, String> sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (sort != null) {
            for (Map.Entry<String, String> entry : sort.entrySet()) {
                String field = entry.getKey();
                String order = entry.getValue();
                ComparableExpressionBase<?> path;
                try {
                    Field fieldObject = QJobData.class.getField(field);
                    path = NumberPath.class.isAssignableFrom(fieldObject.getType())
                      ? (NumberPath<?>) fieldObject.get(qJobData)
                      : (StringPath) fieldObject.get(qJobData);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                orders.add(order.equalsIgnoreCase("desc") ? path.desc() : path.asc());
            }
        }

        return orders;
    }

    private BooleanBuilder createWhereClause(QJobData jobData, Map<String, String> filters) {
        BooleanBuilder whereClause = new BooleanBuilder();
        addStringWhereClause(whereClause, filters.get("job_title"), jobData.jobTitle);
        addStringWhereClause(whereClause, filters.get("gender"), jobData.gender);
        addSalaryWhereClause(whereClause, filters, jobData.salary);

        return whereClause;
    }

    private void addSalaryWhereClause(BooleanBuilder whereClause, Map<String, String> queryParams, NumberPath<BigDecimal> field) {
        String salaryGteFilter = queryParams.get("salary[gte]");
        String salaryLteFilter = queryParams.get("salary[lte]");
        String salaryFilter = queryParams.get("salary");

        if (salaryGteFilter != null && !salaryGteFilter.trim().isEmpty()) {
            addNumericWhereClause(whereClause, field, salaryGteFilter.trim(), this :: goe);
        }

        if (salaryLteFilter != null && !salaryLteFilter.trim().isEmpty()) {
            addNumericWhereClause(whereClause, field, salaryLteFilter.trim(), this :: loe);
        }

        if (salaryFilter != null && !salaryFilter.trim().isEmpty()) {
            addNumericWhereClause(whereClause, field, salaryFilter.trim(), this :: eq);
        }
    }
}
