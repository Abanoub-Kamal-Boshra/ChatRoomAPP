/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Abanoub Kamal
 */
public class ChatHandler extends Thread{

    DataInputStream  inS;
    PrintStream outS;
    static Vector<ChatHandler> clientsVector = new Vector<ChatHandler>();

    public ChatHandler(Socket s) {
        try{
            inS = new DataInputStream(s.getInputStream());
            outS = new PrintStream(s.getOutputStream());

            clientsVector.add(this);
            start();
        }catch(Exception ex){
             System.out.println("server.ChatHandler.<init>()");
        }
    }

    public void run(){
        try{
            while(true){
            String inputMsg = inS.readLine();
            sendMessageToAll(inputMsg);
        }
        }catch(Exception ex){
            try{
                inS.close();
                outS.close();
                clientsVector.removeElement(this);
            }catch(IOException e){}
        }
    }

    public void sendMessageToAll(String msg){
        for(ChatHandler ch: clientsVector){
            ch.outS.println(msg + " " + ch.getId());
        }
    }

}