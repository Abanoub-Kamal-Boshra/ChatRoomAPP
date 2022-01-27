/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Abanoub Kamal
 */
public class ServerMulti{

    ServerSocket myServerSocket;
 
    public ServerMulti(){
        try{
            myServerSocket = new ServerSocket(5000);
            while(true){
                Socket internalSocket = myServerSocket.accept();
                new ChatHandler(internalSocket);
            }
        }catch(Exception ex){
            System.out.println("server.ServerMulti.<init>()");
        }
    }

    public static void main(String [] args){
        new ServerMulti();
    }
    

}
