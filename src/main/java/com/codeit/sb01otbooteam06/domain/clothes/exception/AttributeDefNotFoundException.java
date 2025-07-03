package com.codeit.sb01otbooteam06.domain.clothes.exception;

import com.codeit.sb01otbooteam06.global.exception.ErrorCode;
import java.util.UUID;

public class AttributeDefNotFoundException extends AttributeDefException {

  /**
   * Constructs an AttributeDefNotFoundException with the error code ATTRIBUTEDEF_NOT_FOUND.
   */
  public AttributeDefNotFoundException() {
    super(ErrorCode.ATTRIBUTEDEF_NOT_FOUND);
  }

  /**
   * Creates a new {@code AttributeDefNotFoundException} instance with the specified attribute definition ID included as a detail.
   *
   * @param attributeDefId the UUID of the attribute definition that was not found
   * @return a new {@code AttributeDefNotFoundException} containing the provided attribute definition ID as additional context
   */
  public AttributeDefNotFoundException withId(UUID attributeDefId) {
    AttributeDefNotFoundException exception = new AttributeDefNotFoundException();
    exception.addDetail("attributeDefId", attributeDefId);
    return exception;
  }

}
