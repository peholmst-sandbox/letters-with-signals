package com.example.application.view;

import com.example.application.model.LetterModel;
import com.example.application.util.DateTimeUtil;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.example.application.util.CustomSignalUtil.bindChild;
import static com.example.application.util.CustomSignalUtil.nullSafe;

class LetterHeader extends HorizontalLayout {

    LetterHeader(LetterModel letterModel) {
        var subject = new H1();
        subject.bindText(letterModel.subject());

        var lastUpdated = new Span();
        lastUpdated.bindText(letterModel.lastUpdated().map(nullSafe(DateTimeUtil::formatShort, "")));

        var statusBadgeContainer = new Div();
        bindChild(statusBadgeContainer, letterModel.state(), LetterStateBadgeFactory::createBadge);

        add(subject);
        add(statusBadgeContainer);
        add(lastUpdated);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }
}
