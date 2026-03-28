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
package com.apress.prospring7.boot.nineteen.repos;

import com.apress.prospring7.boot.nineteen.entities.Singer;
import com.apress.prospring7.boot.nineteen.ex.DuplicateRecordException;
import com.apress.prospring7.boot.nineteen.ex.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

///
/// @author iulianacosmina on 01/02/2026
///
@Component
public class SingerRepo {
    private Map<Long, Singer> data = Map.of(
            1L, new Singer("John", "Mayer", "Sob Rock"),
            2L, new Singer("Nick", "Drake", "Pink Moon "),
            3L, new Singer("Nina", "Simone", "A Single Woman"),
            4L, new Singer("Ben", "Barnes", "Where the Light Gets In")
    );

    public List<Singer> findAll() {
        return List.copyOf(data.values());
    }

    public Singer findById(Long id) {
        if(!data.containsKey(id)) throw new NotFoundException(Singer.class, id);
        return data.get(id);
    }

    public Singer save(Singer singer) {
        var res = data.values().stream().filter(s -> s.firstName().equals(singer.firstName()) && s.lastName().equals(singer.lastName())).findAny();
        if (res.isPresent()) throw  new DuplicateRecordException("Duplicate entry '" + singer.firstName() + " " + singer.lastName() + "'" );
        data.put((long) (data.size() + 1), singer);
        return singer;
    }

    public Singer update(Long id, Singer singer) {
        if(!data.containsKey(id)) throw new NotFoundException(Singer.class, id);
        data.put(id, singer);
         return singer;
    }

    public void delete(Long id) {
        if(!data.containsKey(id)) throw new NotFoundException(Singer.class, id);
        data.remove(id);
    }
}
