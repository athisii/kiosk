package com.cdac.kiosk.util;

import com.cdac.kiosk.constant.ApplicationConstant;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayUtil {
    private static final Logger LOGGER = ApplicationLog.getLogger(DisplayUtil.class);

    //Suppress default constructor for noninstantiability
    private DisplayUtil() {
        throw new AssertionError("The DisplayUtil methods must be accessed statically.");
    }

    public static final int[] SCREEN_SD = {1024, 768}; //default FES
    public static final int[] SCREEN_1400X1050 = {1400, 1050};
    public static final int[] SCREEN_1600X1200 = {1600, 1200};
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    static {
        String errorMessage = "**** Could not find a valid resolution format in " + PropertyFile.getProperty(PropertyName.SYSTEM_DISPLAY_RESOLUTION_FILE);
        try {
            String[] resolution = Arrays.stream(Files.readAllLines(Path.of(PropertyFile.getProperty(PropertyName.SYSTEM_DISPLAY_RESOLUTION_FILE)))
                            .stream()
                            .filter(line -> line.contains("--mode") && !line.contains("#"))
                            .reduce((first, second) -> second) // get the last line
                            .orElseThrow(() -> new GenericException(errorMessage))
                            .split("\\s")) // split by white space
                    .filter(word -> word.matches("\\d{3,}[xX]\\d{3,}")) // split by 1920x1080
                    .findFirst()
                    .orElseThrow(() -> new GenericException(errorMessage))
                    .split("[xX]");
            SCREEN_WIDTH = Integer.parseInt(resolution[0]);
            SCREEN_HEIGHT = Integer.parseInt(resolution[1]);
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, () -> "**** No entry for " + PropertyName.SYSTEM_DISPLAY_RESOLUTION_FILE + " in " + ApplicationConstant.DEFAULT_PROPERTY_FILE + " or path pointed by it doesn't exist.");
            throw new GenericException("**** No entry for " + PropertyName.SYSTEM_DISPLAY_RESOLUTION_FILE + " in " + ApplicationConstant.DEFAULT_PROPERTY_FILE + " or path pointed by it doesn't exist.");
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex::getMessage);
            throw new GenericException(ex.getMessage());
        }
    }
}
