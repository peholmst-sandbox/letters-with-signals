package com.example.application.view;

import com.example.application.model.LetterListItemModel;
import com.example.application.util.DateTimeUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.card.Card;

import static com.vaadin.flow.signals.impl.Effect.effect;
import static java.util.Objects.requireNonNull;

final class LetterListCard extends Composite<Card> {

    LetterListCard(LetterListItemModel itemModel) {
        var content = getContent();
        effect(this, () -> content.setTitle(itemModel.subject().get()));
        effect(this, () -> content.setSubtitle(DateTimeUtil.formatShort(requireNonNull(itemModel.lastUpdated().get()))));
        content.setHeaderSuffix(LetterStateBadgeFactory.createBadge(itemModel.state()));
    }
}
