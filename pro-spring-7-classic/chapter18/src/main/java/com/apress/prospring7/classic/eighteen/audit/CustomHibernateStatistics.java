/*
Freeware License, some rights reserved

Copyright (c) 2026 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring7.classic.eighteen.audit;

import org.hibernate.SessionFactory;
import org.hibernate.stat.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

///
/// @author iulianacosmina on 10/02/2026
///
@Component
@ManagedResource(description = "JMX managed resource",
        objectName = "jmxDemo:name=ProSpring7SingerApp-hibernate")
public class CustomHibernateStatistics {

    private final SessionFactory sessionFactory;

    public CustomHibernateStatistics(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Statistics stats;

    @PostConstruct
    private void init() {
        stats = sessionFactory.getStatistics();
    }

    @ManagedOperation(description="Get statistics for entity name")
    @ManagedOperationParameter(name = "entityName", description = "Full class name for the entity")
    public EntityStatistics getEntityStatistics(String entityName) {
        return stats.getEntityStatistics(entityName);
    }

    @ManagedOperation(description="Get statistics for role")
    @ManagedOperationParameter(name = "role", description = "Role name")
    public CollectionStatistics getCollectionStatistics(String role) {
        return stats.getCollectionStatistics(role);
    }

    @ManagedOperation(description="Get statistics for query")
    @ManagedOperationParameter(name = "hql", description = "Query name")
    public QueryStatistics getQueryStatistics(String hql) {
        return stats.getQueryStatistics(hql);
    }

    @ManagedAttribute
    public long getEntityDeleteCount() {
        return stats.getEntityDeleteCount();
    }

    @ManagedAttribute
    public long getEntityInsertCount() {
        return stats.getEntityInsertCount();
    }

    @ManagedAttribute
    public long getEntityLoadCount() {
        return stats.getEntityLoadCount();
    }

    @ManagedAttribute
    public long getEntityFetchCount() {
        return stats.getEntityFetchCount();
    }

    @ManagedAttribute
    public long getEntityUpdateCount() {
        return stats.getEntityUpdateCount();
    }

    @ManagedAttribute
    public long getQueryExecutionCount() {
        return stats.getQueryExecutionCount();
    }

    @ManagedAttribute
    public long getQueryCacheHitCount() {
        return stats.getQueryCacheHitCount();
    }

    @ManagedAttribute
    public long getQueryExecutionMaxTime() {
        return stats.getQueryExecutionMaxTime();
    }

    @ManagedAttribute
    public long getQueryCacheMissCount() {
        return stats.getQueryCacheMissCount();
    }

    @ManagedAttribute
    public long getQueryCachePutCount() {
        return stats.getQueryCachePutCount();
    }

    @ManagedAttribute
    public long getFlushCount() {
        return stats.getFlushCount();
    }

    @ManagedAttribute
    public long getConnectCount() {
        return stats.getConnectCount();
    }

    @ManagedAttribute
    public long getSecondLevelCacheHitCount() {
        return stats.getSecondLevelCacheHitCount();
    }

    @ManagedAttribute
    public long getSecondLevelCacheMissCount() {
        return stats.getSecondLevelCacheMissCount();
    }

    @ManagedAttribute
    public long getSecondLevelCachePutCount() {
        return stats.getSecondLevelCachePutCount();
    }

    @ManagedAttribute
    public long getSessionCloseCount() {
        return stats.getSessionCloseCount();
    }

    @ManagedAttribute
    public long getSessionOpenCount() {
        return stats.getSessionOpenCount();
    }

    @ManagedAttribute
    public long getCollectionLoadCount() {
        return stats.getCollectionLoadCount();
    }

    @ManagedAttribute
    public long getCollectionFetchCount() {
        return stats.getCollectionFetchCount();
    }

    @ManagedAttribute
    public long getCollectionUpdateCount() {
        return stats.getCollectionUpdateCount();
    }

    @ManagedAttribute
    public long getCollectionRemoveCount() {
        return stats.getCollectionRemoveCount();
    }

    @ManagedAttribute
    public long getCollectionRecreateCount() {
        return stats.getCollectionRecreateCount();
    }

    @ManagedAttribute
    public long getStartTime() {
        return stats.getStartTime();
    }

    @ManagedAttribute
    public boolean isStatisticsEnabled() {
        return stats.isStatisticsEnabled();
    }

    @ManagedAttribute
    public String[] getEntityNames() {
        return stats.getEntityNames();
    }

    @ManagedAttribute
    public String[] getQueries() {
        return stats.getQueries();
    }

    @ManagedAttribute
    public long getSuccessfulTransactionCount() {
        return stats.getSuccessfulTransactionCount();
    }
    @ManagedAttribute
    public long getTransactionCount() {
        return stats.getTransactionCount();
    }

    @ManagedAttribute
    public long getPrepareStatementCount(){
        return stats.getPrepareStatementCount();
    }

    @ManagedAttribute
    public String getQueryExecutionMaxTimeQueryString() {
        return stats.getQueryExecutionMaxTimeQueryString();
    }

}
