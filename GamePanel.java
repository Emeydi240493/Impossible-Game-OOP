/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameobject;

/**
 *
 * Shaira Hamid - stats part, level 2 part
 */

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private Thread gameThread;
    private final int FPS = 60;

    // game new
    private static final int STATE_INTRO   = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_ENDING  = 2;
    private int gameState = STATE_INTRO;   // Game now starts on the intro screen

    private int frameCounter = 0;          // used for blinking text / animation
    private long startTime = 0;            // when the player pressed ENTER
    private long finishTime = 0;           // when the player beat Level 5

    private int currentLevel = 1;
    private int deaths = 0;
    private boolean levelCleared = false;
    private boolean gameFinished = false;

    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();
    private List<Spike> spikes = new ArrayList<>();

    private Player player;
    private Door door;

    private FallingPlatform l1MiddlePlat;
    private Spike l1HiddenSpike;

    private Platform l5MainLand, l5FloatPlat2, l5FloatPlat3;
    private Platform l5DisappearingPlat;
    private Spike l5Spike1, l5Spike2;
    private int l5PlatformTimer = 0;
    private boolean l5PlatVisible = true;

    private FallingPlatform l3MiddlePlat;
    private Platform l3TrollWall;
    private Spike l3HiddenSpike;
    private boolean l3TrollTriggered = false;

    private FallingPlatform l2MidPlat1, l2MidPlat2;
    private FallingSpike l2CeilingSpike1, l2CeilingSpike2, l2CeilingSpike3;
    private int l2DoorVelY = -6;

    private MovingPlatform l4MovingPlat;
    private Door l4FakeDoor, l4RealDoor;
    private FallingSpike l4BacktrackSpike1, l4BacktrackSpike2;
    private boolean l4FakeDoorTriggered = false;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(new Color(245, 222, 179));
        setFocusable(true);
        addKeyListener(this);
        loadLevel(1);
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void loadLevel(int level) {
        currentLevel = level;
        levelCleared = false;
        gameObjects.clear();
        platforms.clear();
        spikes.clear();

        switch (level) {
            case 1:
                setupLevel1(); // Normal jumping platforms
                break;
            case 2:
                setupLevel2(); // Disappearing Platform
                break;
            case 3:
                setupLevel3(); // Troll Wall & hidden teleport door
                break;
            case 4:
                setupLevel4(); // Ceiling Spikes
                break;
            case 5:
                setupLevel5(); // Fake Door Backtrack
                break;
        }
    }

    // new start

    private void startGame() {
        deaths = 0;
        gameFinished = false;
        l2DoorVelY = -6;
        loadLevel(1);
        startTime = System.currentTimeMillis();
        gameState = STATE_PLAYING;
    }

    private void finishGame() {
        gameFinished = true;
        finishTime = System.currentTimeMillis();
        player.stop();
        gameState = STATE_ENDING;
    }

    // LEVEL 1: NORMAL LEVEL
    private void setupLevel1() {
        player = new Player(60, 300, 24, 32);

        Platform leftPlat = new Platform(0, 380, 220, 120);
        l1MiddlePlat = new FallingPlatform(290, 320, 110, 30);
        Platform rightPlat = new Platform(470, 380, 330, 120);

        door = new Door(560, 310, 40, 70);
        l1HiddenSpike = new Spike(640, 350, 30, 30);

        platforms.add(leftPlat);
        platforms.add(l1MiddlePlat);
        platforms.add(rightPlat);

        gameObjects.add(leftPlat);
        gameObjects.add(l1MiddlePlat);
        gameObjects.add(rightPlat);
        gameObjects.add(door);
        gameObjects.add(l1HiddenSpike);
        gameObjects.add(player);
    }

    // LEVEL 2: DISAPPEARING PLATFORM
    private void setupLevel2() {
        player = new Player(50, 400, 24, 32);
        l5PlatVisible = true;
        l5PlatformTimer = 0;

        l5MainLand = new Platform(0, 450, 800, 50);
        l5DisappearingPlat = new Platform(160, 370, 130, 20);
        l5FloatPlat2 = new Platform(400, 310, 120, 20);
        l5FloatPlat3 = new Platform(580, 380, 100, 20);

        platforms.add(l5MainLand);
        platforms.add(l5DisappearingPlat);
        platforms.add(l5FloatPlat2);
        platforms.add(l5FloatPlat3);

        l5Spike1 = new Spike(340, 426, 48, 24);
        l5Spike2 = new Spike(510, 426, 48, 24);
        l5Spike1.setActive(false);
        l5Spike2.setActive(false);

        spikes.add(l5Spike1);
        spikes.add(l5Spike2);

        door = new Door(720, 370, 32, 80);

        gameObjects.add(l5MainLand);
        gameObjects.add(l5DisappearingPlat);
        gameObjects.add(l5FloatPlat2);
        gameObjects.add(l5FloatPlat3);
        gameObjects.add(l5Spike1);
        gameObjects.add(l5Spike2);
        gameObjects.add(door);
        gameObjects.add(player);
    }

    // LEVEL 3: TROLL WALL & TELEPORTING DOOR
    private void setupLevel3() {
        player = new Player(50, 320, 24, 32);

        Platform startPlat = new Platform(0, 400, 220, 500);
        l3MiddlePlat = new FallingPlatform(280, 400, 150, 30);
        Platform endPlat = new Platform(500, 400, 400, 400);
        l3TrollWall = new Platform(650, 420, 30, 120);

        platforms.add(startPlat);
        platforms.add(l3MiddlePlat);
        platforms.add(endPlat);
        platforms.add(l3TrollWall);

        l3HiddenSpike = new Spike(550, 350, 30, 30);
        l3HiddenSpike.setActive(false);
        spikes.add(l3HiddenSpike);

        door = new Door(700, 340, 40, 64);

        l3TrollTriggered = false;

        gameObjects.add(startPlat);
        gameObjects.add(l3MiddlePlat);
        gameObjects.add(endPlat);
        gameObjects.add(l3TrollWall);
        gameObjects.add(l3HiddenSpike);
        gameObjects.add(door);
        gameObjects.add(player);
    }

    //LEVEL 4: FALLING CEILING SPIKES & MOVING DOOR
    private void setupLevel4() {
        player = new Player(40, 300, 24, 32);

        Platform startPlat = new Platform(0, 380, 130, 120);
        l2MidPlat1 = new FallingPlatform(220, 340, 50, 25);
        l2MidPlat2 = new FallingPlatform(360, 290, 50, 25);
        Platform endPlat = new Platform(480, 380, 320, 120);

        platforms.add(startPlat);
        platforms.add(l2MidPlat1);
        platforms.add(l2MidPlat2);
        platforms.add(endPlat);

        for (int sx = 130; sx < 480; sx += 35) {
            Spike floorSpike = new Spike(sx, 465, 35, 35);
            floorSpike.setActive(true);
            spikes.add(floorSpike);
            gameObjects.add(floorSpike);
        }

        l2CeilingSpike1 = new FallingSpike(215, 0, 35, 40);
        l2CeilingSpike2 = new FallingSpike(355, 0, 35, 40);
        l2CeilingSpike3 = new FallingSpike(440, 0, 35, 40);

        door = new Door(620, 310, 40, 70);

        gameObjects.add(startPlat);
        gameObjects.add(l2MidPlat1);
        gameObjects.add(l2MidPlat2);
        gameObjects.add(endPlat);
        gameObjects.add(l2CeilingSpike1);
        gameObjects.add(l2CeilingSpike2);
        gameObjects.add(l2CeilingSpike3);
        gameObjects.add(door);
        gameObjects.add(player);
    }

    //LEVEL 5: MOVING PLATFORM & FAKE DOOR BACKTRACK
    private void setupLevel5() {
        l4FakeDoorTriggered = false;
        player = new Player(40, 300, 24, 32);

        Platform startPlat = new Platform(0, 380, 150, 120);
        l4MovingPlat = new MovingPlatform(160, 340, 90, 25, 160, 580, 3);
        Platform endPlat = new Platform(580, 380, 220, 120);

        platforms.add(startPlat);
        platforms.add(l4MovingPlat);
        platforms.add(endPlat);

        for (int sx = 150; sx < 580; sx += 35) {
            Spike floorSpike = new Spike(sx, 465, 35, 35);
            floorSpike.setActive(true);
            spikes.add(floorSpike);
            gameObjects.add(floorSpike);
        }

        l4FakeDoor = new Door(640, 310, 40, 70);
        l4RealDoor = new Door(30, 310, 40, 70);
        door = l4FakeDoor;

        l4BacktrackSpike1 = new FallingSpike(420, 0, 35, 40);
        l4BacktrackSpike2 = new FallingSpike(260, 0, 35, 40);

        gameObjects.add(startPlat);
        gameObjects.add(l4MovingPlat);
        gameObjects.add(endPlat);
        gameObjects.add(l4FakeDoor);
        gameObjects.add(l4BacktrackSpike1);
        gameObjects.add(l4BacktrackSpike2);
        gameObjects.add(player);
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        frameCounter++; // keeps intro/ending animations running

      
        if (gameState != STATE_PLAYING) return;
        if (levelCleared || gameFinished) return;

        player.updatePhysics(platforms);

        for (GameObject obj : gameObjects) {
            obj.tick();
        }

        
        
        
        switch (currentLevel) {
            case 1:
                if (player.getX() > 500) door.triggerMove();
                if (player.getX() > 580) l1HiddenSpike.setActive(true);
                if (l1HiddenSpike.isActive() && player.getBounds().intersects(l1HiddenSpike.getBounds())) playerDie();
                if (player.getBounds().intersects(door.getBounds())) advanceLevel();
                break;

            case 2: // Disappearing Platform
                if (l5PlatVisible && player.isOnGround() &&
                    player.getX() + player.getWidth() > l5DisappearingPlat.getX() &&
                    player.getX() < l5DisappearingPlat.getX() + l5DisappearingPlat.getWidth() &&
                    Math.abs((player.getY() + player.getHeight()) - l5DisappearingPlat.getY()) <= 3) {

                    l5PlatformTimer++;
                    if (l5PlatformTimer >= 30) {
                        l5PlatVisible = false;
                        platforms.remove(l5DisappearingPlat);
                        gameObjects.remove(l5DisappearingPlat);
                    }
                }
                if (player.getX() > 280) l5Spike1.setActive(true);
                if (player.getX() > 455) l5Spike2.setActive(true);

                if ((l5Spike1.isActive() && player.getBounds().intersects(l5Spike1.getBounds())) ||
                    (l5Spike2.isActive() && player.getBounds().intersects(l5Spike2.getBounds()))) {
                    playerDie();
                }
                if (player.getBounds().intersects(door.getBounds())) advanceLevel();
                break;

            case 3: // Troll Wall
                if (player.getX() > 480) l3HiddenSpike.setActive(true);

                if (player.getX() > 550 && !l3TrollTriggered) {
                    l3TrollWall.setY(300);
                    door.setX(20);
                    door.setY(340);
                    l3MiddlePlat.reset();
                    l3TrollTriggered = true;
                }
                if (l3HiddenSpike.isActive() && player.getBounds().intersects(l3HiddenSpike.getBounds())) playerDie();
                if (player.getBounds().intersects(door.getBounds())) advanceLevel();
                break;

            case 4: // Ceiling Spikes
                if (player.getX() > 160) l2CeilingSpike1.triggerFall();
                if (player.getX() > 300) l2CeilingSpike2.triggerFall();
                if (player.getX() > 390) l2CeilingSpike3.triggerFall();

                if (player.getX() > 490) {
                    door.setY(door.getY() + l2DoorVelY);
                    if (door.getY() <= 200) l2DoorVelY = 6;
                    if (door.getY() >= 320) l2DoorVelY = -6;
                }

                if (player.getBounds().intersects(l2CeilingSpike1.getBounds()) ||
                    player.getBounds().intersects(l2CeilingSpike2.getBounds()) ||
                    player.getBounds().intersects(l2CeilingSpike3.getBounds())) {
                    playerDie();
                }
                if (player.getBounds().intersects(door.getBounds())) advanceLevel();
                break;

            case 5: // Fake Door Backtrack
                if (!l4FakeDoorTriggered && player.getBounds().intersects(l4FakeDoor.getBounds())) {
                    l4FakeDoorTriggered = true;
                    gameObjects.remove(l4FakeDoor);
                    gameObjects.add(l4RealDoor);
                }
                if (l4FakeDoorTriggered) {
                    if (player.getX() < 500) l4BacktrackSpike1.triggerFall();
                    if (player.getX() < 340) l4BacktrackSpike2.triggerFall();
                }
                if (player.getBounds().intersects(l4BacktrackSpike1.getBounds()) ||
                    player.getBounds().intersects(l4BacktrackSpike2.getBounds())) {
                    playerDie();
                }
                if (l4FakeDoorTriggered && player.getBounds().intersects(l4RealDoor.getBounds())) {
                    finishGame(); // goes to the ENDING screen
                }
                break;
        }
        
        
        

        for (Spike s : spikes) {
            if (s.isActive() && player.getBounds().intersects(s.getBounds())) {
                playerDie();
                return;
            }
        }

        if (player.getY() > 500) {
            playerDie();
        }
    }

    private void advanceLevel() {
        if (currentLevel < 5) {
            loadLevel(currentLevel + 1);
        } else {
            finishGame(); //goes to the ENDING screen
        }
    }

    private void playerDie() {
        deaths++;
        loadLevel(currentLevel);
    }

    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (gameState == STATE_INTRO) {
            drawIntro(g2);
            return;
        }
        if (gameState == STATE_ENDING) {
            drawEnding(g2);
            return;
        }

       
        for (GameObject obj : gameObjects) {
            obj.render(g);
        }

        g.setColor(new Color(40, 40, 40));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("LEVEL " + currentLevel, 30, 45);
        g.drawString("DEATHS: " + deaths, 630, 45);

        if (currentLevel == 5 && l4FakeDoorTriggered && !gameFinished) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("FAKE DOOR! GO BACK TO START!", 230, 100);
        }
    }

    // Draws text centered horizontally on the 800px wide screen
    private void drawCentered(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }

    // intro
    private void drawIntro(Graphics2D g2) {
        // dark background
        g2.setColor(new Color(30, 25, 35));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // decorative ground + spikes along the bottom
        g2.setColor(new Color(105, 105, 105));
        g2.fillRect(0, 440, getWidth(), 60);
        g2.setColor(Color.RED);
        for (int sx = 0; sx < getWidth(); sx += 40) {
            int[] xs = {sx, sx + 20, sx + 40};
            int[] ys = {440, 410, 440};
            g2.fillPolygon(xs, ys, 3);
        }

        int bob = (int) (Math.sin(frameCounter / 12.0) * 6);
        int px = 90, py = 378 + bob;
        g2.setColor(Color.WHITE);
        g2.fillRect(px, py, 24, 32);
        g2.setColor(Color.BLACK);
        g2.fillRect(px + 24 - 8, py + 6, 5, 8);

        // a suspicious door waiting at the other side
        g2.setColor(new Color(139, 69, 19));
        g2.fillRect(690, 370, 40, 70);
        g2.setColor(Color.YELLOW);
        g2.fillRect(690 + 40 - 10, 370 + 35 - 3, 6, 8);

        // title
        g2.setFont(new Font("Arial", Font.BOLD, 56));
        g2.setColor(new Color(0, 0, 0, 120));
        drawCentered(g2, "IMPOSSIBLE GAME", 112);   // shadow
        g2.setColor(new Color(245, 222, 179));
        drawCentered(g2, "IMPOSSIBLE GAME", 108);

        g2.setFont(new Font("Arial", Font.ITALIC, 20));
        g2.setColor(new Color(200, 80, 80));
        drawCentered(g2, "Levels 1 - 5", 140);

        // story
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        drawCentered(g2, "You wake up in a strange place with only one way out: the door.", 190);
        drawCentered(g2, "Easy, right? Just walk to it.", 216);
        g2.setColor(new Color(255, 120, 120));
        drawCentered(g2, "But nothing here is what it seems. Platforms fall, spikes appear,", 250);
        drawCentered(g2, "and even the doors can lie to you.", 276);

        // controls
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(245, 222, 179));
        drawCentered(g2, "CONTROLS", 318);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(Color.WHITE);
        drawCentered(g2, "A / D or Arrow Keys - Move     |     W / SPACE / Up - Jump     |     R - Restart level", 342);

        // blinking prompt
        if ((frameCounter / 30) % 2 == 0) {
            g2.setFont(new Font("Arial", Font.BOLD, 26));
            g2.setColor(new Color(120, 220, 120));
            drawCentered(g2, "Press ENTER to start", 395);
        }
    }

    // end
    private void drawEnding(Graphics2D g2) {
        // dark background
        g2.setColor(new Color(20, 30, 25));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // falling confetti 
        Color[] confettiColors = {
            new Color(255, 90, 90), new Color(255, 220, 90),
            new Color(110, 220, 130), new Color(110, 170, 255), Color.WHITE
        };
        for (int i = 0; i < 60; i++) {
            int cx = (i * 97) % getWidth();
            int cy = (int) ((frameCounter * (1.5 + (i % 4) * 0.7) + i * 53) % (getHeight() + 20)) - 10;
            g2.setColor(confettiColors[i % confettiColors.length]);
            g2.fillRect(cx, cy, 6, 10);
        }

        // title
        g2.setFont(new Font("Arial", Font.BOLD, 52));
        g2.setColor(new Color(0, 0, 0, 120));
        drawCentered(g2, "YOU ESCAPED!", 112);
        g2.setColor(new Color(120, 230, 130));
        drawCentered(g2, "YOU ESCAPED!", 108);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        drawCentered(g2, "You beat all 5 levels of the Impossible Game!", 155);

        // happy player + the real door (open, bright green)
        g2.setColor(new Color(60, 160, 80));
        g2.fillRect(330, 190, 40, 70);
        g2.setColor(Color.YELLOW);
        g2.fillRect(330 + 40 - 10, 190 + 35 - 3, 6, 8);
        int hop = (int) Math.abs(Math.sin(frameCounter / 10.0) * 18);
        g2.setColor(Color.WHITE);
        g2.fillRect(410, 228 - hop, 24, 32);
        g2.setColor(Color.BLACK);
        g2.fillRect(410 + 24 - 8, 228 - hop + 6, 5, 8);
        g2.setColor(new Color(105, 105, 105));
        g2.fillRect(300, 260, 200, 8);

        // stats
        long totalSeconds = Math.max(0, (finishTime - startTime) / 1000);
        String time = String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(new Color(245, 222, 179));
        drawCentered(g2, "Total Deaths: " + deaths + "        Time: " + time, 315);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(255, 220, 90));
        drawCentered(g2, "Rank: " + getRank(), 352);

        g2.setFont(new Font("Arial", Font.ITALIC, 16));
        g2.setColor(new Color(200, 200, 200));
        drawCentered(g2, "Thanks for playing! Made by <Your Team Name>", 385);

        // blinking prompt
        if ((frameCounter / 30) % 2 == 0) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(120, 220, 120));
            drawCentered(g2, "ENTER - Play Again        ESC - Quit", 440);
        }
    }

    private String getRank() {
        if (deaths <= 5)  return "IMPOSSIBLE? NOT FOR YOU!";
        if (deaths <= 20) return "Seasoned Survivor";
        if (deaths <= 50) return "Stubborn Hero";
        return "Never Gives Up";
    }

   

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //  INTRO: press ENTER (or SPACE) to start 
        if (gameState == STATE_INTRO) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                startGame();
            }
            return;
        }

        // ENDING: ENTER = play again, ESC = quit 
        if (gameState == STATE_ENDING) {
            if (code == KeyEvent.VK_ENTER) {
                startGame();
            } else if (code == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            return;
        }

        //  PLAYING (your original controls) 
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) player.moveLeft();
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) player.moveRight();
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) player.jump();
        if (code == KeyEvent.VK_R) loadLevel(currentLevel);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameState != STATE_PLAYING) return;

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT ||
            code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            player.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}



