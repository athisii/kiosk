module com.cdac.kiosk {
    //core java
    requires java.net.http;
    requires java.logging;
    requires java.naming;
    requires java.smartcardio;
    //javafx
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    // exports
    opens com.cdac.kiosk to javafx.fxml;
    exports com.cdac.kiosk;
    exports com.cdac.kiosk.controller;
    opens com.cdac.kiosk.controller to javafx.fxml;
    exports com.cdac.kiosk.security;
    opens com.cdac.kiosk.security to javafx.fxml;
    exports com.cdac.kiosk.api;
    opens com.cdac.kiosk.api to javafx.fxml;
    exports com.cdac.kiosk.logging;
    opens com.cdac.kiosk.logging to javafx.fxml;
    exports com.cdac.kiosk.util;
    opens com.cdac.kiosk.util to javafx.fxml;
    exports com.cdac.kiosk.dto;
    opens com.cdac.kiosk.dto to javafx.fxml;
    exports com.cdac.kiosk.enums;
    opens com.cdac.kiosk.enums to javafx.fxml;

    requires static lombok;
}
