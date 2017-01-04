package chatapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Shlez
 */
public class Client implements Runnable {

    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;
    private GUI_Client gui;
    private DefaultListModel<String> dlm;
    private boolean ALIVE = false;

    public Client(String IPAddress, int port, GUI_Client gui) {
        this.ALIVE = true;
        try {
            client = new Socket(IPAddress, port);
            this.gui = gui;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            String msg;
            while ((msg = dis.readUTF()) != null) {
                this.gui.append("Someone : " + msg + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendTo(String message, String designation) {
        try {
            dos.writeUTF("<" + designation + "> " + message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Returns the running client's port.
     */
    public int getPort() {
        return this.client.getLocalPort();
    }

    /*
     * Stops the running client from operating.
     */
    public void stop() {
        try {
            this.client.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Returns the value of the private variable ALIVE.
     */
    public boolean isAlive() {
        return this.ALIVE;
    }

}
