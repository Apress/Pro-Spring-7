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
package com.apress.prospring7.classic.fourteen.services;

import com.apress.prospring7.classic.fourteen.entities.Singer;
import com.apress.prospring7.classic.fourteen.ex.NotFoundException;
import com.apress.prospring7.classic.fourteen.repos.SingerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

///
/// @author iulianacosmina on 29/01/2026
///
@Transactional
@Service("singerService")
public class SingerServiceImpl implements SingerService {

    private final SingerRepo singerRepo;

    public SingerServiceImpl(SingerRepo singerRepo) {
        this.singerRepo = singerRepo;
    }

    @Override
    public List<Singer> findAll() {
        var singers = singerRepo.findAll();
        if(singers.isEmpty()) {
            throw new NotFoundException(Singer.class);
        }
        return singerRepo.findAll();
    }

    @Override
    public Singer findById(Long id) {
        return singerRepo.findById(id).orElseThrow(() -> new NotFoundException(Singer.class, id));
    }

    @Override
    public Singer save(Singer singer) {
        return singerRepo.save(singer);
    }

    @Override
    public Singer update(Long id, Singer singer) {
        return  singerRepo.findById(id)
                .map(s -> singerRepo.save(new Singer(id, s.getFirstName(), s.getLastName(), s.getBirthDate())))
                .orElseThrow(() -> new NotFoundException(Singer.class, id));
    }

    @Override
    public void delete(Long id) {
        Optional<Singer> fromDb = singerRepo.findById(id);
        if (fromDb.isEmpty())  {
            throw new NotFoundException(Singer.class, id);
        }
        singerRepo.deleteById(id);
    }
}
