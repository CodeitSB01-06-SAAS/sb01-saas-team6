package com.codeit.sb01otbooteam06.domain.clothes.exception;

import com.codeit.sb01otbooteam06.global.exception.ErrorCode;
import java.util.UUID;

public class AttributeDefAlreadyExistsException extends AttributeDefException {

  /**
   * Constructs a new exception indicating that the attribute definition already exists.
   */
  public AttributeDefAlreadyExistsException() {
    super(ErrorCode.ATTRIBUTEDEF_ALREADY_EXISTS);
  }

  /**
   * Returns a new instance of this exception with the specified attribute definition ID included as additional detail.
   *
   * @param attributeDefId the UUID of the attribute definition that already exists
   * @return a new AttributeDefAlreadyExistsException instance with the attributeDefId detail attached
   */
  public AttributeDefAlreadyExistsException withId(UUID attributeDefId) {
    AttributeDefAlreadyExistsException exception = new AttributeDefAlreadyExistsException();
    exception.addDetail("attributeDefId", attributeDefId);
    return exception;
  }
}
