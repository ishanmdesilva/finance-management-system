package com.inova.pfms.exception;

public class ObjectDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    private final Object objectData;

    public ObjectDoesNotExistException(Object object, String message) {
        super(String.format("%s: %s", message, object != null ? object.toString() : "null"));
        this.objectData = object;
    }

    public Object getObjectData() {
        return objectData;
    }
}
