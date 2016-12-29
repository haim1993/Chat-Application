/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

/**
 *
 * @author Shlez
 */
public class Server implements Runnable {
    
    static boolean ALIVE = false;
    
    @Override
    public void run() {
        while (ALIVE) {            
            GUI_Server.communicate();
        }
    }

}
