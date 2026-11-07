/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * @author user
 */
//ClassFallingSpike



import java.awt.Color;
import java.awt.Graphics;

public class FallingSpike extends GameObject {

    private boolean falling = false;
    private double velY = 0;
    private int originalY;

    public FallingSpike(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.originalY = y;
    }

    @Override
    public void tick() {
        if (falling) {
            velY += 0.8; // Acceleration / gravity effect down 
           y += velY;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
       
        int[] xPoints = {x, x + (width / 2), x + width};
        int[] yPoints = {y, y + height, y};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public void triggerFall() {
        this.falling = true;
    }

    public void reset() {
        this.y = originalY;
        this.velY = 0;
        this.falling = false;
    }

    public boolean isFalling() {
        return falling;
    }
}