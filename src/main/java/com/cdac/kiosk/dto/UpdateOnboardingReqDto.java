package com.cdac.kiosk.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author athisii
 * @version 1.0
 * @since 1/29/25
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOnboardingReqDto {
    String pno;
    String deviceSerialNo;
    String hardwareType;
    String hwId;
    String enrollStationId;
    String action;
}
