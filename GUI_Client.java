
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Haim & Noa
 */
public class GUI_Client extends javax.swing.JFrame {

    //--Object Variables
    private Client client;

    //--Class Variables
    static String NAME = "";
    static int PORT = 6060;
    static String IP = "127.0.0.1";

    /**
     * Creates new form GUI_Client
     */
    public GUI_Client() {
        initComponents();
        startClient();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_chat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea_chat = new javax.swing.JTextArea();
        txt_send = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        btn_reconnect = new javax.swing.JButton();
        lbl_chat = new javax.swing.JLabel();
        panel_online = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_online = new javax.swing.JList<>();
        lbl_online = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");

        panel_chat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

        txtArea_chat.setColumns(20);
        txtArea_chat.setRows(5);
        txtArea_chat.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtArea_chat.setEnabled(false);
        jScrollPane1.setViewportView(txtArea_chat);
        DefaultCaret caret = (DefaultCaret) txtArea_chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        txt_send.setText("Type here...");
        txt_send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_sendMouseClicked(evt);
            }
        });
        txt_send.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_sendKeyPressed(evt);
            }
        });

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        btn_reconnect.setText("Reconnect");
        btn_reconnect.setEnabled(false);
        btn_reconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reconnectActionPerformed(evt);
            }
        });

        lbl_chat.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        lbl_chat.setText("Broadcast");

        javax.swing.GroupLayout panel_chatLayout = new javax.swing.GroupLayout(panel_chat);
        panel_chat.setLayout(panel_chatLayout);
        panel_chatLayout.setHorizontalGroup(
            panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_chatLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_chatLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_chatLayout.createSequentialGroup()
                                .addComponent(txt_send, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_chatLayout.createSequentialGroup()
                        .addComponent(lbl_chat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_reconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_chatLayout.setVerticalGroup(
            panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_chatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_reconnect)
                    .addComponent(lbl_chat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_send, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panel_online.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

        list_online.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_onlineMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(list_online);

        lbl_online.setFont(new java.awt.Font("Arial", 2, 18)); // NOI18N
        lbl_online.setText("Online");

        javax.swing.GroupLayout panel_onlineLayout = new javax.swing.GroupLayout(panel_online);
        panel_online.setLayout(panel_onlineLayout);
        panel_onlineLayout.setHorizontalGroup(
            panel_onlineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_onlineLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_onlineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addGroup(panel_onlineLayout.createSequentialGroup()
                        .addComponent(lbl_online)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_onlineLayout.setVerticalGroup(
            panel_onlineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_onlineLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_online)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_online, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_online, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_chat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
     * Starts the client socket.
     */
    private void startClient() {
        client = new Client(IP, PORT, this);
        new Thread(client).start();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client " + client.getPort());
        append("Welcome to the chat room!\n");
        append("Type 'bye' to leave chat room.\n");
    }

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        String msg = txt_send.getText();
        txt_send.setText("");
        if (!msg.equals("")) {
            append("Me : " + msg + "\n");
            client.sendTo(msg, lbl_chat.getText());
        }
        if (msg.equals("bye")) {
            client.stop();
            btn_reconnect.setEnabled(true);
            return;
        }
    }//GEN-LAST:event_btn_sendActionPerformed

    private void txt_sendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_sendMouseClicked
        if (txt_send.getText().equals("Type here...")) {
            txt_send.setText("");
        }
    }//GEN-LAST:event_txt_sendMouseClicked

    private void txt_sendKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_sendKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String msg = txt_send.getText();
            txt_send.setText("");
            if (!msg.equals("")) {
                client.sendTo(msg, lbl_chat.getText());
                append("Me : " + msg + "\n");
            }
            if (msg.equals("bye")) {
                client.stop();
                btn_reconnect.setEnabled(true);
                return;
            }
        }
    }//GEN-LAST:event_txt_sendKeyPressed

    private void btn_reconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reconnectActionPerformed
        startClient();
        btn_reconnect.setEnabled(false);
        txt_send.setText("");
        txt_send.requestFocusInWindow();
    }//GEN-LAST:event_btn_reconnectActionPerformed

    private void list_onlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_onlineMouseClicked
        if (!list_online.isSelectionEmpty()) {
            lbl_chat.setText(list_online.getSelectedValue());
            txt_send.setText("");
            txt_send.requestFocusInWindow();
        }
    }//GEN-LAST:event_list_onlineMouseClicked

    /*
     * Appends messages to the client screen.
     */
    public void append(String message) {
        txtArea_chat.append(message);
    }

    /*
     * Runs a thread that continuously updates the contact
     * list on the client side.
     */
    public void updateOnlineClients(DefaultListModel<String> dlm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    DefaultListModel<String> tempDLM = new DefaultListModel<>();
                    tempDLM.addElement("Broadcast");
                    for (int i = 0; i < dlm.size(); i++) {
                        if (!dlm.elementAt(i).equals(client.getPort() + "")) {
                            tempDLM.addElement((String) dlm.elementAt(i));
                        }
                    }
                    list_online.setModel(tempDLM);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI_Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btn_reconnect;
    private javax.swing.JButton btn_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbl_chat;
    private javax.swing.JLabel lbl_online;
    private javax.swing.JList<String> list_online;
    private javax.swing.JPanel panel_chat;
    private javax.swing.JPanel panel_online;
    private javax.swing.JTextArea txtArea_chat;
    private javax.swing.JTextField txt_send;
    // End of variables declaration//GEN-END:variables

}
