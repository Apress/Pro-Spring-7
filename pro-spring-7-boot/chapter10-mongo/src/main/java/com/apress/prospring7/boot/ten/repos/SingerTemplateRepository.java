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
package com.apress.prospring7.boot.ten.repos;

import com.apress.prospring7.boot.ten.document.Singer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;

///
/// @author iulianacosmina on 03/01/2026
///
@Repository
public class SingerTemplateRepository {

    public final ReactiveMongoTemplate template;

    public SingerTemplateRepository(ReactiveMongoTemplate template) {
        this.template = template;
    }

    public Mono<Long> count() {
        return this.template.count(new Query(), Singer.class);
    }

    public Flux<Singer> findByFullName(String firstName, String lastName) {
        return this.template.find(
                Query.query(
                        where("firstName").is(firstName).and("lastName").is(lastName)

        ) , Singer.class);
    }

    public Flux<Singer> findAll() {
        return this.template.findAll(Singer.class);
    }

    public Mono<Singer> findById(Long id) {
        return this.template.findById(id, Singer.class);
    }

    public Mono<String> save(Singer s) {
        return this.template.insert(Singer.class).one(s)
                .map(Singer::id);
    }

    public Mono<Singer> update(Singer newSinger) {
        return this.template
                .findAndReplace(Query.query(where("id").is(newSinger.id())), newSinger);
    }

    // returning Id of the deleted entity
    public Mono<String> deleteById(String id) {
        return this.template.findAndRemove(Query.query(where("id").is(id)), Singer.class)
                .map(Singer::id);
    }
}
