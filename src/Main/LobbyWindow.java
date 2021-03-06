/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Remote.LobbyServer;
import Remote.ServerInfo;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author efren
 */
public class LobbyWindow extends javax.swing.JFrame {

    /**
     * Creates new form LobbyWindow
     */
    public LobbyWindow() {
        initComponents();
        running = false;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lobbyTag = new javax.swing.JLabel();
        startButton = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        serverTable = new javax.swing.JTable();
        updateTable = new javax.swing.JButton();
        portLabel = new javax.swing.JLabel();
        portField = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lobbyTag.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lobbyTag.setText("Lobby");

        startButton.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        startButton.setText("Start");
        startButton.setToolTipText("Starts the lobby server");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });

        serverTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        serverTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Server", "Max Players", "IP", "Port"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(serverTable);

        updateTable.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        updateTable.setText("Update Table");
        updateTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateTableMouseClicked(evt);
            }
        });
        updateTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTableActionPerformed(evt);
            }
        });

        portLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        portLabel.setText("Port (Must be open)");

        portField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        portField.setText("2004");
        portField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lobbyTag)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(portLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(portField))
                        .addGap(18, 18, 18)
                        .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(portLabel)
                    .addComponent(startButton)
                    .addComponent(lobbyTag)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(portField)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateTable, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateTableActionPerformed

    private void updateTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateTableMouseClicked
        updateServerTable();
    }//GEN-LAST:event_updateTableMouseClicked

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        if (running) {
            running = false;
            System.out.println("AA");
        } else {
            running = true;
            lobby = new LobbyServer(running, Integer.parseInt(portField.getText()));
            lobby.run();
            System.out.println("AAAAA");

            Thread update = new Thread(() -> {
                while (running) {
                    updateServerTable();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LobbyWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            update.start();
        }
    }//GEN-LAST:event_startButtonMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        running = false;
    }//GEN-LAST:event_formWindowClosing

    private void updateServerTable() {
        DefaultTableModel table = (DefaultTableModel) serverTable.getModel();
        table.setRowCount(0);
        if (lobby.getServers() != null) {
            for (ServerInfo server : lobby.getServers()) {
                table.addRow(new Object[]{server.getName(), server.getMaxPlayers(), server.getIp(), server.getPort()});
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LobbyWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LobbyWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LobbyWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LobbyWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LobbyWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lobbyTag;
    private javax.swing.JFormattedTextField portField;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTable serverTable;
    private javax.swing.JToggleButton startButton;
    private javax.swing.JButton updateTable;
    // End of variables declaration//GEN-END:variables
    private boolean running;
    private LobbyServer lobby;
}
