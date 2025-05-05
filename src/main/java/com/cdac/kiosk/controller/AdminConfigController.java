package com.cdac.kiosk.controller;

import com.cdac.kiosk.App;
import com.cdac.kiosk.logging.ApplicationLog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author athisii, CDAC
 * Created on 29/03/23
 */
public class AdminConfigController extends AbstractBaseController {
    //For Application Log
    private static final Logger LOGGER = ApplicationLog.getLogger(AdminConfigController.class);

    @FXML
    private Label messageLabel;

    @FXML
    public void logOut() throws IOException {
        App.setRoot("admin_auth");
    }

    @FXML
    public void deOnboardAction() throws IOException {
        App.setRoot("de_onboard");
    }

    @FXML
    public void networkDetailsAction() throws IOException {
        App.setRoot("network_details");
    }


    @Override
    public void onUncaughtException() {
        LOGGER.log(Level.INFO, "***Unhandled exception occurred.");
        updateUi("Something went wrong. Please try again");
    }

    private void updateUi(String message) {
        Platform.runLater(() -> messageLabel.setText(message));
    }
}
