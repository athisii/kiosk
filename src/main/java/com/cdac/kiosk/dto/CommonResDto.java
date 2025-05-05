package com.cdac.kiosk.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author root
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonResDto {
    int errorCode;
    String desc;
}
