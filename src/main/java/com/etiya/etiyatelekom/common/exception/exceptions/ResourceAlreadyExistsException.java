package com.etiya.etiyatelekom.common.exception.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {

    private String resourceName;
    private String field;
    private String fieldName;

    public ResourceAlreadyExistsException() {}

    public ResourceAlreadyExistsException(String resourceName, String field, String fieldName) {
        super(String.format("%s already exists with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public String getResourceName() { return resourceName; }
    public String getField() { return field; }
    public String getFieldName() { return fieldName; }
}
