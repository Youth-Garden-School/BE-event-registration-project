package com.eventregistration.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkUtils {

    /**
     * Gets the server's IP address
     * @return The server's IP address or "unknown" if it cannot be determined
     */
    public static String getServerIpAddress() {
        try {
            // Try to get the non-loopback IPv4 address
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.getHostAddress().contains(":")) {
                        // Skip IPv6 addresses
                        continue;
                    }
                    return address.getHostAddress();
                }
            }

            // Fallback to localhost if no other address is found
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.error("Failed to determine server IP address", e);
            return "unknown";
        }
    }
}
