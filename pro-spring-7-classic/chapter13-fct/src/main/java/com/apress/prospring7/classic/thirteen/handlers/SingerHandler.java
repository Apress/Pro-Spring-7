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
package com.apress.prospring7.classic.thirteen.handlers;

import com.apress.prospring7.classic.thirteen.entities.Singer;
import com.apress.prospring7.classic.thirteen.ex.InvalidCriteriaException;
import com.apress.prospring7.classic.thirteen.services.SingerService;
import com.apress.prospring7.classic.thirteen.util.CriteriaDto;
import jakarta.servlet.ServletException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


///
/// @author iulianacosmina on 23/01/2026
///
@Component
public class SingerHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(SingerHandler.class);

    private final SingerService singerService;
    private final MessageSource messageSource;
    private final Validator validator;

    public final HandlerFunction<ServerResponse> list;
    public final HandlerFunction<ServerResponse> findById;
    public final HandlerFunction<ServerResponse> searchForm;
    public final HandlerFunction<ServerResponse> createForm;
    public final HandlerFunction<ServerResponse> editForm;
    public final HandlerFunction<ServerResponse> uploadPhotoForm;
    public final HandlerFunction<ServerResponse> deleteById;

    public SingerHandler(SingerService singerService, MessageSource messageSource, Validator validator) {
        this.singerService = singerService;
        this.messageSource = messageSource;
        this.validator = validator;

        /* 1 */
        list = _ -> ServerResponse
                .ok()
                .render("singers/list", // view
                        Map.of("singers", singerService.findAll())); // attributes

        /* 2 */
        findById = serverRequest -> ServerResponse
                .ok()
                .render("singers/show", // view
                        Map.of("singer", singerService.findById(Long.valueOf(serverRequest.pathVariable("id"))))); // attributes

        /* 3 */
        searchForm = _ -> ServerResponse
                .ok()
                .render("singers/search", new CriteriaDto());

        /* 4 */
        createForm = _ -> ServerResponse
                .ok()
                .render("singers/create", new Singer());

        /* 5 */
        editForm = serverRequest -> ServerResponse
                .ok()
                .render("singers/edit",
                        Map.of("singer", singerService.findById(Long.valueOf(serverRequest.pathVariable("id")))));

        /* 6 */
        uploadPhotoForm = serverRequest -> ServerResponse
                .ok()
                .render("singers/upload",
                        Map.of("singer", singerService.findById(Long.valueOf(serverRequest.pathVariable("id")))));

        /* 7 */
        deleteById = serverRequest -> {
            singerService.delete(Long.valueOf(serverRequest.pathVariable("id")));
            return ServerResponse
                    .ok()
                    .render("singers/list", // view
                            Map.of("singers", singerService.findAll())); // attributes
        };
    }

    public ServerResponse go(ServerRequest serverRequest) {
        var criteriaDto = (CriteriaDto) serverRequest.attribute("criteriaDto").orElseGet(CriteriaDto::new); // just use empty object

        var result = new BeanPropertyBindingResult(criteriaDto, "criteriaDto");
        validator.validate(criteriaDto, result);

        if (result.hasErrors())
            throw new ServerWebInputException(result.getAllErrors().toString());
            //return ServerResponse.ok().render("singers/search", result.getAllErrors(),  Map.of("criteriaDto", criteriaDto));
        try {
            List<Singer> singers = singerService.getByCriteriaDto(criteriaDto);
            if (singers.isEmpty()) {
                result.addError(new FieldError("criteriaDto", "noResults",
                        messageSource.getMessage("NotEmpty.criteriaDto.noResults", null, LocaleContextHolder.getLocale())));

                throw new ServerWebInputException(result.getAllErrors().toString());
                //return ServerResponse.ok().render("singers/search", Map.of("criteriaDto", criteriaDto));
            } else if (singers.size() == 1) {
                return ServerResponse.ok().render("singers/show", Map.of("singer", singers.getFirst()));
            } else {
                return ServerResponse.ok().render("singers/list", Map.of("singers", singers));
            }

        } catch (InvalidCriteriaException ice) {
            result.addError(new FieldError("criteriaDto", ice.getFieldName(),
                    messageSource.getMessage(ice.getMessageKey(), null, LocaleContextHolder.getLocale())));

            throw new ServerWebInputException(result.getAllErrors().toString());
            //return backToSearch;
        }
    }

    public ServerResponse create(ServerRequest serverRequest){
        var createData = (Singer) serverRequest.attribute("singer").orElseGet(Singer::new); // just use empty object

        //var backToCreate = ServerResponse.ok().render("singers/create", Map.of("singerForm", createData));
        var result = new BeanPropertyBindingResult(createData, "singer");

        validator.validate(createData, result);
        if (result.hasErrors()) {
            throw new ServerWebInputException(result.getAllErrors().toString());
            //return backToCreate; // without any errors, because binding does not work
        }

        var s = new Singer();
        s.setFirstName(createData.getFirstName());
        s.setLastName(createData.getLastName());
        s.setBirthDate(createData.getBirthDate());
        // Process file  upload
        if (createData.getPhoto() != null ) {
            s.setPhoto(createData.getPhoto());
        }
        var created = singerService.save(s);

        return ServerResponse.ok().render("singers/show", Map.of("singer", created));
    }

    public ServerResponse downloadPhoto(ServerRequest serverRequest){
       var id = Long.valueOf(serverRequest.pathVariable("id"));
       var singer = singerService.findById(id);
        if (singer.getPhoto() != null) {
            LOGGER.info("Downloading photo for id: {} with size: {}", singer.getId(), singer.getPhoto().length);

            return ServerResponse.ok().body(singer.getPhoto());
        }
        return ServerResponse.noContent().build();
    }

    public ServerResponse uploadPhoto(ServerRequest serverRequest) {
        var id = Long.valueOf(serverRequest.pathVariable("id"));
        var singer = singerService.findById(id);

        try {
            var input = Objects.requireNonNull(serverRequest.multipartData().getFirst("file")).getInputStream();
            var fileContent = IOUtils.toByteArray(input);
            singer.setPhoto(fileContent);
            singerService.save(singer);
        } catch (IOException | ServletException e) {
            throw new ServerWebInputException("Photo upload failed!");
        }
        return ServerResponse.ok().render("singers/show", Map.of("singer", singer));
    }

    public ServerResponse edit(ServerRequest serverRequest){
        var updateData = (Singer) serverRequest.attribute("singer").orElseGet(Singer::new); // just use empty object

        //var backToUpdate = ServerResponse.ok().render("singers/{id}/edit", Map.of("singer", updateData));
        var result = new BeanPropertyBindingResult(updateData, "singer");

        validator.validate(updateData, result);
        if (result.hasErrors()) {
            throw new ServerWebInputException(result.getAllErrors().toString());
            //return backToUpdate; // without any errors, because binding does not work with functional endpoints
        }

        var fromDb = singerService.findById(Long.valueOf(serverRequest.pathVariable("id")));
        fromDb.setFirstName(updateData.getFirstName());
        fromDb.setLastName(updateData.getLastName());
        fromDb. setBirthDate(updateData.getBirthDate());
        var created = singerService.save(fromDb);

        return ServerResponse.ok().render("singers/show", Map.of("singer", created));
    }

}
