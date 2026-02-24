package com.example.application.converter;

import com.example.application.data.EmailAddress;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class EmailAddressConverter implements Converter<String, EmailAddress> {

    @Override
    public Result<EmailAddress> convertToModel(String s, ValueContext valueContext) {
        if (s == null || s.isEmpty()) {
            return Result.ok(null);
        } else {
            try {
                return Result.ok(new EmailAddress(s));
            } catch (IllegalArgumentException e) {
                return Result.error(e.getMessage());
            }
        }
    }

    @Override
    public String convertToPresentation(EmailAddress emailAddress, ValueContext valueContext) {
        return emailAddress == null ? "" : emailAddress.value();
    }
}
