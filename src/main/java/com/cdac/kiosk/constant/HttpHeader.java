package com.cdac.kiosk.constant;

/**
 * @author athisii, CDAC
 * Created on 02/12/22
 */

public class HttpHeader {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";

    //Suppress default constructor for noninstantiability
    private HttpHeader() {
        throw new AssertionError("The HttpHeader fields must be accessed statically.");
    }
}
