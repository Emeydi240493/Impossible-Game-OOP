/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * @author user
 */
//For Platform
//Platform
import java.awt.Color;
import java.awt.Graphics;

public class Platform extends GameObject {

    public Platform(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void tick() {
        // Static platform 
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(105, 105, 105)); // Gray color
        g.fillRect(x, y, width, height);
    }
}