/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * @author user
 */
// Spike

import java.awt.Color;
import java.awt.Graphics;

public class Spike extends GameObject {

    private boolean active = false;

    // Constructor
    public Spike(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.active = false;
    }

    
    public Spike(int x, int y, int width, int height, boolean active) {
        super(x, y, width, height);
        this.active = active;
    }

    @Override
    public void tick() {
        // Static hazard 
    }

    @Override
    public void render(Graphics g) {
        if (!active) return; 

        g.setColor(Color.RED);
        int[] xPoints = {x, x + (width / 2), x + width};
        int[] yPoints = {y + height, y, y + height};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

   
    public boolean isVisible() { return isActive(); }
    public void setVisible(boolean visible) { setActive(visible); }
}