package com.cdac.kiosk.controller;

import com.cdac.kiosk.App;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.util.PropertyFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author athisii, CDAC
 * Created on 26/12/22
 */

public class MainController extends AbstractBaseController {
    private static final Logger LOGGER = ApplicationLog.getLogger(MainController.class);
    @FXML
    private VBox onboardMsgVBox;
    @FXML
    private Button startKioskBtn;

    public void initialize() {
        if ("1".equals(PropertyFile.getProperty(PropertyName.INITIAL_SETUP).trim())) {
            onboardMsgVBox.setManaged(true);
            onboardMsgVBox.setVisible(true);
        } else {
            startKioskBtn.setDisable(false);
        }
        startKioskBtn.setOnAction(event -> startKioskAction());
    }

    private void startKioskAction() {
        LOGGER.log(Level.INFO, () -> "Starting Kiosk..");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("runuser", "-l", "sadmin", "-c",
                    "DISPLAY=:0 /opt/firefox/firefox-bin --kiosk " + PropertyFile.getProperty(PropertyName.KIOSK_URL));
            processBuilder.start();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, () -> "Error starting Kiosk: " + e.getMessage());
        }
        Platform.exit();
    }

    @FXML
    public void onSettings() throws IOException {
        if ("1".equals(PropertyFile.getProperty(PropertyName.INITIAL_SETUP).trim())) {
            App.setRoot("onboard_network_config");
        } else {
            App.setRoot("admin_auth");
        }
    }

    @Override
    public void onUncaughtException() {
        LOGGER.log(Level.SEVERE, "***Unhandled exception occurred.");
    }
}
