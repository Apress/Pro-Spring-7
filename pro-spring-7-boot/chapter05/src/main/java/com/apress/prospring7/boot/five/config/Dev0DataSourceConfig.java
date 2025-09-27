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
package com.apress.prospring7.boot.five.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * @author iulianacosmina on 21/09/2025
 */
@Configuration(proxyBeanMethods = false)
@Profile("dev0")
class Dev0DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dev0DataSourceConfig.class);

    @Bean
    @ConfigurationProperties("app.datasource0")
    ConnectionProperties connectionProperties() {
        return new ConnectionProperties();
    };

    @Bean(destroyMethod = "close")
    DataSource dataSource(ConnectionProperties connectionProperties) {
        try {
            final var dataSource =  new HikariDataSource();
            dataSource.setDriverClassName(connectionProperties.getDriverClassName());
            dataSource.setJdbcUrl(connectionProperties.getUrl());
            dataSource.setUsername(connectionProperties.getUsername());
            dataSource.setPassword(connectionProperties.getPassword());
            dataSource.setMaximumPoolSize(connectionProperties.getPoolSize());
            LOGGER.debug(" [DEV0] >> Using explicitly configured HikariCP DataSource using custom class `ConnectionProperties`.");
            return dataSource;
        } catch (Exception e) {
            LOGGER.error("HikariCP DataSource bean cannot be created!", e);
            return null;
        }
    }
}

class ConnectionProperties {
    String driverClassName;
    String url;
    String username;
    String password;
    Integer poolSize;

    ConnectionProperties() {
    }

    ConnectionProperties(String driverClassName, String url, String username,
                                String password, Integer poolSize) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.poolSize = poolSize;
    }

    String getDriverClassName() {
        return driverClassName;
    }

   void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    Integer getPoolSize() {
        return poolSize;
    }

    void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }
}
