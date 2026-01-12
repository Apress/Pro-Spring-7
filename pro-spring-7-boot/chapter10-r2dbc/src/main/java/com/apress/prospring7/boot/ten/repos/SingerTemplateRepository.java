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
package com.apress.prospring7.boot.ten.repos;

import com.apress.prospring7.boot.ten.document.Singer;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

///
/// @author iulianacosmina on 29/12/2025
///

@Component
public class SingerTemplateRepository {

    public final R2dbcEntityTemplate template;

    public SingerTemplateRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Flux<Singer> findByFullName(String firstName, String lastName) {
        return this.template.select(Singer.class)
                .matching(Query.query(
                                where("first_name").is(firstName)
                                        .and(
                                                where("last_name").is(lastName)
                                        )
                        )
                        .limit(10).offset(0))
                .all();
    }

    public Flux<Singer> findAll() {
        return this.template.select(Singer.class).all();
    }

    public Mono<Long> count() {
        return this.template.count(Query.empty(), Singer.class);
    }

    public Mono<Singer> findById(Long id) {
        return this.template.selectOne(Query.query(where("id").is(id)), Singer.class);
    }

    public Mono<Long> save(Singer s) {
        return this.template.insert(Singer.class)
                .using(s)
                .map(Singer::id);
    }

    public Mono<Long> update(Singer p) {
        return this.template.update(
                Query.query(where("id").is(p.id())),
                Update.update("first_name", p.firstName())
                        .set("last_name", p.lastName())
                        .set("birth_date", p.birthDate()),
                Singer.class);
    }

    // returning Id of the deleted entity
    public Mono<Void> deleteById(Long id) {
        return this.template.delete(Query.query(where("id").is(id)), Singer.class).then();
    }
}
