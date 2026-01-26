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
package com.apress.prospring7.boot.thirteen.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

///
/// @author iulianacosmina on 18/01/2026
///
public class CookieParamLocaleResolver implements LocaleContextResolver {

    public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = "Bookstore.Cookie.LOCALE";

    private String languageParameterName;

    public CookieParamLocaleResolver(final String languageParameterName) {
        this.languageParameterName = languageParameterName;
    }

    @Override
    public LocaleContext resolveLocaleContext(final ServerWebExchange exchange) {
        Locale defaultLocale = getLocaleFromCookie(exchange);
        List<String> referLang = exchange.getRequest().getQueryParams().get(languageParameterName);
        if (!CollectionUtils.isEmpty(referLang) ) {
            String lang = referLang.get(0);
            defaultLocale = Locale.forLanguageTag(lang);
            setLocaleToCookie(lang, exchange);
        }
        return new SimpleLocaleContext(defaultLocale);
    }

    private void setLocaleToCookie(final String languageValue, final ServerWebExchange exchange) {
        MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
        HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
        if(langCookie == null || !languageValue.equals(langCookie.getValue())) {
            ResponseCookie cookie = ResponseCookie.from(LOCALE_REQUEST_ATTRIBUTE_NAME, languageValue).maxAge(Duration.ofMinutes(5)).build();
            exchange.getResponse().addCookie(cookie);
        }
    }

    private Locale getLocaleFromCookie(final ServerWebExchange exchange){
        MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
        HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
        return langCookie != null ? Locale.forLanguageTag(langCookie.getValue()) : Locale.getDefault();
    }

    @Override
    public void setLocaleContext(final ServerWebExchange exchange, final LocaleContext localeContext) {
        throw new UnsupportedOperationException("Not Supported");
    }

}
