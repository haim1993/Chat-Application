
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Haim & Noa
 */
public class Client implements Runnable {

    //--Object Variables 
    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;
    private GUI_Client gui;
    private DefaultListModel<String> dlm;
    private boolean ALIVE = false;

    //--Constructor
    public Client(String IPAddress, int port, GUI_Client gui) {
        this.ALIVE = true;
        try {
            client = new Socket(IPAddress, port);
            this.gui = gui;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            gui.btn_reconnect.setEnabled(true);
            this.stop();
        }
        dlm = new DefaultListModel<>();
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF("{" + getPort() + "," + this.gui.getName() + "}");
            String msg;
            while ((ALIVE) && ((msg = dis.readUTF()) != null)) {
                if ((msg.charAt(0) == '[') && (msg.charAt(msg.length() - 1) == ']')) {
                    convertStringToList(msg);
                    gui.updateOnlineClients(this.dlm);
                } else if (parseMessageSource(msg) != client.getLocalPort()) {
                    int index = msg.indexOf(';');
                    msg = msg.substring(index + 1);
                    this.gui.append(msg + "\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            this.gui.append("Server stopped working. \n");
            this.gui.append("To re-enter chat room click 'Reconnect'.\n");
            System.out.println("Do I see this?");
            this.gui.btn_reconnect.setEnabled(true);
            dlm.removeAllElements();
            this.gui.updateOnlineClients(dlm);
        }
    }

    /*
     * Input : An existing list represeted by it's toString.
     * Updates : Add to existing list all port number that are in the list variable.
     */
    public void convertStringToList(String list) {
        dlm.removeAllElements();
        list = list.substring(1, list.length() - 1);
        String[] ports = list.split(", ");
        for (String port : ports) {
            dlm.addElement(port);
        }
    }

    /*
     * To whom the message is meant to.
     * Here we send the message with a designated port.
     */
    public void sendTo(String message, String designation) {
        try {
            dos.writeUTF("(" + this.getPort() + "," + gui.getName() + ")<" + designation + "> " + message);
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
        this.ALIVE = false;
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

    /*
     * Reads the message sent to server and returns
     * the source of the message (Port number of sender).
     */
    public int parseMessageSource(String message) {
        int end = message.indexOf(';');
        String port = message.substring(0, end);
        return Integer.parseInt(port);
    }

}
