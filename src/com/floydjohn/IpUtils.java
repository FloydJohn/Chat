package com.floydjohn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

public class IpUtils {
    public static String getInternalIP() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Console.printlnPure("Error retrieving internal IP : "+ex,Console.ANSI_RED);
            return "NO_IP";
        }
        return null;
    }

    public static String getExternalIP(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ip;
        } catch (Exception e){
            Console.printlnPure("Error retrieving external IP : "+e,Console.ANSI_RED);
            return "NO_IP";
        }
    }
}
