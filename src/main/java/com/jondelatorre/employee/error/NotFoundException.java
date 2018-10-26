package com.jondelatorre.employee.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ************************************************************************ NotFoundException.java
 * employee project
 * 
 * Generic exception to be thrown whenever some entity searched by a single field is not found in
 * the database
 *
 * @since 25 oct 2018
 *************************************************************************
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found") // 404
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(NotFoundException.class);

    private final String entityName;
    private final String fieldName;
    private final String fieldValue;

    /**
     * 
     * @param entityName Name of the entity not found (e.g.: Employee)
     * @param fieldName Name of the field used to search the entity (e.g.: id)
     * @param value Value used to identify the entity (e.g.: 5)
     */
    public NotFoundException(final String entityName, final String fieldName,
            final String fieldValue) {
        super(String.format("%s with %s = %s not found in the database", entityName, fieldName,
                fieldValue));
        logger.info("{} with {} = {} not found in the database", entityName, fieldName, fieldValue);
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

}
