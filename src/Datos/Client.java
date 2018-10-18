package Datos;

import Interfaz.ClientGUI;
import Negocio.Mensaje;
import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  { 
    public ObjectInputStream sInput;		// to read from the socket
    public ObjectOutputStream sOutput;		// to write on the socket
    public Socket socket; 
    public ClientGUI cg;  
    public String server, username;
    public int port; 
    public Client(String server, int port, String username) { 
            this(server, port, username, null);
    } 
    public Client(String server, int port, String username, ClientGUI cg) {
            this.server = server;
            this.port = port;
            this.username = username; 
            this.cg = cg;
    } 
    public boolean start() { 
        try {
            socket = new Socket(server, port);
        }  
        catch(Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg); 
        try {
            sInput  = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        } 
        new ListenFromServer().start(); 
        try {
            sOutput.writeObject(username); 
        }
        catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        } 
        return true;
    } 
    public void display(String msg) {
        if(cg == null) {
            System.out.println(msg);
            
        } else {
            cg.append(msg + "\n"); 
             
        } 
    } 
    public void sendMessage(Mensaje msg) {
        try {
            sOutput.writeObject(msg);
            
        }
        catch(IOException e) {
            display("Exception writing to server: " + e);
        }
    } 
    public void enviarCorreo(Mensaje mensaje){
        try {
            sOutput.writeObject(mensaje);
        }
        catch(IOException e) {
            display("Exception writing to server: " + e);
        }
    }
    public void disconnect() {
        try { 
                if(sInput != null) sInput.close();
        }
        catch(Exception e) {} 
        try {
                if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {} 
        try{
            if(socket != null) socket.close();
        } catch(Exception e) {}  
            if(cg != null)
                cg.connectionFailed(); 
    }  
    public class ListenFromServer extends Thread { 
        public void run() {
            while(true) {
                try {
                    String msg = (String) sInput.readObject(); 
                    if(cg == null) {
                        System.out.println(msg);
                        System.out.print("> ");
                    }
                    else {
                        cg.append(msg);  
                    }
                }
                catch(IOException e) {
                    display("Server has close the connection: " + e);
                    if(cg != null) 
                        cg.connectionFailed();
                    break;
                } 
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }
}
