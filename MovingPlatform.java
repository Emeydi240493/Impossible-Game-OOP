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

public class MovingPlatform extends Platform {

    private int minX, maxX;
    private int speed;
    private int direction = 1;
    private int originalX;

    public MovingPlatform(int x, int y, int width, int height, int minX, int maxX, int speed) {
        super(x, y, width, height);
        this.minX = minX;
        this.maxX = maxX;
        this.speed = speed;
        this.originalX = x;
    }

    @Override
    public void tick() {
        x += speed * direction;

      
        if (x <= minX) {
            x = minX;
            direction = 1;
        } else if (x + width >= maxX) {
            x = maxX - width;
            direction = -1;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(70, 130, 180)); // Steel Blue
        g.fillRect(x, y, width, height);
    }

    
    public void reset() {
        this.x = originalX;
        this.direction = 1;
    }

  
    public int getMoveVelocity() {
        return speed * direction;
    }
}