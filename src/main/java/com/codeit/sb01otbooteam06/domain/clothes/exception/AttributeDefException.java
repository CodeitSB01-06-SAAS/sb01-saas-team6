package com.codeit.sb01otbooteam06.domain.clothes.exception;

import com.codeit.sb01otbooteam06.global.exception.ErrorCode;
import com.codeit.sb01otbooteam06.global.exception.OtbooException;

public class AttributeDefException extends OtbooException {

  /**
   * Constructs a new AttributeDefException with the specified error code.
   *
   * @param errorCode the error code representing the specific attribute definition error
   */
  public AttributeDefException(ErrorCode errorCode) {
    super(errorCode);
  }

}