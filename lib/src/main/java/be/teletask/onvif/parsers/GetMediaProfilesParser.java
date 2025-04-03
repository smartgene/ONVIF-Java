package be.teletask.onvif.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.responses.OnvifResponse;

/**
 * Created by Tomas Verhelst on 04/09/2018. Copyright (c) 2018 TELETASK BVBA.
 * All rights reserved.
 */
public class GetMediaProfilesParser extends OnvifParser<List<OnvifMediaProfile>> {

    // Constants
    public static final String TAG = GetMediaProfilesParser.class.getSimpleName();
    private static final String KEY_PROFILES = "Profiles";
    private static final String ATTR_TOKEN = "token";
    private static final String ATTR_NAME = "Name";
    private static final String KEY_WIDTH = "Width";
    private static final String KEY_HEIGHT = "Height";
    private static final String KEY_ENCODING = "Encoding";
    private static final String KEY_FPS = "FrameRateLimit";
    private static final String KEY_VIDEO_CONF = "VideoEncoderConfiguration";

    @Override
    public List<OnvifMediaProfile> parse(OnvifResponse response) {
        List<OnvifMediaProfile> profiles = new ArrayList<>();

        try {
            getXpp().setInput(new StringReader(response.getXml()));
            eventType = getXpp().getEventType();
            OnvifMediaProfile current = null;
            boolean inVideoConf = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    switch (getXpp().getName()) {
                    case KEY_PROFILES:
                        String token = getXpp().getAttributeValue(null, ATTR_TOKEN);
                        getXpp().nextTag();
                        if (getXpp().getName().equals(ATTR_NAME)) {
                            getXpp().next();
                            String name = getXpp().getText();
                            current = new OnvifMediaProfile(name, token);
                            profiles.add(current);
                        }
                        break;
                    case KEY_WIDTH:
                        getXpp().next();
                        if (current != null && inVideoConf) {
                            current.setWidth(Integer.parseInt(getXpp().getText()));
                        }
                        break;
                    case KEY_HEIGHT:
                        getXpp().next();
                        if (current != null && inVideoConf) {
                            current.setHeight(Integer.parseInt(getXpp().getText()));
                        }
                        break;
                    case KEY_FPS:
                        getXpp().next();
                        if (current != null && inVideoConf) {
                            current.setFPS(Integer.parseInt(getXpp().getText()));
                        }
                        break;
                    case KEY_ENCODING:
                        getXpp().next();
                        if (current != null && inVideoConf) {
                            current.setEncoding(getXpp().getText());
                        }
                        break;
                    case KEY_VIDEO_CONF:
                        inVideoConf = true;
                        break;
                    }
                }
                
                if (eventType == XmlPullParser.END_TAG ) {
                    switch (getXpp().getName()) {
                    case KEY_PROFILES:
                        current = null;
                        break;
                    case KEY_VIDEO_CONF:
                        inVideoConf = false;
                        break;
                    }
                }
                eventType = getXpp().next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return profiles;
    }

}
