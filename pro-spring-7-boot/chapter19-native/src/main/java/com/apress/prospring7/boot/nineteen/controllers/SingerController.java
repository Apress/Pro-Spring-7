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
package com.apress.prospring7.boot.nineteen.controllers;

import com.apress.prospring7.boot.nineteen.entities.Singer;
import com.apress.prospring7.boot.nineteen.repos.SingerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

///
/// @author iulianacosmina on 01/02/2026
///
@RestController
@RequestMapping(value="/singer")
public class SingerController {

    final Logger LOGGER = LoggerFactory.getLogger(SingerController.class);

    private final SingerRepo singerRepo;

    public SingerController(SingerRepo singerRepo) {
        this.singerRepo = singerRepo;
    }

    @GetMapping(path={"/", ""})
    public List<Singer> all() {
        return singerRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public Singer findSingerById(@PathVariable Long id) {
        return singerRepo.findById(id);
    }

    @PostMapping(path="/")
    public Singer  create(@RequestBody Singer singer) {
        LOGGER.info("Creating singer: {}" , singer);
        return singerRepo.save(singer);
    }

    @PutMapping(value="/{id}")
    public Singer update(@RequestBody Singer singer, @PathVariable Long id) {
        LOGGER.info("Updating singer: {}" , singer);
        return singerRepo.update(id, singer);
    }

    @DeleteMapping(value="/{id}")
    public void delete(@PathVariable Long id) {
        LOGGER.info("Deleting singer with id: {}" , id);
        singerRepo.delete(id);
    }
}
