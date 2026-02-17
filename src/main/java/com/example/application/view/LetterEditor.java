package com.example.application.view;

import com.example.application.model.LetterModel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.value.ValueChangeMode;

class LetterEditor extends VerticalLayout {

    LetterEditor(LetterModel letterModel) {
        var header = new LetterHeader(letterModel);

        var body = new RichTextEditor();
        body.setValueChangeMode(ValueChangeMode.LAZY);
        body.bindValue(letterModel.body(), letterModel::setBody);

        var subViewTabs = new TabSheet();
        subViewTabs.add(VaadinIcon.INFO_CIRCLE_O.create(), new DetailsSubview(letterModel));
        subViewTabs.add(VaadinIcon.ENVELOPE_O.create(), new RecipientSubview(letterModel));
        subViewTabs.add(VaadinIcon.PAPERCLIP.create(), new AttachmentsSubview(letterModel));
        subViewTabs.add(VaadinIcon.COMMENTS.create(), new CommentsSubview(letterModel));

        var contentArea = new HorizontalLayout();
        contentArea.setSizeFull();
        contentArea.add(body);
        body.setSizeFull();
        contentArea.add(subViewTabs);
        subViewTabs.setWidth("400px");

        add(header);
        add(contentArea);
        setSizeFull();
    }
}
