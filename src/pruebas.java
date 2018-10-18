
import Interfaz.ServerGUI;
import Interfaz.ClientGUI;

public class pruebas { 
    public static void main(String[] args) {
        ServerGUI servergui = new ServerGUI(1500);
        ClientGUI clientgui = new ClientGUI("localhost", 1500);
        servergui.setVisible(true);   
        clientgui.setVisible(true);  
    } 
}