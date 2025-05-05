package com.cdac.kiosk.constant;

/**
 * @author athisii, CDAC
 * Created on 17/12/22
 */

// A utility class for mapping properties names in /etc/enrollment-app.properties file.
public class PropertyName {
    //Suppress default constructor for noninstantiability
    private PropertyName() {
        throw new AssertionError("The PropertyName fields must be accessed statically.");
    }

    //DEFAULT_PROPERTY_FILE = "/etc/enrollment-app.properties"

    public static final String LOG_FILE = "log.file";
    public static final String ADMIN_PASSWD = "admin.passwd";
    public static final String LDAP_DOMAIN = "ldap.domain";
    public static final String LDAP_URL = "ldap.url";
    public static final String ENV = "env";
    public static final String MAFIS_API_URL = "mafis.api.url";
    public static final String KIOSK_URL = "kiosk.url";
    public static final String KIOSK_ID = "kiosk.id";
    public static final String KIOSK_UNIT_ID = "kiosk.unit.id";
    public static final String KIOSK_UNIT_CAPTION = "kiosk.unit.caption";
    public static final String APP_VERSION_NUMBER = "app.version.number";
    public static final String SYSTEM_DISPLAY_RESOLUTION_FILE = "system.display.resolution.file";
    public static final String INITIAL_SETUP = "initial.setup";
    public static final String DEVICE_SERIAL_NO = "device.serial.no";
    public static final String HARDWARE_ID = "hardware.id";
}
