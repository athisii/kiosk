package com.cdac.kiosk.controller;

import com.cdac.kiosk.App;
import com.cdac.kiosk.constant.PropertyName;
import com.cdac.kiosk.exception.GenericException;
import com.cdac.kiosk.logging.ApplicationLog;
import com.cdac.kiosk.util.PropertyFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.cdac.kiosk.constant.ApplicationConstant.SCENE_ROOT_ERR_MSG;

public class NetworkDetailsController extends AbstractBaseController {
    private static final Logger LOGGER = ApplicationLog.getLogger(NetworkDetailsController.class);
    @FXML
    private Label messageLabel;
    @FXML
    private TextField hostnameTextField;
    @FXML
    private TextField ipAddressTextField;
    @FXML
    private TextField subnetMaskTextField;
    @FXML
    private TextField defaultGatewayTextField;
    @FXML
    private TextField dnsIpTextField;
    @FXML
    private Button backBtn;
    @FXML
    private TextField ldapUrlTextField;
    @FXML
    private TextField mafisUrlTextField;
    @FXML
    private TextField kioskUrlTextField;

    public void initialize() {
        backBtn.setOnAction(event -> backBtnAction());
        setTextFieldValuesOnUI(); // only set after getting all required fields
    }

    private void backBtnAction() {
        try {
            App.setRoot("admin_config");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, SCENE_ROOT_ERR_MSG, ex);
        }
    }


    private void setTextFieldValuesOnUI() {
        try {
            List<String> lines = Files.readAllLines(Path.of("/etc/network/interfaces"));
            lines.forEach(line -> {
                line = line.trim();
                String[] entry = line.split("\\s+");
                if (entry[0].equalsIgnoreCase("address")) {
                    ipAddressTextField.setText(entry[1]);
                } else if (entry[0].equalsIgnoreCase("netmask")) {
                    subnetMaskTextField.setText(entry[1]);
                } else if (entry[0].equalsIgnoreCase("gateway")) {
                    defaultGatewayTextField.setText(entry[1]);
                } else if (entry[0].equalsIgnoreCase("dns-nameservers")) {
                    StringBuilder dnsIps = new StringBuilder();
                    for (int i = 1; i < entry.length - 1; i++) {
                        dnsIps.append(entry[i]).append(",");
                    }
                    dnsIps.append(entry[entry.length - 1]);
                    dnsIpTextField.setText(dnsIps.toString());
                }
            });
            hostnameTextField.setText(getHostname());
            ldapUrlTextField.setText(PropertyFile.getProperty(PropertyName.LDAP_URL));
            mafisUrlTextField.setText(PropertyFile.getProperty(PropertyName.MAFIS_API_URL));
            kioskUrlTextField.setText(PropertyFile.getProperty(PropertyName.KIOSK_URL));
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, () -> "***Error: " + ex.getMessage());
            messageLabel.setText(ex.getMessage());
        }
    }

    private String getHostname() {
        BufferedReader input = null;
        try {
            Process process = Runtime.getRuntime().exec("hostname");
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = input.readLine();
            if (line == null || line.isBlank()) {
                LOGGER.log(Level.INFO, () -> "***Error: Received null value hostname");
                throw new GenericException("Failed to get hostname.");
            }
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                LOGGER.log(Level.INFO, () -> "***Error: Process Exit Value: " + exitVal);
                throw new GenericException("Failed to get hostname.");
            }
            return line;
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, () -> "***Error: " + ex.getMessage());
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new GenericException(ex.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e::getMessage);
            }
        }
    }

    @Override
    public void onUncaughtException() {
        LOGGER.log(Level.INFO, "***Unhandled exception occurred.");
    }
}
