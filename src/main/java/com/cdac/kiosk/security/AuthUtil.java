package com.cdac.kiosk.security;

import com.cdac.kiosk.api.DirectoryLookup;
import com.cdac.kiosk.constant.ApplicationConstant;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.util.PropertyFile;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author athisii, CDAC
 * Created on 09/01/23
 */
public class AuthUtil {
    private static final Logger LOGGER = ApplicationLog.getLogger(AuthUtil.class);

    //Suppress default constructor for noninstantiability
    private AuthUtil() {
        throw new AssertionError("The AuthUtil methods  must be accessed statically.");
    }

    public static boolean authenticate(String username, String password) {
        if (password.isBlank() || username.isBlank()) {
            throw new GenericException("Please provide the username and password");
        }

        // PROD environment
        if ("0".equals(PropertyFile.getProperty(PropertyName.ENV))) {
            LOGGER.log(Level.INFO, () -> "***** Authenticating using LDAP ********");
            return DirectoryLookup.doLookup(username, password);
        } else {
            // DEV environment
            LOGGER.log(Level.INFO, () -> "***** Authenticating using properties ********");
            String adminPasswd = PropertyFile.getProperty(PropertyName.ADMIN_PASSWD);
            if (adminPasswd.isBlank()) {
                LOGGER.log(Level.SEVERE, "No entry for '" + PropertyName.ADMIN_PASSWD + "' in " + ApplicationConstant.DEFAULT_PROPERTY_FILE);
                throw new GenericException(ApplicationConstant.GENERIC_ERR_MSG);
            }
            return HashUtil.isSame(adminPasswd, HashUtil.hashPassword(password));
        }
    }
}
