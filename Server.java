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
                while ((ALIVE) && ((msg = dis.readUTF()) != null)) {
                    //this.gui.append(client.getPort() + " : " + msg + "\n");
                    try {
                        messages.put(msg);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                        myMessages();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }

        public void myMessages() {
            while (!client.isClosed()) {
                try {
                    dos.writeUTF(dlm.toString());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
                copyQueue(queue);
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    try {
                        String msg = queue.take();
                        
                        int clientPort = parseMessageDesignation(msg);
                        String message = passMessage(msg);
                        if (clientPort == client.getPort()) { //--The message will be send solely to this port.
                            try {
                                dos.writeUTF(message);
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            messages.remove(msg);
                        } else if (clientPort == (-1)) { //--Broadcast. Send to all ports.
                            try {
                                dos.writeUTF(message);
                                Thread.sleep(100); //--To prevent deleting a broadcast message too fast.
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            messages.remove(msg);
                            queue.remove(msg);
                        } else { //--Was not sent to this port.
                            queue.remove(msg);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        /*
         * Copies messages to given queue.
         * Returns the given queue, containing messages.
         */
        public void copyQueue(BlockingQueue<String> queue) {
            messages.forEach((message) -> {
                try {
                    queue.put(message);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        /*
         * Reads the message sent to server and returns
         * the topic of the message (Port number, or Broadcast - All).
         * Return -1 if meant for Broadcast.
         */
        public int parseMessageDesignation(String message) {
            int start = message.indexOf('<');
            int end = message.indexOf('>');
            String port = message.substring(start + 1, end);
            if (!port.equals("Broadcast")) {
                return Integer.parseInt(port);
            }
            return -1;
        }

        /*
         * Reads the message sent to server and returns
         * the source of the message (Port number of sender).
         */
        public int parseMessageSource(String message) {
            int start = message.indexOf('(');
            int end = message.indexOf(')');
            String port = message.substring(start + 1, end);
            return Integer.parseInt(port);
        }
        
        public String passMessage(String str) {
            int start = str.indexOf('>');
            String port = parseMessageSource(str) + "";
            return port + " : " + str.substring(start + 2);
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
        this.ALIVE = true;
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
                break;
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
        try {
            this.client.close();
            //this.server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
