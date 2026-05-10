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
package com.apress.prospring7.classic.nine.services;

import com.apress.prospring7.classic.nine.entities.Singer;
import com.apress.prospring7.classic.nine.repos.SingerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

///
/// @author iulianacosmina on 03/12/2025
///
@Service
@Transactional
public class SingerServiceImpl implements SingerService {

    private final SingerRepository singerRepository;

    public SingerServiceImpl(SingerRepository singerRepository) {
        this.singerRepository = singerRepository;
    }

    @Override
    @Transactional(readOnly=true)
    public Stream<Singer> findAll() {
        return StreamSupport.stream(singerRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional(readOnly=true)
    public Stream<Singer> findByFirstName(String firstName) {
        return singerRepository.findByFirstName(firstName).stream();
    }

    @Override
    @Transactional(readOnly=true)
    public Stream<Singer> findByFirstNameAndLastName(String firstName, String lastName) {
        return singerRepository.findByFirstNameAndLastName(firstName, lastName).stream();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, label = "modifying")
    @Override
    public Singer updateFirstName(String firstName, Long id) {
        singerRepository.findById(id).ifPresent(_ -> singerRepository.setFirstNameFor(firstName, id));
        return  singerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    @Override
    public Stream<SingerRepository.FullName> findByLastName(String lastName) {
        return singerRepository.findByLastName(lastName).stream();
    }

    @Transactional(readOnly=true)
    @Override
    public String getFirstNameByIdProc(Long id) {
        return singerRepository.getFirstNameByIdProc(id);
    }


    @Override
    public int deleteFirstName(String firstName) {
        return singerRepository.deleteByFirstName(firstName);
    }
}
