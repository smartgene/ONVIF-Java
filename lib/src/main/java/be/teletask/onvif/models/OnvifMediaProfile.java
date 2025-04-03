package be.teletask.onvif.models;

/**
 * Created by Tomas Verhelst on 03/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */
public class OnvifMediaProfile {

    //Constants
    public static final String TAG = OnvifMediaProfile.class.getSimpleName();

    //Attributes
    private final String name;
    private final String token;
    private String encoding;
    private Integer width;
    private Integer height;
    private Integer fps;

    //Constructors

    public OnvifMediaProfile(String name, String token) {
        this.name = name;
        this.token = token;
    }

    //Properties

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
    
    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
    
    public String getEncoding() {
        return encoding;
    }
    
    public Integer getFPS() {
        return fps;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public void setFPS(Integer fps) {
        this.fps = fps;
    }
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "OnvifMediaProfile{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
