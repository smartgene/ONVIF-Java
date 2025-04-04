package be.teletask.onvif.models;

/**
 * Created by Tomas Verhelst on 04/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */
public enum OnvifType {
    CUSTOM(""),
    GET_SERVICES("http://www.onvif.org/ver10/device/wsdl"),
    GET_DEVICE_INFORMATION("http://www.onvif.org/ver10/device/wsdl"),
    GET_MEDIA_PROFILES("http://www.onvif.org/ver10/media/wsdl"),
    GET_STREAM_URI("http://www.onvif.org/ver10/media/wsdl"),
    GET_SNAPSHOT_URI("http://www.onvif.org/ver10/media/wsdl"),
    GET_STATUS("http://www.onvif.org/ver20/ptz/wsdl"),
    GET_PTZ_SERVICE("http://www.onvif.org/ver20/ptz/wsdl"),
    ABSOLUTE_MOVE("http://www.onvif.org/ver20/ptz/wsdl"),
    CONTINUOUS_MOVE("http://www.onvif.org/ver20/ptz/wsdl"),
    GOTO_HOME_POSITION("http://www.onvif.org/ver20/ptz/wsdl"),
    GOTO_PRESET("http://www.onvif.org/ver20/ptz/wsdl"),
    GET_PRESETS("http://www.onvif.org/ver20/ptz/wsdl"),
    SET_PRESET("http://www.onvif.org/ver20/ptz/wsdl"),
    REMOVE_PRESET("http://www.onvif.org/ver20/ptz/wsdl"),
    GET_CONFIGURATIONS("http://www.onvif.org/ver20/ptz/wsdl")
    ;

    public final String namespace;

    OnvifType(String namespace) {
        this.namespace = namespace;
    }

}
