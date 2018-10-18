package Datos;

import Interfaz.ServerGUI;
import Negocio.Mensaje;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*; 
public class Server { 
    public static int uniqueId; 
    public ArrayList<ClientThread> al; 
    public ServerGUI sg; 
    public SimpleDateFormat sdf; 
    public int port; 
    public boolean keepGoing; 
    
    public static LinkedList<Mensaje> mensajes = new LinkedList<Mensaje>();
    public Server(int port) {
            this(port, null);
    } 
    public Server(int port, ServerGUI sg) { 
            this.sg = sg; 
            this.port = port; 
            sdf = new SimpleDateFormat("HH:mm:ss"); 
            al = new ArrayList<ClientThread>();
    } 
    public void start() {
        keepGoing = true; 
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(keepGoing) { 
                display("Server waiting for Clients on port " + port + "."); 
                Socket socket = serverSocket.accept();   
                if(!keepGoing)
                        break;
                ClientThread t = new ClientThread(socket); 
                al.add(t); 
                t.start();
            } try {
                serverSocket.close();
                for(int i = 0; i < al.size(); ++i) {
                    ClientThread tc = al.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    }
                    catch(IOException ioE) {
                    }
                }
            } catch(Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }	 
    public void stop() {
        keepGoing = false; 
        try {
            new Socket("localhost", port);
        }
        catch(Exception e) { 
        }
    } 
    public void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        if(sg == null) {
            System.out.println(time);  
        } else{
            sg.appendEvent(time + "\n");
             
        }
    } 
    public synchronized void broadcast(String message) { 
        String time = sdf.format(new Date());
        String messageLf = time + " " + message + "\n"; 
        if(sg == null) {
            System.out.print(messageLf);  
        } else {
            for(int i = al.size(); --i >= 0;) {
                ClientThread ct = al.get(i); 
                if(!ct.writeMsg(messageLf)) {
                    al.remove(i);
                    display("Disconnected Client " + ct.username + " removed from list.");
                }
            }
        }
    } 
    synchronized void remove(int id) { 
        for(int i = 0; i < al.size(); ++i) {
            ClientThread ct = al.get(i); 
            if(ct.id == id) {
                al.remove(i);
                return;
            }
        }
    }  
    public class ClientThread extends Thread { 
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput; 
        int id; 
        String username; 
        Mensaje cm; 
        
        String date; 
        public ClientThread(Socket socket) {
            id = ++uniqueId;
            this.socket = socket; 
            System.out.println("Thread trying to create Object Input/Output Streams");
            try { 
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput  = new ObjectInputStream(socket.getInputStream()); 
                username = (String) sInput.readObject();
                display(username + " just connected.");
            }
            catch (IOException e) {
                    display("Exception creating new Input/output Streams: " + e);
                    return;
            } 
            catch (ClassNotFoundException e) {
            }
            date = new Date().toString() + "\n";
        } 
        public void run() { 
            boolean keepGoing = true;
            while(keepGoing) { 
                try {
                    cm = (Mensaje) sInput.readObject();
                    mensajes.add(cm);
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;				
                }
                catch(ClassNotFoundException e2) {
                    break;
                } 
                String message = cm.getMessage(); 
                switch(cm.getType()) { 
                    case Mensaje.MESSAGE:
                            broadcast(username + ": " + message);
                            break;
                    case Mensaje.LOGOUT:
                            display(username + " disconnected with a LOGOUT message.");
                            keepGoing = false;
                            break;
                    case Mensaje.WHOISIN:
                            writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
                            // scan al the users connected
                            for(int i = 0; i < al.size(); ++i) {
                                    ClientThread ct = al.get(i);
                                    writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
                            }
                            break;
                }
            } 
            remove(id);
            close();
        } 
        public void close() { 
            try {
                if(sOutput != null) sOutput.close();
            } catch(Exception e) {}
            try {
                    if(sInput != null) sInput.close();
            }
            catch(Exception e) {};
            try {
                    if(socket != null) socket.close();
            }
            catch (Exception e) {}
        } 
        public boolean writeMsg(String msg) { 
          
            if(!socket.isConnected()) { 
                close();
                return false;
                
            } 
            try {
                sOutput.writeObject(msg); 
                sg.llenarT();
                 
            } 
            catch(IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }
    }
}

