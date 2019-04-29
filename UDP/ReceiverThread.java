package com.example.robbertvanderheiden.udptest.UDP;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.widget.EditText;

import com.example.robbertvanderheiden.udptest.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;


@SuppressWarnings("Duplicates")
public class ReceiverThread implements Runnable {

    private Context context;
    private EditText received;

    public ReceiverThread(Context context, EditText received){
        this.context = context;
        this.received = received;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(9876, getBroadcastAddress());
            socket.setBroadcast(true);
            System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
            byte[] buf;
            DatagramPacket packet;
            while (true) {
                buf = new byte[512];
                packet = new DatagramPacket(buf, buf.length);
                System.out.println("Waiting for data");
                socket.receive(packet);
                String result = new String(packet.getData());
                result = result.split("\u0000")[0];
                System.out.println("INCOMING: " + result);
                System.out.println("Data received");
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Haalt het broadcast adres op van het huidige netwerk.
     * @return -> Het broadcast adres van het huidige netwerk waarmee de telefoon is verbonden
     * @throws IOException
     */
    private InetAddress getBroadcastAddress() throws IOException {
        /*WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);*/



        /*
        InetAddress broadcastAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface
                    .getNetworkInterfaces();

            while (broadcastAddress == null
                    && networkInterface.hasMoreElements()) {
                NetworkInterface singleInterface = networkInterface
                        .nextElement();
                String interfaceName = singleInterface.getName();
                if (interfaceName.contains("wlan0")
                        || interfaceName.contains("eth0")) {
                    for (InterfaceAddress infaceAddress : singleInterface
                            .getInterfaceAddresses()) {
                        broadcastAddress = infaceAddress.getBroadcast();
                        if (broadcastAddress != null) {
                            break;
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

        return broadcastAddress;
         */

        Set<InetAddress> set = new LinkedHashSet<>();
        Enumeration<NetworkInterface> nicList = NetworkInterface.getNetworkInterfaces();
        for(; nicList.hasMoreElements();){
            NetworkInterface nic = nicList.nextElement();
            if(nic.isUp() && !nic.isLoopback()){
                for (InterfaceAddress ia : nic.getInterfaceAddresses())
                    set.add(ia.getBroadcast());
            }
        }
        return Arrays.asList(set.toArray(new InetAddress[0])).get(0);
    }
}
