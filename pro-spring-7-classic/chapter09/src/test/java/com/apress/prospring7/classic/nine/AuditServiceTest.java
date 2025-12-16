/*
Freeware License, some rights reserved

Copyright (c) 2025 Iuliana Cosmina

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
package com.apress.prospring7.classic.nine;

import com.apress.prospring7.classic.nine.config.AuditCfg;
import com.apress.prospring7.classic.nine.config.EnversCfg;
import com.apress.prospring7.classic.nine.entities.SingerAudit;
import com.apress.prospring7.classic.nine.audit.services.SingerAuditService;
import org.hibernate.cfg.Environment;
import org.hibernate.envers.configuration.EnversSettings;
import org.hibernate.envers.strategy.internal.ValidityAuditStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author iulianacosmina on 06/12/2025
 */
@Testcontainers
@Sql({ "classpath:testcontainers/audit/drop-schema.sql", "classpath:testcontainers/audit/create-schema.sql" })
@SpringJUnitConfig(classes = {AuditServiceTest.TestContainersConfig.class})
public class AuditServiceTest extends TestContainersBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceTest.class);

    @Autowired
    SingerAuditService auditService;

    @BeforeEach
    void setUp(){
        var singer = new SingerAudit();
        singer.setFirstName("BB");
        singer.setLastName("King");
        singer.setBirthDate(LocalDate.of(1940, 8, 16));
        auditService.save(singer);
    }

    @Test
    void testFindById() {
        var singer = auditService.findAll().findFirst().orElse(null);

        assertAll( "auditFindByIdTest" ,
                () -> assertNotNull(singer),
                () -> assertTrue(singer.getCreatedBy().isPresent()),
                () -> assertTrue(singer.getLastModifiedBy().isPresent()),
                () -> assertNotNull(singer.getCreatedDate()),
                () -> assertNotNull(singer.getLastModifiedDate())
        );
        LOGGER.info(">> created record: {} ", singer);
    }

    @Test
    void testUpdate() {
        var singer = auditService.findAll().findFirst().orElse(null);
        assertNotNull(singer);
        singer.setFirstName("Riley B.");
        var updated = auditService.save(singer);

        assertAll( "auditUpdateTest" ,
                () -> assertEquals("Riley B.", updated.getFirstName()),
                () -> assertTrue(updated.getLastModifiedBy().isPresent()),
                () -> assertNotEquals(updated.getCreatedBy().orElse(null), updated.getLastModifiedBy().orElse(null))
        );
        LOGGER.info(">> updated record: {} ", updated);
    }

    @Test
    void testFindAuditByRevision() {
        // update to create new version
        var singer = auditService.findAll().findFirst().orElse(null);
        assertNotNull(singer);
        singer.setFirstName("Riley B.");
        auditService.save(singer);

        var oldSinger = auditService.findAuditByRevision(singer.getId(), 1);
        assertEquals("BB", oldSinger.getFirstName());
        LOGGER.info(">> old singer: {} ", oldSinger);

        var newSinger = auditService.findAuditByRevision(singer.getId(), 2);
        assertEquals("Riley B.", newSinger.getFirstName());
        LOGGER.info(">> updated singer: {} ", newSinger);
    }

    @Test
    void testFindAuditAfterDeletion() {
        // delete record
        var singer = auditService.findAll().findFirst().orElse(null);
        auditService.delete(singer.getId());

        // extract from audit
        var deletedSinger = auditService.findAuditByRevision(singer.getId(), 1);
        assertEquals("BB", deletedSinger.getFirstName());
        LOGGER.info(">> deleted singer: {} ", deletedSinger);
    }

    @Test
    void testFindAllRevisions() {
        // update to create new version
        var singer = auditService.findAll().findFirst().orElse(null);
        assertNotNull(singer);
        singer.setFirstName("Riley B.");
        auditService.save(singer);
        auditService.delete(singer.getId());

       var revisions = auditService.findAllRevisions(singer.getId());
        LOGGER.info(">> all revisions: {} ", revisions);
       assert(revisions.stream().anyMatch(r -> r.getMetadata().getRevisionType() == RevisionMetadata.RevisionType.INSERT));
       assert(revisions.stream().anyMatch(r -> r.getMetadata().getRevisionType() == RevisionMetadata.RevisionType.UPDATE));
       assert(revisions.stream().anyMatch(r -> r.getMetadata().getRevisionType() == RevisionMetadata.RevisionType.DELETE));
    }


    @Configuration
    //@Import({DataJpaCfg.class, AuditCfg.class})
    @Import({EnversCfg.class, AuditCfg.class})
    public static class TestContainersConfig {
        @Primary
        @Bean
        public Properties jpaProperties() {
            final var jpaProps = new Properties();
            jpaProps.put(Environment.FORMAT_SQL, true);
            jpaProps.put(Environment.USE_SQL_COMMENTS, false);
            jpaProps.put(Environment.SHOW_SQL, true);
            jpaProps.put(Environment.HIGHLIGHT_SQL, true);
            jpaProps.put(Environment.STATEMENT_BATCH_SIZE, 30);

            // needed here once Envers is on the classpath
            //Properties for Hibernate Envers -- needed  because we customized table and field names
            jpaProps.put(EnversSettings.AUDIT_TABLE_SUFFIX, "_H");
            jpaProps.put(EnversSettings.REVISION_FIELD_NAME, "AUDIT_REVISION");
            jpaProps.put(EnversSettings.REVISION_TYPE_FIELD_NAME, "ACTION_TYPE");
            jpaProps.put(EnversSettings.AUDIT_STRATEGY, ValidityAuditStrategy.class.getName());
            jpaProps.put(EnversSettings.AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME, "AUDIT_REVISION_END");
            jpaProps.put(EnversSettings.AUDIT_STRATEGY_VALIDITY_STORE_REVEND_TIMESTAMP, "true");
            jpaProps.put(EnversSettings.AUDIT_STRATEGY_VALIDITY_REVEND_TIMESTAMP_FIELD_NAME, "AUDIT_REVISION_END_TS");
            return jpaProps;
        }
    }
}
