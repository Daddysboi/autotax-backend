package com.autotax.service.impl;

import com.autotax.dao.AppRepository;import com.autotax.dao.QueryResultTransformer;import com.autotax.domain.GenericStatusConstant;import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Olaleye Afolabi <oafolabi@byteworks.com.ng>
 */
@Repository
class AppRepositoryImpl implements AppRepository {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> Optional<E> fetchOne(JPAQuery<E> query) {
        return Optional.ofNullable(query.fetchOne());
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> long count(Class<E> type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);
        criteriaQuery.select(cb.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> Optional<E> findById(Class<E> type, Object id) {
        return Optional.ofNullable(entityManager.find(type, id));
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> List<E> findByIds(Class<E> type, Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> clientCriteriaQuery = criteriaBuilder.createQuery(type);
        Root<E> clientRoot = clientCriteriaQuery.from(type);
        clientCriteriaQuery.where(clientRoot.get("id").in(ids));
        return entityManager.createQuery(clientCriteriaQuery).getResultList();
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    public <T> T unproxy(Class<T> tClass, T entity) {
        if (!Hibernate.isInitialized(entity)) {
            return findById(tClass, ((HibernateProxy) entity).getHibernateLazyInitializer().getIdentifier()).orElse(null);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    public <T> List<T> unproxyAll(Class<T> tClass, List<T> entities) {

        List<T> entityList = new ArrayList<>();

        for (T entity : entities) {
            entityList.add(unproxy(tClass, entity));
        }

        return entityList;
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <T> List<T> getByProperty(Class<T> tClass, String propertyName, Object propertyValue) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> clientCriteriaQuery = criteriaBuilder.createQuery(tClass);
        Root<T> clientRoot = clientCriteriaQuery.from(tClass);
        Predicate predicate = criteriaBuilder.equal(clientRoot.get(propertyName), propertyValue);
        clientCriteriaQuery.where(predicate);
        return entityManager.createQuery(clientCriteriaQuery).getResultList();
    }

    @Override
    @Transactional(transactionManager = "defaultTransactionManager")
    public <E> E persist(E e) {
        entityManager.persist(e);
        return e;
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    public <E> E merge(E e) {
        return entityManager.merge(e);
    }

    @Override
    public void remove(Object e) {
        entityManager.remove(e);
    }

    @Override
    public <E> JPAQuery<E> startJPAQuery(EntityPath<E> entityPath) {
        return new JPAQuery<E>(entityManager).from(entityPath);
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> QueryResults<E> fetchResults(JPAQuery<E> query) {
        return query.fetchResults();
    }

    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> List<E> fetchResultList(JPAQuery<E> query) {
        return query.fetch();
    }

    @Override
    public <E, T> QueryResults<T> fetchResults(JPAQuery<E> query, QueryResultTransformer<E, T> transformer) {
        QueryResults<E> results = query.fetchResults();
        return new QueryResults<T>(results.getResults().stream().map(transformer::transaform)
                .collect(Collectors.toList()), new QueryModifiers(results.getLimit(), results.getOffset()), results.getTotal());
    }

    @Override
    public <E> OrderSpecifier<?> getSortedColumn(Order order, String columnName, EntityPath<E> entityPath) {
        Path<Object> fieldPath = Expressions.path(Object.class, entityPath, columnName);
        return new OrderSpecifier(order, fieldPath);
    }

    /**
     * Creates an OrderSpecifier for ordering based on priority IDs.
     *
     * @param priorityIds List of IDs to prioritize.
     * @param idPath Path to the ID field in the entity.
     * @param defaultOrder The default ordering if priority IDs are not matched.
     * @return OrderSpecifier to be used in QueryDSL queries.
     */
    @Override
    public <E> OrderSpecifier<?> getPriorityOrderSpecifier(List<Long> priorityIds, NumberPath<Long> idPath, Order defaultOrder) {
        if (priorityIds == null || priorityIds.isEmpty()) {
            return new OrderSpecifier<>(defaultOrder, idPath);
        }

        NumberTemplate<Integer> priorityOrderingTemplate = Expressions.numberTemplate(
                Integer.class,
                "CASE WHEN {0} IN ({1}) THEN 0 ELSE 1 END",
                idPath,
                priorityIds
        );

        return new OrderSpecifier<>(defaultOrder, priorityOrderingTemplate);
    }


    @Transactional(readOnly = true, transactionManager = "defaultTransactionManager")
    @Override
    public <E> Optional<E> findFirstByField(Class<E> type, String columnName, Object value) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteria = builder.createQuery(type);
        Root<E> from = criteria.from(type);
        criteria.select(from);
        criteria.where(builder.equal(from.get(columnName), value));
        try {
            return Optional.ofNullable(entityManager.createQuery(criteria).setMaxResults(1).getSingleResult());
        } catch (final NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public <E> Long countByProperty(Class<E> type, String propertyName, Object propertyValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);
        criteriaQuery.select(cb.count(root));

        List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(
                cb.and(
                        cb.equal(root.get(propertyName), propertyValue),
                        cb.equal(root.get("status"), GenericStatusConstant.ACTIVE)
                )
        );
        criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting entity by property.", e);
            return null;
        }
    }

    @Override
    public void setLockTimeout(long timeOutInMilliSeconds) {
        entityManager.createNativeQuery(String.format("set lock_timeout = %d", timeOutInMilliSeconds)).executeUpdate();
    }

    @Override
    public String getLockTimeout() {
        return (String) entityManager.createNativeQuery("show lock_timeout").getSingleResult();
    }
}
