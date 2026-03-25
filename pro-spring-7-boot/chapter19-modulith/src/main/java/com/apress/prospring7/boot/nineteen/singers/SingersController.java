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
package com.apress.prospring7.boot.nineteen.singers;

import com.apress.prospring7.boot.nineteen.NotFoundException;
//import com.apress.prospring7.boot.nineteen.albums.validation.AlbumValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.GenerationType.IDENTITY;

///
/// @author iulianacosmina on 14/03/2026
///
@RestController
@RequestMapping("/singers")
public class SingersController {

/*    @Autowired
    AlbumValidation validation;*/

    private final SingerService singerService;

    SingersController(SingerService singerService) {
        this.singerService = singerService;
    }

    @GetMapping
   public List<Singer> getAll(){
        return singerService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createSinger(@RequestBody  Singer singer){
        singerService.save(singer);
    }

    @GetMapping(path = "/{id}")
    Singer findAllPerSinger(@PathVariable Long id){
        return singerService.findById(id);
    }
}

interface SingerRepository extends JpaRepository<Singer, Long> { }

@Transactional
@Service
class SingerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingerService.class);

    private final ApplicationEventPublisher events;
    private final SingerRepository singerRepo;

    SingerService(ApplicationEventPublisher events, SingerRepository singerRepository) {
        this.events = events;
        this.singerRepo = singerRepository;
    }

    List<Singer> findAll() {
        return singerRepo.findAll();
    }

    Singer findById(Long id) {
        return singerRepo.findById(id).orElseThrow(() -> new NotFoundException(Singer.class, id));
    }

    Singer save(Singer singer) {
        final var saved =  singerRepo.save(singer);
        LOGGER.info("--- Saved singer {} {} with id  {}" , saved.getFirstName(), saved.getLastName(), saved.getId());
        events.publishEvent(new NewSingerAddedEvent(saved.id));
        return saved;
    }

     void delete(Long id) {
         Optional<Singer> fromDb = singerRepo.findById(id);
         if (fromDb.isEmpty())  {
             throw new NotFoundException(Singer.class, id);
         }
         singerRepo.deleteById(id);
    }
}

@Entity
@Table(name = "SINGER")
class Singer implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id // @JsonProperty
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @JsonProperty
    @Column(name = "FIRST_NAME")
    private String firstName;

    @JsonProperty
    @Column(name = "LAST_NAME")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

     Long getId() {
        return id;
    }

     void setId(Long id) {
        this.id = id;
    }

     String getFirstName() {
        return firstName;
    }

     void setFirstName(String firstName) {
        this.firstName = firstName;
    }

     String getLastName() {
        return lastName;
    }

     void setLastName(String lastName) {
        this.lastName = lastName;
    }

     LocalDate getBirthDate() {
        return birthDate;
    }

     void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
