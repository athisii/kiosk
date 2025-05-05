package com.cdac.kiosk.api;


import com.cdac.kiosk.constant.ApplicationConstant;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.dto.*;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.util.PropertyFile;
import com.cdac.kiosk.util.Singleton;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author athisii, CDAC
 * Created on 29/03/23
 */

public class MafisServerApi {

    private static final Logger LOGGER = ApplicationLog.getLogger(MafisServerApi.class);

    //Suppress default constructor for noninstantiability
    private MafisServerApi() {
        throw new AssertionError("The MafisServerApi methods must be accessed statically.");
    }


    /**
     * @return String - MAFIS API home url
     */
    public static String getMafisApiUrl() {
        String mafisServerApi = PropertyFile.getProperty(PropertyName.MAFIS_API_URL);
        if (mafisServerApi.isBlank()) {
            LOGGER.log(Level.SEVERE, () -> "'mafis.api.url' not found or is empty in " + ApplicationConstant.DEFAULT_PROPERTY_FILE);
            throw new GenericException("'mafis.api.url' not found or is empty in " + ApplicationConstant.DEFAULT_PROPERTY_FILE);
        }

        if (mafisServerApi.endsWith("/")) {
            return mafisServerApi + "api/EnrollmentStation";
        }
        return mafisServerApi + "/api/EnrollmentStation";
    }


    public static void validateUserCategory(UserReqDto userReqDto) {
        String jsonRequestData;
        try {
            jsonRequestData = Singleton.getObjectMapper().writeValueAsString(userReqDto);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, ApplicationConstant.JSON_WRITE_ER_MSG);
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }

        HttpResponse<String> response = HttpUtil.sendHttpRequest(HttpUtil.createPostHttpRequest(getUserValidationUrl(), jsonRequestData));
        CommonResDto commonResDto;
        try {
            commonResDto = Singleton.getObjectMapper().readValue(response.body(), CommonResDto.class);
        } catch (JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }
        LOGGER.log(Level.INFO, () -> "***ServerResponseErrorCode: " + commonResDto.getErrorCode());
        if (commonResDto.getErrorCode() != 0) {
            LOGGER.log(Level.INFO, () -> ApplicationConstant.GENERIC_SERVER_ERR_MSG + commonResDto.getDesc());
            throw new GenericException(commonResDto.getDesc());
        }
    }

    public static String getUserValidationUrl() {
        return getMafisApiUrl() + "/GetDetailValidFesPesUser";
    }

    public static OnboardingResDto fetchOnboardingDetails(OnboardingReqDto onboardingReqDto) {
        String jsonRequestData;
        try {
            jsonRequestData = Singleton.getObjectMapper().writeValueAsString(onboardingReqDto);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, ApplicationConstant.JSON_WRITE_ER_MSG);
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }
        HttpResponse<String> response = HttpUtil.sendHttpRequest(HttpUtil.createPostHttpRequest(getOnboardingDetailsUrl(), jsonRequestData));
        OnboardingResDto onboardingResDto;
        try {
            onboardingResDto = Singleton.getObjectMapper().readValue(response.body(), OnboardingResDto.class);
        } catch (JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }
        LOGGER.log(Level.INFO, () -> "***ServerResponseErrorCode: " + onboardingResDto.getErrorCode());
        if (onboardingResDto.getErrorCode() != 0) {
            LOGGER.log(Level.INFO, () -> ApplicationConstant.GENERIC_SERVER_ERR_MSG + onboardingResDto.getDesc());
            throw new GenericException(onboardingResDto.getDesc());
        }
        if (onboardingResDto.getOnboardingUnitDetails() == null || onboardingResDto.getOnboardingUnitDetails().isEmpty()) {
            LOGGER.log(Level.INFO, () -> "***ServerErrorCode: Null or received an empty list of FES device.");
            throw new GenericException("No FES device available for the selected unit.");
        }
        return onboardingResDto;
    }

    public static String getOnboardingDetailsUrl() {
        return getMafisApiUrl() + "/GetListOfVacantFPDevice";
    }

    public static void updateOnboarding(UpdateOnboardingReqDto updateOnboardingReqDto) {
        String jsonRequestData;
        try {
            jsonRequestData = Singleton.getObjectMapper().writeValueAsString(updateOnboardingReqDto);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, ApplicationConstant.JSON_WRITE_ER_MSG);
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }

        HttpResponse<String> response = HttpUtil.sendHttpRequest(HttpUtil.createPostHttpRequest(getUpdateOnboardingUrl(), jsonRequestData));
        CommonResDto commonResDto;
        try {
            commonResDto = Singleton.getObjectMapper().readValue(response.body(), CommonResDto.class);
        } catch (JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
        }
        LOGGER.log(Level.INFO, () -> "***ServerResponseErrorCode: " + commonResDto.getErrorCode());
        if (commonResDto.getErrorCode() != 0) {
            LOGGER.log(Level.INFO, () -> ApplicationConstant.GENERIC_SERVER_ERR_MSG + commonResDto.getDesc());
            throw new GenericException(commonResDto.getDesc());
        }
    }

    public static String getUpdateOnboardingUrl() {
        return getMafisApiUrl() + "/GetOnBoardFPDevice";
    }

}
