package com.example.application.view;

import com.example.application.model.LetterModel;
import com.example.application.util.DateTimeUtil;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import static com.example.application.util.CustomSignalUtil.nullSafe;

class DetailsSubview extends VerticalLayout {

    DetailsSubview(LetterModel letterModel) {
        var subject = new TextField("Subject");
        subject.setValueChangeMode(ValueChangeMode.LAZY);
        subject.bindReadOnly(letterModel.readOnly());
        subject.bindValue(letterModel.subject(), letterModel::setSubject);

        var lastUpdated = new TextField("Last Updated");
        lastUpdated.setReadOnly(true);
        lastUpdated.bindValue(letterModel.lastUpdated().map(nullSafe(DateTimeUtil::formatLong, "")), null);

        subject.setWidthFull();
        add(subject);

        lastUpdated.setWidthFull();
        add(lastUpdated);

        setPadding(false);
    }
}
