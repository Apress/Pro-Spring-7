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
package com.apress.prospring7.boot.nineteen.albums;

import com.apress.prospring7.boot.nineteen.NotFoundException;
import com.apress.prospring7.boot.nineteen.singers.NewSingerAddedEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static jakarta.persistence.GenerationType.IDENTITY;

///
/// @author iulianacosmina on 14/03/2026
///
@RestController
@RequestMapping("/albums")
public class AlbumsController {
    private AlbumService albumService;

    public AlbumsController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping(path = "/{singerId}")
    List<Album> findAllPerSinger(@PathVariable Long singerId){
        return albumService.findAllPerSinger(singerId);
    }

    @GetMapping(path = "/byId/{albumId}")
    Album getById(@PathVariable Long albumId){
        return albumService.findById(albumId);
    }

    @PostMapping
    void create(@RequestBody Album album){
        albumService.save(album);
    }
}

interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllBySingerId(Long singerId);
}

@Transactional
@Service
class AlbumService {
    private final AlbumRepository albumRepository;

    AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    List<Album> findAllPerSinger(final Long singerId) {
        return albumRepository.findAllBySingerId(singerId);
    }

    Album findById(Long id) {
        return albumRepository.findById(id).orElseThrow(() -> new NotFoundException(Album.class, id));
    }

    Album save(Album album) {
        return albumRepository.save(album);
    }

    void delete(Long id) {
        Optional<Album> fromDb = albumRepository.findById(id);
        if (fromDb.isEmpty())  {
            throw new NotFoundException(Album.class, id);
        }
        albumRepository.deleteById(id);
    }
}

@Entity
@Table(name = "ALBUM")
class Album {
    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @JsonProperty
    @Column
    private String title;

    @JsonProperty
    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @JsonProperty
    @Column(name = "SINGER_ID")
    private Long singerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSingerId() {
        return singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        if(this.id != null) {
            return this.id.equals(((Album) o).id);
        }
        return title.equals(album.title) && releaseDate.equals(album.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, releaseDate);
    }

    @Override
    public String toString() {
        return "Album - Id: " + id + ", Singer id: " + singerId
                + ", Title: " + title + ", Release Date: " + releaseDate;
    }
}

@Service
@Transactional
class AlbumPopulator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumPopulator.class);
    private final AlbumRepository albumRepository;

    public AlbumPopulator(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Async
    @EventListener
    void on(NewSingerAddedEvent event) throws InterruptedException, IOException {
        Thread.sleep(Duration.ofMillis(5_000)); // wait about 5 seconds before loading the albums and saving them
        final var singerId =  (Long) event.getSource();
        LOGGER.info("-- Loading albums for singer with id {} ", singerId);
        final var input = new ClassPathResource("album-data/nick-drake.json");
        final var mapper = new JsonMapper();
        var albums = mapper.readValue(input.getInputStream(), Album[].class);
        Arrays.asList(albums).stream().forEach(
                album -> {
                    album.setSingerId(singerId);
                    var saved = albumRepository.save(album);
                    LOGGER.info("   >> Saved album with id {} for singer with id {} ", saved.id, singerId);
                }
        );
        // could have used this, but singerId needed to be set on each,also we get nice lgging
        //albumRepository.saveAll(Arrays.asList(albums));
    }
}
