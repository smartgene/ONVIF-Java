package be.teletask.onvif.models;

import be.teletask.onvif.DiscoveryMode;
import be.teletask.onvif.OnvifXMLBuilder;


import java.util.Locale;

/**
 * Created by Tomas Verhelst on 05/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */
public class DiscoveryPacket extends OnvifPacket {

    //Constants
    public static final String TAG = DiscoveryPacket.class.getSimpleName();
    private static final String LINE_END = "\r\n";
    private static final String DEFAULT_QUERY = "M-SEARCH * HTTP/1.1" + LINE_END +
            "HOST: 239.255.255.250:1900" + LINE_END +
            "MAN: \"ssdp:discover\"" + LINE_END +
            "MX: 1" + LINE_END +
            //"ST: urn:schemas-upnp-org:service:AVTransport:1" + LINE_END + // Use for Sonos
            //"ST: urn:schemas-upnp-org:device:InternetGatewayDevice:1" + LINE_END + // Use for Routes
            "ST: ssdp:all" + LINE_END + // Use this for all UPnP Devices
            LINE_END;

    //Attributes
    private final String uuid;
    private final DiscoveryMode mode;

    //Constructors
    public DiscoveryPacket(String uuid) {
        this(uuid, DiscoveryMode.ONVIF);
    }

    public DiscoveryPacket(String uuid, DiscoveryMode mode) {
        this.uuid = uuid;
        this.mode = mode;
    }

    //Properties
    @Override
    public byte[] getData() {
        String data = "";
        if (mode.equals(DiscoveryMode.ONVIF)) {
//            StringBuilder builder = new StringBuilder();
//            String header = String.format(Locale.getDefault(), OnvifXMLBuilder.getDiscoverySoapHeader(), uuid);
//            builder.append(header);
//            builder.append(OnvifXMLBuilder.getDiscoverySoapBody());
//            builder.append(OnvifXMLBuilder.getEnvelopeEnd());
//            data = builder.toString();
            
            data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                    + "<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\">\r\n"
                    + "   <Header>\r\n"
                    + "      <wsa:MessageID xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">uuid:eb7e41ee-a6df-4e18-8582-ed1acf29cb8f</wsa:MessageID>\r\n"
                    + "      <wsa:To xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To>\r\n"
                    + "      <wsa:Action xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action>\r\n"
                    + "   </Header>\r\n"
                    + "   <Body>\r\n"
                    + "      <Probe xmlns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
                    + "         <Types>tds:Device</Types>\r\n"
                    + "         <Scopes />\r\n"
                    + "      </Probe>\r\n"
                    + "   </Body>\r\n"
                    + "</Envelope>";
        } else if (mode.equals(DiscoveryMode.UPNP)) {
            data = DEFAULT_QUERY;
        }

        return data.getBytes();
    }

}
