package com.it.fi.itismeucci;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
    public static ArrayList<Persona> personePresenti = new ArrayList<>(); 
    private static ServerSocket serverSocket;

    public void avvia(){
        try {
            serverSocket = new ServerSocket(7777);
            do{
                System.out.println("Server in attesa...");
                Socket socket = serverSocket.accept();
                System.out.println("Socket: " + socket);
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
                System.out.println();
            }while(true);
        } catch (Exception e) {
            System.out.println("SERVER SPENTO");
        }
    }
}
