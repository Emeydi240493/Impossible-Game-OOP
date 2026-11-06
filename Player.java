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
import java.util.List;

public class Player extends GameObject {

    private double velX = 0;
    private double velY = 0;
    private double exactX;
    private double exactY;
    private boolean onGround = false;

    private final double GRAVITY = 0.5;
    private final double JUMP_STRENGTH = -10;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.exactX = x;
        this.exactY = y;
    }

   
    public void updateMovement(KeyHandler keyH) {
        if (keyH.leftPressed) {
            moveLeft();
        } else if (keyH.rightPressed) {
            moveRight();
        } else {
            stop();
        }

        if (keyH.jumpPressed) {
            jump();
        }
    }

    @Override
    public void tick() {
        
    }

   
    public void updatePhysics(List<Platform> platforms) {
        velY += GRAVITY;

       
        exactX += velX;
        this.x = (int) Math.round(exactX);

        for (Platform p : platforms) {
            if (getBounds().intersects(p.getBounds())) {
                if (velX > 0) {
                    exactX = p.getX() - width;
                } else if (velX < 0) {
                    exactX = p.getX() + p.getWidth();
                }
                this.x = (int) exactX;
                velX = 0;
            }
        }

       
        exactY += velY;
        this.y = (int) Math.round(exactY);
        onGround = false;

        for (Platform p : platforms) {
            if (getBounds().intersects(p.getBounds())) {
                if (velY > 0) {
                    exactY = p.getY() - height;
                    this.y = (int) exactY;
                    velY = 0;
                    onGround = true;

                    
                    if (p instanceof FallingPlatform) {
                        ((FallingPlatform) p).triggerFall();
                    }

                    // Sasabay ang player sa galaw ng MovingPlatform
                    if (p instanceof MovingPlatform) {
                        exactX += ((MovingPlatform) p).getMoveVelocity();
                        this.x = (int) Math.round(exactX);
                    }
                } else if (velY < 0) { 
                    exactY = p.getY() + p.getHeight();
                    this.y = (int) exactY;
                    velY = 0;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        // Bdy of the player
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        // eye of the player
        g.setColor(Color.BLACK);
        g.fillRect(x + width - 8, y + 6, 5, 8);
    }

    public void moveLeft() { velX = -4; }
    public void moveRight() { velX = 4; }
    public void stop() { velX = 0; }

    public void jump() {
        if (onGround) {
            velY = JUMP_STRENGTH;
            onGround = false;
        }
    }

    public void resetPosition(int startX, int startY) {
        this.exactX = startX;
        this.exactY = startY;
        this.x = startX;
        this.y = startY;
        this.velX = 0;
        this.velY = 0;
        this.onGround = false;
    }

   
    public double getVelY() { return velY; }
    public void setVelY(double velY) { this.velY = velY; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
}