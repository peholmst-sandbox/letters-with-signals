package com.example.application.view;

import com.example.application.model.LetterListItemModel;
import com.example.application.util.DateTimeUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.signals.impl.Effect;

import static java.util.Objects.requireNonNull;

final class LetterListCard extends Composite<Card> {

    LetterListCard(LetterListItemModel itemModel) {
        var content = getContent();
        Effect.effect(this, () -> {
            var letterListItem = requireNonNull(itemModel.item().get());
            content.setTitle(letterListItem.subject());
            content.setSubtitle(DateTimeUtil.formatShort(letterListItem.lastUpdated()));
            content.setHeaderSuffix(LetterStateBadgeFactory.createBadge(letterListItem.state()));
        });
    }
}
