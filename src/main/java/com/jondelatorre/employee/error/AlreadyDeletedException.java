package com.jondelatorre.employee.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ************************************************************************
 * AlreadyDeletedException.java employee project
 * 
 * Exception to be thrown whenever some entity is already deleted and somebody tries to delete it
 * again
 *
 * @since 25 oct 2018
 *************************************************************************
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Entity has already been deleted") // 409
public class AlreadyDeletedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AlreadyDeletedException.class);

    private final String entityName;
    private final String id;

    public AlreadyDeletedException(String entityName, String id) {
        super(String.format("%s with ids = %s has already been deleted", entityName, id));
        logger.info("{} with id = {} has already been deleted", entityName, id);
        this.entityName = entityName;
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getId() {
        return id;
    }



}
