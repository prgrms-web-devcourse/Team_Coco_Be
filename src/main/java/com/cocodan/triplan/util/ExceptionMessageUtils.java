package com.cocodan.triplan.util;

import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

public class ExceptionMessageUtils {

    private static MessageSourceAccessor messageSourceAccessor;

    public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        ExceptionMessageUtils.messageSourceAccessor = messageSourceAccessor;
    }

    public static String getMessage(String code){
        return getMessage(code, null);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, null);
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        return messageSourceAccessor.getMessage(code, args, locale);
    }
}
