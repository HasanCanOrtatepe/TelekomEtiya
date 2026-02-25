package com.etiya.etiyatelekom.common.enums;

import java.util.EnumSet;
import java.util.Set;

public enum TicketStatusEnums {

    CREATED,
    ASSIGNED,
    RESOLVED,
    CLOSED,
    ESCALATED;


    public Set<TicketStatusEnums> allowedNext() {
        return switch (this) {
            case CREATED -> EnumSet.of(ASSIGNED, ESCALATED);
            case ASSIGNED -> EnumSet.of(RESOLVED, ESCALATED);
            case ESCALATED -> EnumSet.of(ASSIGNED, RESOLVED, CLOSED);
            case RESOLVED -> EnumSet.of(CLOSED, ESCALATED);
            case CLOSED -> EnumSet.noneOf(TicketStatusEnums.class);
        };
    }

    public boolean canTransitionTo(TicketStatusEnums next) {
        return (next != null && allowedNext().contains(next));
    }
}
