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
package com.apress.prospring7.classic.five.client;

import com.apress.prospring7.classic.five.config.C3p0DataSourceCfg;
import com.apress.prospring7.classic.five.plain.records.Singer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

import java.util.List;
import java.util.Set;

import static com.apress.prospring7.classic.five.QueryConstants.ALL_SELECT;
import static com.apress.prospring7.classic.five.QueryConstants.FIND_BY_FIRST_NAME;
import static com.apress.prospring7.classic.five.QueryConstants.PARAMETRIZED_FIND_NAME;

///
/// @author iulianacosmina on 02/11/2025
///

interface SingerDao {
    String findNameById(Long id);
    Singer findByFirstName(String firstName);
    List<Singer> findAll();
}

class JdbcSingerDao implements SingerDao {
    private JdbcClient jdbcClient;

    public void setJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public String findNameById(Long id) {
        return jdbcClient
                .sql(PARAMETRIZED_FIND_NAME)
                .param(id)
                .query(String.class)
                .single();
    }

    @Override
    public Singer findByFirstName(String firstName) {
        return jdbcClient
                .sql(FIND_BY_FIRST_NAME)
                .param("first_name", firstName)
                .query(Singer.class)
                .single();
    }

    @Override
    public List<Singer> findAll() {
        return jdbcClient.sql(ALL_SELECT)
                .query((rs, rowNum) -> new Singer(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        Set.of()))
                .list();
    }
}

@Import(C3p0DataSourceCfg.class)
@Configuration
class SpringJdbcClientCfg {
    @Autowired
    DataSource dataSource;

    @Bean
    JdbcClient jdbcClient(){
        return JdbcClient.create(dataSource);
    }

    @Bean
    SingerDao singerDao() {
        final var dao = new JdbcSingerDao();
        dao.setJdbcClient(jdbcClient());
        return dao;
    }

}
