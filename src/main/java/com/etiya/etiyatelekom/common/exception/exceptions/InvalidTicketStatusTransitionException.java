package com.etiya.etiyatelekom.common.exception.exceptions;

import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;

public class InvalidTicketStatusTransitionException extends RuntimeException {

    public InvalidTicketStatusTransitionException(
            TicketStatusEnums from,
            TicketStatusEnums to
    ) {
        super("Invalid ticket status transition: " + from + " -> " + to);
    }
}