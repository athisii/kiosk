package com.cdac.kiosk.controller;

import com.cdac.kiosk.App;
import com.cdac.kiosk.api.MafisServerApi;
import com.cdac.kiosk.dto.OnboardingReqDto;
import com.cdac.kiosk.dto.OnboardingResDto;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.security.AuthUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.cdac.kiosk.constant.ApplicationConstant.SCENE_ROOT_ERR_MSG;

public class OnboardAuthController extends AbstractBaseController {
    private static final Logger LOGGER = ApplicationLog.getLogger(OnboardAuthController.class);

    private static final int MAX_LENGTH = 30;
    private volatile boolean isDone = false;
    @FXML
    private Button backBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label statusMsg;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    public void backBtnAction() throws IOException {
        App.setRoot("onboard_network_config");
    }


    @FXML
    public void loginBtnAction() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (!isDone) {
                statusMsg.setText("The server is taking more time than expected. Kindly try again.");
                enableControls(backBtn, loginBtn);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
        disableControls(backBtn, loginBtn);
        App.getThreadPool().execute(() -> authenticateUser(usernameTextField.getText(), passwordField.getText()));
    }

    private void authenticateUser(String username, String password) {
        try {
            if (AuthUtil.authenticate(username, password)) {
                // Hardware Type Mapping:
                //      PES - 1
                //      FES - 2
                OnboardingResDto onboardingResDto = MafisServerApi.fetchOnboardingDetails(new OnboardingReqDto(username, "3"));
                App.setPno(username);
                App.setOnboardingUnitDetails(onboardingResDto.getOnboardingUnitDetails());
                // must set on JavaFX thread.
                Platform.runLater(() -> {
                    try {
                        App.setRoot("onboard_complete");
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, SCENE_ROOT_ERR_MSG, ex);
                        throw new GenericException(ex.getMessage());
                    }
                });
                isDone = true;
                return;
            }
            LOGGER.log(Level.INFO, "Incorrect username or password.");
            updateUi("Wrong username or password.");
        } catch (Exception ex) {
            updateUi(ex.getMessage());
        }
        App.setPno(null);
        App.setOnboardingUnitDetails(null);
        isDone = true;
        // clean up UI on failure
        clearUiControls();
        enableControls(backBtn, loginBtn);
    }

    public void initialize() {
        // restrict the TextField Length
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> limitCharacters(usernameTextField, oldValue, newValue));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> limitCharacters(passwordField, oldValue, newValue));

        // ease of use for operator
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
                event.consume();
            }
            if (!statusMsg.getText().isBlank()) {
                statusMsg.setText("");
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginBtnAction();
            }
        });
    }

    private void limitCharacters(TextField textField, String oldValue, String newValue) {
        if (newValue.length() > MAX_LENGTH) {
            textField.setText(oldValue);
        }
    }

    private void updateUi(String message) {
        Platform.runLater(() -> statusMsg.setText(message));
    }

    private void clearUiControls() {
        Platform.runLater(() -> {
            usernameTextField.requestFocus();
            usernameTextField.setText("");
            passwordField.setText("");
        });
    }

    private void disableControls(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    private void enableControls(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(false);
        }
    }

    @Override
    public void onUncaughtException() {
        LOGGER.log(Level.INFO, "***Unhandled exception occurred.");
        backBtn.setDisable(false);
        loginBtn.setDisable(false);
        updateUi("Something went wrong. Please try again");
    }
}
