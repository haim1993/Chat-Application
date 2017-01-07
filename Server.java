
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
 * @author Haim & Noa
 */
public class Server implements Runnable {

    private class ConnectionToClient implements Runnable {

        //--Object Variables
        private Socket client;
        private DataInputStream dis;
        private DataOutputStream dos;
        private GUI_Server gui;

        //--Constructor
        private ConnectionToClient(Socket client, GUI_Server gui) {
            this.client = client;
            this.gui = gui;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(client.getInputStream());
                outputMessages();
                String msg;
                while ((isAlive()) && ((msg = dis.readUTF()) != null)) {
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

        /*
         * All the messages sent by the clients are recieved by the server.
         * here we open a new thread and call methods that regulate the messages
         * to the correct client.
         */
        public void outputMessages() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dos = new DataOutputStream(client.getOutputStream());
                        sendNewUpdatedList(dos);
                        sendOutMyMessages();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }

        /*
         * In this method we send out the messages to the
         * right designation. If direct messaging or broadcast.
         */
        public void sendOutMyMessages() {
            while (isAlive()) {
                BlockingQueue<String> queue = new LinkedBlockingQueue<>();
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
                                Thread.sleep(250); //--To prevent deleting a broadcast message too fast.
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
         * Opens a new thread that continuously runs and send to client
         * the most updated online list.
         */
        public void sendNewUpdatedList(DataOutputStream dos) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isAlive()) {
                        try {
                            dos.writeUTF(dlm.toString());
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        client.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
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

        /*
         * Returns the message the way it shows on clients screen.
         * Who sent it, and what is the content.
         */
        public String passMessage(String str) {
            int start = str.indexOf('>');
            String port = parseMessageSource(str) + "";
            return port + " : " + str.substring(start + 2);
        }
    }

    //--Object Variables
    private ServerSocket server;
    private Socket client;
    private DefaultListModel<String> dlm;
    private ArrayList<String> serverMessages;
    private GUI_Server gui;
    private boolean ALIVE = false;
    private BlockingQueue<String> messages;

    //--Constructor
    public Server(int port, GUI_Server gui) {
        try {
            this.server = new ServerSocket(port);

        } catch (IOException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        dlm = new DefaultListModel<>();
        this.gui = gui;
        this.ALIVE = true;
        messages = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        this.ALIVE = true;
        while (isAlive()) {
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
            this.server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
