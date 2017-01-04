package chatapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Shlez
 */
public class Server implements Runnable {

    private class ConnectionToClient implements Runnable {

        private Socket client;
        private DataInputStream dis;
        private DataOutputStream dos;
        private GUI_Server gui;

        private ConnectionToClient(Socket client, GUI_Server gui) {
            this.client = client;
            this.gui = gui;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(client.getInputStream());
                findMessage();
                String msg;
                while ((msg = dis.readUTF()) != null) {
                    this.gui.append(client.getPort() + " : " + msg + "\n");
                    messages.add(msg);
                    if (msg.equals("bye")) {
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.gui.append(client.getPort() + " left.\n");
                removeClient(client);
            }
        }

        public void findMessage() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dos = new DataOutputStream(client.getOutputStream());
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        for (int i = 0; i < messages.size(); i++) {
                            String message = messages.take();
                            try {
                                dos.writeUTF(message);
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }).start();
        }
    }

    private ServerSocket server;
    private Socket client;
    private DefaultListModel<String> dlm;
    private ArrayList<String> serverMessages;
    private GUI_Server gui;
    private boolean ALIVE = false;
    private BlockingQueue<String> messages;

    public Server(int port, GUI_Server gui) {
        try {
            this.server = new ServerSocket(port);

        } catch (IOException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        dlm = new DefaultListModel<String>();
        this.gui = gui;
        this.ALIVE = true;
        messages = new LinkedBlockingQueue<String>();
    }

    @Override
    public void run() {
        while (ALIVE) {
            try {
                this.client = this.server.accept();
                addClient(client);
                gui.updateOnlineClients();
                this.gui.append(client.getPort() + " joined.\n");

                ConnectionToClient connectToClient = new ConnectionToClient(client, gui);
                Thread th = new Thread(connectToClient);
                th.start();

            } catch (IOException ex) {
                Logger.getLogger(Server.class
                        .getName()).log(Level.SEVERE, null, ex);
                stop();
            }
        }
        this.gui.append("Server stopped working.\n");
    }

    /*
     * Add client to the online client list.
     */
    public void addClient(Socket client) {
        dlm.addElement(client.getPort() + "");
    }

    /*
     * Removes client from the online client list.
     */
    public void removeClient(Socket client) {
        dlm.remove(dlm.indexOf(client.getPort() + ""));
    }

    /*
     * Returns the online contact list.
     */
    public DefaultListModel<String> getClientList() {
        return this.dlm;
    }

    /*
     * Returns the value of the private variable ALIVE.
     */
    public boolean isAlive() {
        return this.ALIVE;
    }

    /*
     * Stops the running server from operating.
     */
    public void stop() {
        this.ALIVE = false;
    }

}
