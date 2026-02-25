package com.etiya.etiyatelekom.common.exception.exceptions;

public class CannotDeactivateAdminException extends RuntimeException {

    public CannotDeactivateAdminException() {
        super("Cannot deactivate admin user");
    }
}
