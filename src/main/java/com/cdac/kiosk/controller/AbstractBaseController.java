package com.cdac.kiosk.controller;

import com.cdac.kiosk.constant.ApplicationConstant;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.util.PropertyFile;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author athisii, CDAC
 * Created on 07/08/23
 */
public abstract class AbstractBaseController {
    private static final Logger LOGGER = ApplicationLog.getLogger(AbstractBaseController.class);

    public abstract void onUncaughtException();

    public String getAppVersion() {
        String appVersionNumber = PropertyFile.getProperty(PropertyName.APP_VERSION_NUMBER);
        if (appVersionNumber.isBlank()) {
            LOGGER.log(Level.SEVERE, () -> "No entry for '" + PropertyName.APP_VERSION_NUMBER + "' or is empty in " + ApplicationConstant.DEFAULT_PROPERTY_FILE);
            throw new GenericException("No entry for '" + PropertyName.APP_VERSION_NUMBER + "' or is empty in " + ApplicationConstant.DEFAULT_PROPERTY_FILE);
        }
        return appVersionNumber;
    }
}
