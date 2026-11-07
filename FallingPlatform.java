/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * shaira hamid part
 */

import java.awt.Color;
import java.awt.Graphics;

public class FallingPlatform extends Platform {

    protected boolean falling = false;
    protected int originalY;
    private static final int FALL_SPEED = 8;

    public FallingPlatform(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.originalY = y;
    }

    @Override
    public void tick() {
        if (falling) {
            y += FALL_SPEED; 
        }
    }

    @Override
    public void render(Graphics g) {
        
        g.setColor(new Color(80, 80, 80));
        g.fillRect(x, y, width, height);
    }

    public void triggerFall() {
        this.falling = true;
    }

   
    public void reset() {
        this.y = originalY;
        this.falling = false;
    }

    // Getter method
    public boolean isFalling() {
        return falling;
    }
}