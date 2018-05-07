package com.tm.api.common.localization;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final Locale DEFAULT_LOCALE = Locale.US;
    private static final List<Locale> LOCALES = Arrays.asList(Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.ITALY);

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguageHeader = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (StringUtils.isEmpty(acceptLanguageHeader)) {
            return DEFAULT_LOCALE;
        }

        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(acceptLanguageHeader);
        Locale bestMatchingLocale = Locale.lookup(list, LOCALES);

        return bestMatchingLocale != null ? bestMatchingLocale : DEFAULT_LOCALE;
    }
}
