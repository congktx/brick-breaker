package controller;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Board extends JPanel {
    Timer timer;
    Dimension screenSize = new Dimension(960, 540);
    Dimension cursorLocation = new Dimension(0, 0);

    Player player = new Player();

    Ball[] balls = new Ball[3];
    int numberBall = 0;

    Brick[] bricks = new Brick[200];
    int numberBrick = 0;

    Item[] items = new Item[8];
    int numberItem = 0;

    int haveGun = 0;
    int lives = 3;
    int loopMusic = -1;
    int isPause = 1;

    Image backgroundImage, playerImage, ballImage, gunImage, bulletImage;
    ArrayList<Image> brickImage = new ArrayList<Image>();
    ArrayList<Image> itemImage = new ArrayList<Image>();

    Clip music;

    public Board() {
        setFocusable(true);
        setDoubleBuffered(true);

        addKeyListener(new TKeyboardAdapter());
        addMouseListener(new TMouseAdapter());
        addMouseMotionListener(new TMouseMotionAdapter());
        
        getImages();
        getMusic();
        setUpMap1();
        reset();

        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 0, 12);
    }

    public void reset() {
        Ball ball = new Ball();
        ball.x = 480;
        ball.y = 493;
        ball.dx = 0;
        ball.dy = -6.32455532;
        balls[0] = ball;
        numberBall = 1;

        player.location.width = 421;
        player.location.height = 507;

        isPause = 1;
    }

    // move objects
    public void ballMove() {
        for (int i=0; i<numberBall; i++) {
            balls[i].x += balls[i].dx;
            balls[i].y += balls[i].dy;
        }
    }

    public void playerMove() {
        player.location.width = Math.min(cursorLocation.width, screenSize.width - player.size.width - 10);
    }

    public void itemMove() {
        for (int i=0; i<numberItem; i++) {
            items[i].location.height += 2;
        }
    }

    public void moveAll() {
        playerMove();
        ballMove();
        itemMove();
    }

    // check contact
    public void BallOut() {
        for (int i=numberBall-1; i>=0; i--) {
            if (balls[i].y - balls[i].radius >= screenSize.height) {
                balls[i] = balls[numberBall - 1];
                numberBall--;
            }
        }
    }

    public void BallWall() {
        for (int i=0; i<numberBall; i++) {
            if (balls[i].x - balls[i].radius <= 0) {
                balls[i].dx = Math.abs(balls[i].dx);
            }

            if (balls[i].x + balls[i].radius >= screenSize.width) {
                balls[i].dx = - Math.abs(balls[i].dx);
            }

            if (balls[i].y - balls[i].radius <= 30) {
                balls[i].dy = Math.abs(balls[i].dy);
            }
        }
    }

    public void BallPlayer() {
        for (int i=0; i<numberBall; i++) {
            if (balls[i].y + balls[i].radius < player.location.height) continue;
            if (balls[i].x + balls[i].radius < player.location.width) continue;
            if (balls[i].x - balls[i].radius > player.location.width + player.size.width) continue;

            if (balls[i].y - player.location.height > 18) {
                if (balls[i].x > player.location.width + player.size.width) {
                    balls[i].dx = 6;
                    balls[i].dy = 2;
                }
                if (balls[i].x < player.location.width) {
                    balls[i].dx = -6;
                    balls[i].dy = 2;
                }
            } else {
                double change = Math.min(player.location.width + player.size.width, Math.max(player.location.width, balls[i].x)) - player.location.width;
                change = ((change / player.size.width) - 0.5) * 2;
                change *= 6;
                balls[i].dx = change;
                balls[i].dy = - Math.sqrt(40 - change * change);
            }
        }
    }

    public void BallBrick() {
        for (int i=0; i<numberBall; i++) 
        for (int j=numberBrick - 1; j>=0; j--) {
            if (balls[i].x + balls[i].radius < bricks[j].location.width) continue;
            if (balls[i].x - balls[i].radius > bricks[j].location.width + bricks[j].size.width) continue;
            if (balls[i].y + balls[i].radius < bricks[j].location.height) continue;
            if (balls[i].y - balls[i].radius > bricks[j].location.height + bricks[j].size.height) continue;

            if (balls[i].x < bricks[j].location.width) {
                balls[i].dx = - Math.abs(balls[i].dx);
            }
            if (balls[i].x > bricks[j].location.width + bricks[j].size.width) {
                balls[i].dx = Math.abs(balls[i].dx);
            }
            if (balls[i].y < bricks[j].location.height) {
                balls[i].dy = - Math.abs(balls[i].dy);
            }
            if (balls[i].y > bricks[j].location.height + bricks[j].size.height) {
                balls[i].dy = Math.abs(balls[i].dy);
            }

            bricks[j].hp--;
            if (bricks[j].hp == 0) {
                if (bricks[j].item != 0) {
                    items[numberItem++] = new Item();
                    items[numberItem-1].location.width = bricks[j].location.width + bricks[j].size.width / 2;
                    items[numberItem-1].location.height = bricks[j].location.height + bricks[j].size.height / 2;
                    items[numberItem-1].type = bricks[j].item;
                }
                bricks[j] = bricks[numberBrick - 1];
                numberBrick--;
            }
        }
    }

    public void ItemPlayer() {
        for (int i=numberItem - 1; i>=0; i--) {
            if (items[i].location.width + items[i].size.width < player.location.width) continue;
            if (items[i].location.width > player.location.width + player.size.width) continue;
            if (items[i].location.height + items[i].size.height < player.location.height) continue;
            if (items[i].location.height > player.location.height + player.size.height) continue;
            if (items[i].type == 4) {
                haveGun = 1;
            }
            items[i].type = 0;
            // items[i] = items[numberItem - 1];
            // numberItem--;
            // System.out.println(numberItem);
        }
    }

    public void checkContact() {
        if (numberBall == 0) {
            reset();
            lives--;
            isPause = 1;
            return;
        }
        BallOut();
        BallWall();
        BallPlayer();
        BallBrick();
        ItemPlayer();
    }

    // paint all objects
    public void paintBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
    }

    public void paintPlayer(Graphics g) {
        g.drawImage(playerImage, player.location.width, player.location.height, this);
    }

    public void paintBall(Graphics g) {
        for (int i=0; i<numberBall; i++) {
            g.drawImage(ballImage, (int)balls[i].x - balls[i].radius, (int)balls[i].y - balls[i].radius, this);
        }
    }

    public void paintBricks(Graphics g) {
        for (int i=0; i<numberBrick; i++) 
            if (bricks[i].hp > 0) {
                Brick br = bricks[i];
                int index = (br.color - 1) * 2 + (br.hp - 1); 
                g.drawImage(brickImage.get(index), br.location.width, br.location.height, this);
            }
    }

    public void paintItems(Graphics g) {
        for (int i=0; i<numberItem; i++) if (items[i].type != 0) {
            Item it = items[i];
            g.drawImage(itemImage.get(it.type - 1), it.location.width, it.location.height, this);
        }
    }

    public void paintAll(Graphics g) {
        paintBackground(g);
        paintPlayer(g);
        paintBall(g);
        paintBricks(g);
        paintItems(g);
    }

    // game on
    public void paint(Graphics g) {

        // if (isPause == -1) moveAll();

        // checkContact();

        paintAll(g);

        if (loopMusic == 1 && music != null) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    class ScheduleTask extends TimerTask {

        public void run() {

            if (isPause == -1) moveAll();
            checkContact();
            repaint();

        }
    }

    // listener
    class TKeyboardAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                isPause = -isPause;
            }
        }
    }
    class TMouseAdapter extends MouseAdapter {
        public void mouseClicked() {
        }
    }
    class TMouseMotionAdapter extends MouseAdapter {
        public void mouseMoved(MouseEvent e) {
            cursorLocation.width = e.getX();
            cursorLocation.height = e.getY();
        }
    }

    // setup
    public void getImages() {
        backgroundImage = new ImageIcon("image/background.png").getImage();
        playerImage = new ImageIcon("image/player.png").getImage();
        ballImage = new ImageIcon("image/ball.png").getImage();
        for (int i=1; i<=5; i++)
        for (int j=1; j<=2; j++) {
            brickImage.add(new ImageIcon("image/brick"+i+"_"+j+".png").getImage());
        }
        for (int i=1; i<=4; i++) {
            itemImage.add(new ImageIcon("image/item"+i+".png").getImage());
        }
    }

    public void getMusic() {
        try {
            File file = new File("sound/sound.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            music = AudioSystem.getClip();
            music.open(audioStream);
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

    public void setUpMap1() {

        int initHeight = 80, indexBrick = 0;

        for (int i=1; i<=14; i++) {
            Brick br1 = new Brick();
            br1.location.width = 60 + br1.size.width * (i-1);
            br1.location.height = initHeight;
            br1.color = (int)(Math.random() * 4) + 1;
            bricks[indexBrick++]=br1;

            Brick br3 = new Brick();
            br3.location.width = 60 + br3.size.width * (i-1);
            br3.location.height = initHeight + br3.size.height * 2;
            br3.color = (int)(Math.random() * 4) + 1;
            bricks[indexBrick++]=br3;

            Brick br5 = new Brick();
            br5.location.width = 60 + br5.size.width * (i-1);
            br5.location.height = initHeight + br5.size.height * 4;
            br5.color = (int)(Math.random() * 4) + 1;
            bricks[indexBrick++]=br5;
        }

        for (int i=1; i<=13; i++) {
            Brick br2 = new Brick();
            br2.location.width = 90 + br2.size.width * (i-1);
            br2.location.height = initHeight + br2.size.height * 1;
            br2.color = (int)(Math.random() * 4) + 1;
            bricks[indexBrick++]=br2;

            Brick br4 = new Brick();
            br4.location.width = 90 + br4.size.width * (i-1);
            br4.location.height = initHeight + br4.size.height * 3;
            br4.color = (int)(Math.random() * 4) + 1;
            bricks[indexBrick++]=br4;
        }

        numberBrick = indexBrick;

        for (int i=1; i<=4; i++) 
        for (int j=1; j<=2; j++) {
            while (true) {
                indexBrick = (int)Math.floor(Math.random() * (numberBrick - 1));
                if (bricks[indexBrick].item == 0) break;
            }
            bricks[indexBrick].item = i;
        }
    }
}