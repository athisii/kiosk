package com.cdac.kiosk.constant;

/**
 * @author athisii, CDAC
 * Created on 02/12/22
 */

public class ApplicationConstant {
    //Suppress default constructor for noninstantiability
    private ApplicationConstant() {
        throw new AssertionError("The ApplicationConstant fields must be accessed statically.");
    }

    public static final String DEFAULT_PROPERTY_FILE = "/etc/kiosk.properties";
    public static final String INTERNAL = "Internal";
    public static final String EXTERNAL = "External";
    public static final String GENERIC_ERR_MSG = "Something went wrong. Please try again.";
    public static final String GENERIC_SERVER_ERR_MSG = "Received non-zero error code from the server: ";
    public static final String JSON_READ_ERR_MSG = "Error occurred while reading json data.";
    public static final String JSON_WRITE_ER_MSG = "Error occurred while writing as JSON string.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String SCENE_ROOT_ERR_MSG = "**Error occurred while setting scene root: ";

}
