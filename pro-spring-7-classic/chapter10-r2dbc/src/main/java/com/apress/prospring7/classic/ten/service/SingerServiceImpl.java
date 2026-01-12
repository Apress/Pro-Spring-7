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
package com.apress.prospring7.classic.ten.service;

import com.apress.prospring7.classic.ten.document.Singer;
import com.apress.prospring7.classic.ten.repos.SingerRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

///
/// @author iulianacosmina on 07/01/2026
///
@Service
@Transactional
public class SingerServiceImpl implements SingerService {
 final SingerRepository singerRepository;

 public SingerServiceImpl(SingerRepository singerRepository) {
  this.singerRepository = singerRepository;
 }

 @Override
 public Mono<Long> count() {
   return singerRepository.count();
 }

 @Override
 public Mono<Singer> save(Singer singer) {
  return singerRepository.save(singer);
 }

 @Override
 public Mono<Singer> findByFullName(@NonNull String firstName, String lastName) {
  return singerRepository.findByFullName(firstName, lastName);
 }

 @Override
 public Flux<Singer> findByFirstName(@NonNull String firstName) {
  return singerRepository.findByFirstName(firstName);
 }

 @Override
 public Flux<Singer> findByLastName(@NonNull String lastName) {
   return singerRepository.findByLastName(lastName);
 }

 @Override
 public Flux<Singer> findByBirthDate(@NonNull LocalDate birthDate) {
  return singerRepository.findByBirthDate(birthDate);
 }

 @Override
 public Mono<Void> deleteById(Long id) {
  return singerRepository.deleteById(id);
 }
}
