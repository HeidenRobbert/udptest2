package com.example.robbertvanderheiden.udptest;

import android.content.Context;
import android.widget.EditText;
import com.example.robbertvanderheiden.udptest.UDP.ReceiverThread;
import com.example.robbertvanderheiden.udptest.UDP.Sender;
import java.io.IOException;
import java.net.InetAddress;



public class UDPHandler {
    private EditText display;
    private Context context;
    private EditText message;
    private Sender sender;
    private MainActivity mainActivity;

    public UDPHandler(EditText display, Context context, EditText message){
        this.display = display;
        this.context = context;
        this.message = message;
        sender = new Sender(context);
    }

    public void run(){
        Thread rt = new Thread(new ReceiverThread(context,display));
        rt.start();
        try {
            System.out.println("Adres 1 = " + sender.getBroadcast(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress())));
            System.out.println("Adres 2 = " + sender.getBroadcastAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRelay(){
        try {
            sender.sendBroadcastMessage(message.getText().toString(),sender.getBroadcastAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
