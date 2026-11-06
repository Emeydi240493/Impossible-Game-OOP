/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * @author user
 */

import java.awt.Color;
import java.awt.Graphics;

public class Door extends GameObject {

    private boolean moving = false;
    private int originalX;
    private int targetX = 710; // Default boundary point for movements
    
    private Color doorColor = new Color(139, 69, 19); // Default: Brown
    private Color knobColor = Color.YELLOW;            // Default: Yellow knob

    
    public Door(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.originalX = x;
    }

  
    public Door(int x, int y, int width, int height, Color doorColor, Color knobColor) {
        super(x, y, width, height);
        this.originalX = x;
        this.doorColor = doorColor;
        this.knobColor = knobColor;
    }

    @Override
    public void tick() {
        if (moving && x < targetX) {
            x += 5;
        }
    }

    @Override
    public void render(Graphics g) {
       
        g.setColor(doorColor);
        g.fillRect(x, y, width, height);

        
        if (knobColor != null) {
            g.setColor(knobColor);
            g.fillRect(x + width - 10, y + (height / 2) - 3, 6, 8);
        }
    }

    
    public void triggerMove() {
        this.moving = true;
    }

    
    public void reset() {
        this.x = originalX;
        this.moving = false;
    }

   
    public boolean isMoving() { return moving; }
    public void setTargetX(int targetX) { this.targetX = targetX; }
}