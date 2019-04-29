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



@SuppressWarnings("Duplicates")
public class ReceiverThread implements Runnable {

    private Context context;
    private EditText received;
    private MainActivity mainActivity;

    public ReceiverThread(Context context, EditText received, MainActivity mainActivity){
        this.context = context;
        this.received = received;
        this.mainActivity = mainActivity;
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
                mainActivity.textSetter("er is iets binnen");
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
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);
    }

}
