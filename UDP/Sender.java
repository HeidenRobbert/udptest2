package com.example.robbertvanderheiden.udptest.UDP;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class Sender {

    private DatagramSocket socket;
    private Context context;

    public Sender(Context context){
        this.context = context;
    }
    /**
     * Scanned naar userinput en stuurt deze door over UDP
     */
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                System.out.println("Message for outside:");
                sendBroadcastMessage(scanner.nextLine(),getBroadcastAddress() );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Verstuurd een String bericht over udp
     * @param broadcastMessage  -> Het bericht in string formaat.
     * @param address           -> het broadcastadres
     */
    public void sendBroadcastMessage(String broadcastMessage, InetAddress address) {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] buffer = broadcastMessage.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9876);
            socket.send(packet);
            System.out.println("send");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verstuurd een Byte[] bericht over udp
     * @param broadcastMessage  -> Het bericht in string formaat.
     * @param address           -> het broadcastadres
     */
    public void sendBroadcastMessage(byte[] broadcastMessage, InetAddress address) {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);

            DatagramPacket packet = new DatagramPacket(broadcastMessage, broadcastMessage.length, address, 9876);
            socket.send(packet);
            System.out.println("send");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Haalt het broadcast adres op van het huidige netwerk.
     * @return -> Het broadcast adres van het huidige netwerk waarmee de telefoon is verbonden
     * @throws IOException
     */
    public InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);
    }

    public InetAddress getBroadcast(InetAddress myIpAddress) {

        NetworkInterface temp;
        InetAddress iAddr = null;
        try {
            temp = NetworkInterface.getByInetAddress(myIpAddress);
            List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

            for (InterfaceAddress inetAddress : addresses) {
                iAddr = inetAddress.getBroadcast();
            }
            System.out.println("iAddr=" + iAddr);
            return iAddr;

        } catch (SocketException e) {

            e.printStackTrace();
            System.out.println("getBroadcast" + e.getMessage());
        }
        return null;
    }
}
