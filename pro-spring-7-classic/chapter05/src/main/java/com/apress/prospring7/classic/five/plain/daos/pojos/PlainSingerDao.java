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
package com.apress.prospring7.classic.five.plain.daos.pojos;

import com.apress.prospring7.classic.five.NotImplementedException;
import com.apress.prospring7.classic.five.plain.pojos.Singer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static com.apress.prospring7.classic.five.QueryConstants.*;

/**
 * @author iulianacosmina on 02/09/2025
 */
public class PlainSingerDao implements SingerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainSingerDao.class);

    @Override
    public Set<Singer> findAll() {
        final Set<Singer> result = new HashSet<>();
        try (final var connection = getConnection();
             final var statement = connection.prepareStatement(ALL_SELECT);
             final var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var singer = new Singer();
                singer.setId(resultSet.getLong("id"));
                singer.setFirstName(resultSet.getString("first_name"));
                singer.setLastName(resultSet.getString("last_name"));
                singer.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                result.add(singer);
            }
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!",ex);
        }
        return result;
    }

    @Override
    public Set<Singer> findByFirstName(String firstName) {
        throw new NotImplementedException("findByFirstName");
    }

    @Override
    public String findNameById(Long id) {
        throw new NotImplementedException("findNameById");
    }

    @Override
    public String findLastNameById(Long id) {
        throw new NotImplementedException("findLastNameById");
    }

    @Override
    public String findFirstNameById(Long id) {
        throw new NotImplementedException("findFirstNameById");
    }

    @Override
    public Singer insert(Singer singer) {
        try (final var connection = getConnection()){
            final var statement = connection.prepareStatement(SIMPLE_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, singer.getFirstName());
            statement.setString(2, singer.getLastName());
            statement.setDate(3, java.sql.Date.valueOf(singer.getBirthDate()));
            statement.execute();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                singer.setId(generatedKeys.getLong(1));
            }
            return singer;
        } catch (SQLException ex) {
            LOGGER.error("Problem executing INSERT", ex);
        }
        return null;
    }

    @Override
    public void update(Singer singer) {
        throw new NotImplementedException("update");
    }

    @Override
    public void delete(Long singerId) {
        try (final var connection = getConnection();
            final var statement = connection.prepareStatement(SIMPLE_DELETE)) {
            statement.setLong(1, singerId);
            statement.execute();
        } catch (SQLException ex) {
            LOGGER.error("Problem executing DELETE", ex);
        }
    }

    @Override
    public Set<Singer> findAllWithAlbums() {
        throw new NotImplementedException("findAllWithAlbums");
    }

    @Override
    public void insertWithAlbum(Singer singer) {
        throw new NotImplementedException("insertWithAlbum");
    }
}
