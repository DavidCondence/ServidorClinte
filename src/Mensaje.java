
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
public class Mensaje implements Serializable {
    String Destinatario,Asunto,Mensaje ; 
    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;
    private String message;
    public Mensaje(String Destinatario, String Asunto, String Mensaje) {
        this.Destinatario = Destinatario;
        this.Asunto = Asunto;
        this.Mensaje = Mensaje;
    }  
    Mensaje(int type, String message) {
            this.type = type;
            this.message = message;
    }  
    public String getDestinatario() {
        return Destinatario;
    } 
    public void setDestinatario(String Destinatario) {
        this.Destinatario = Destinatario;
    } 
    public String getAsunto() {
        return Asunto;
    } 
    public void setAsunto(String Asunto) {
        this.Asunto = Asunto;
    } 
    public String getMensaje() {
        return Mensaje;
    } 
    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    } 
    int getType() {
        return type;
    }
    String getMessage() {
        return "Mensaje{" + "Destinatario=" + Destinatario + ", Asunto=" + Asunto + ", Mensaje=" + Mensaje + ", type=" + type + ", message=" + message + '}';
    }

    @Override
    public String toString() {
        return "Mensaje{" + "Destinatario=" + Destinatario + ", Asunto=" + Asunto + ", Mensaje=" + Mensaje + ", type=" + type + ", message=" + message + '}';
    }
    
    
}
