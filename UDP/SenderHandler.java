package com.example.robbertvanderheiden.udptest.UDP;

import android.content.Context;
import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SenderHandler implements Runnable{

    private Context context;
    private Button button;
    public SenderHandler(Context context, Button button){
        this.context = context;
        this.button = button;
    }

    @Override
    public void run() {
        Sender sender = new Sender(context);
        String text = button.getText().toString();

        try {
            sender.sendBroadcastMessage(text,InetAddress.getByName("127,.255.255.255")); //sender.getBroadcastAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
