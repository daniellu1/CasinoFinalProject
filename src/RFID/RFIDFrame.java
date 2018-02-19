package RFID;

import Casino.CasinoFrame;
import Players.Player;
import Players.PlayerList;
import com.phidgets.*;
import com.phidgets.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class RFIDFrame extends javax.swing.JFrame implements RFIDable {

    RFIDPhidget rfid;
    SimulateCardReadFrame scrf;
    boolean ListenStatus =true ;
    Player player;
    
    
    public RFIDFrame() throws Exception {
       
        initComponents();     
        scrf = new SimulateCardReadFrame(this);
        scrf.setVisible(true);

       //write a new file
    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Activate = new java.awt.Button();
        Deactivate = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Activate.setLabel("Activate");
        Activate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ActivateMouseClicked(evt);
            }
        });
        Activate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActivateActionPerformed(evt);
            }
        });

        Deactivate.setLabel("Deactivate");
        Deactivate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DeactivateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Activate, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Deactivate, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Deactivate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Activate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(556, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ActivateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ActivateMouseClicked
        // TODO add your handling code here:
        try{
            setUpThePhidgetThing();
        }catch(Exception e) {}
        
    }//GEN-LAST:event_ActivateMouseClicked

    
    private void DeactivateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeactivateMouseClicked
        // TODO add your handling code here:
        System.out.print("closing...");
        try{rfid.close();} catch(Exception e) { } 
        rfid = null;
    }//GEN-LAST:event_DeactivateMouseClicked

     
    private void ActivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActivateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ActivateActionPerformed

    /**
     * @param args the command line arguments
     */
   public void setUpThePhidgetThing() throws Exception { 
        rfid = new RFIDPhidget();
        rfid.addAttachListener((AttachEvent ae) -> {
            try
            {
                ((RFIDPhidget)ae.getSource()).setAntennaOn(true);
                ((RFIDPhidget)ae.getSource()).setLEDOn(true);
            }
            catch (PhidgetException ex) { }
            System.out.println("attachment of " + ae);
        });


        rfid.addTagGainListener((TagGainEvent oe) -> {
            System.out.println("tagGained Event: " + oe);
            String cardid = oe.getValue();
            
            handleRead(cardid);
        });
        rfid.addTagLossListener((TagLossEvent oe) -> {
            System.out.println("tagLoss Event: " + oe);
            String cardid = oe.getValue();
            handleLoss(cardid);
        });
        
        rfid.addErrorListener((ErrorEvent ee) -> {
            System.out.println("error event for " + ee);
        });
   

        rfid.openAny();
        System.out.println("waiting for RFID attachment...");
        rfid.waitForAttachment(1000);

        System.out.println("Serial: " + rfid.getSerialNumber());
        System.out.println("Outputs: " + rfid.getOutputCount());

        System.out.println("Outputting events.  Input to stop.");


   }

   
    @Override
   public void handleRead(String id){
       System.out.println("handleRead: " + id);
       boolean existingPlayer = false; 
       if (PlayerList.playerList.size()>0){
            for (Player players : PlayerList.playerList){
                if (players.getId().equals(id)){
                    existingPlayer = true; 
                    player = players;
                    break;
                }
            }
            if (!existingPlayer){
                player = new Player(id); 
                PlayerList.playerList.add(player);
            }
       }
       else{
           player = new Player(id); 
           PlayerList.playerList.add(player);
       }   
       CasinoFrame cf = new CasinoFrame(id); 
       cf.setVisible(true); 
   }

   
    @Override
    public void handleLoss(String id){
       System.out.println("handleLoss: " + id );
       for (Player players : PlayerList.playerList)
           if (players.getId().equals(id)){
               PlayerList.playerList.remove(players); 
               break;
           }
   } 
    
   
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run()  {
              
                try {
                    System.out.println("Making frame...");
                    new RFIDFrame().setVisible(true);
                }
                catch(Exception e) {
                    System.out.println("Error Encountered!");
                    System.exit(0);
                }
            }
        }
                        );
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button Activate;
    private java.awt.Button Deactivate;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables



}

