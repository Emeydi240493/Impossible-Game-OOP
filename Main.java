/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * @author user
 */

//ForMainCLASS

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

           
            JFrame window = new JFrame();

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Impossible Game (Levels 1 - 5)");

           
            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            
           
            window.pack();
            
           
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            
            gamePanel.startGameThread();

        });

    }

}