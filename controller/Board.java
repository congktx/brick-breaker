package controller;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Board extends JFrame implements ActionListener {
    Timer timer;

    Player player = new Player();
    Ball ball = new Ball();

    Dimension screenSize = new Dimension(960, 540);
    Dimension cursorLocation = new Dimension(0, 0);
    Dimension[][] vectorBall = {
            {
                    new Dimension(-6, -2),
                    new Dimension(-5, -3),
                    new Dimension(-4, -4),
                    new Dimension(-3, -5),
                    new Dimension(-2, -6),
                    new Dimension(0, -5),
                    new Dimension(2, -6),
                    new Dimension(3, -5),
                    new Dimension(4, -4),
                    new Dimension(5, -3),
                    new Dimension(6, -2),
            },
            {
                    new Dimension(-6, 2),
                    new Dimension(-5, 3),
                    new Dimension(-4, 4),
                    new Dimension(-3, 5),
                    new Dimension(-2, 6),
                    new Dimension(0, 5),
                    new Dimension(2, 6),
                    new Dimension(3, 5),
                    new Dimension(4, 4),
                    new Dimension(5, 3),
                    new Dimension(6, 2),
            }
    };
    Dimension vectorIndex = new Dimension(0,4);

    Dimension lastLocationPlayer = new Dimension(0, 0);

    int maxSpeed = 0;

    int lastTimeMove = 0;

    int loopMusic = -1;
    int isPause = -1;

    Image backgroundImage, playerImage, ballImage;
    Clip music;

    public Board() {
        getImages();

        setTitle("Brick Breaker");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(screenSize.width, screenSize.height, screenSize.width, screenSize.height);

        timer = new Timer(10, this);
        timer.start();

        try {
            File file = new File("sound/sound.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            music = AudioSystem.getClip();
            music.open(audioStream);
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }

        
        ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];

        addKeyListener(new TKeyboardAdapter());
        addMouseListener(new TMouseAdapter());
        addMouseMotionListener(new TMouseMotionAdapter());
    }

    public boolean BallWall() {
        if (ball.location.width - ball.radius <= 0) {
            ball.location.width = ball.radius;
            vectorIndex.height = 10 - vectorIndex.height;
            ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];
            return true;
        }

        if (ball.location.width + ball.radius >= screenSize.width) {
            ball.location.width = screenSize.width - ball.radius;
            vectorIndex.height = 10 - vectorIndex.height;
            ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];
            return true;
        }

        if (ball.location.height - ball.radius <= 30) {
            ball.location.height = 30 + ball.radius;
            vectorIndex.width = 1 - vectorIndex.width;
            ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];
            return true;
        }

        if (ball.location.height + ball.radius >= screenSize.height) {
            ball.location.height = screenSize.height - ball.radius;
            vectorIndex.width = 1 - vectorIndex.width;
            ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];
            return true;
        }

        return false;
    }

    public boolean BallPlayer() {
        if (ball.location.height + ball.radius < player.location.height) return false;
        if (ball.location.width + ball.radius < player.location.width) return false;
        if (ball.location.width - ball.radius > player.location.width + player.size.width) return false;

        if (player.location.width - 8 <= ball.location.width && ball.location.width <= player.location.width + player.size.width + 8) {
            ball.location.height = player.location.height - ball.radius;
            vectorIndex.width = 1 - vectorIndex.width;
            double change = utils.min(player.location.width + player.size.width, utils.max(player.location.width, ball.location.width)) - player.location.width;
            System.out.println(change);
            vectorIndex.height = (int)Math.floor(change / player.size.width * 10);
            System.out.println(vectorIndex);
            ball.vector = vectorBall[vectorIndex.width][vectorIndex.height];
            return true;
        }

        vectorIndex.height = 10 - vectorIndex.height;
        return true;
    }

    public void ballMove() {
        ball.location.width += ball.vector.width;
        ball.location.height += ball.vector.height;
    }

    public void checkContact() {
        if(BallWall())return;
        if(BallPlayer())return;
    }

    public void playerMove() {
        int futureLocation = utils.min(cursorLocation.width, screenSize.width - player.size.width - 10);
        if (lastLocationPlayer.width < player.location.width) {
            if (futureLocation <= player.location.width) {
                lastLocationPlayer.width = player.location.width;
            }
        }
        if (lastLocationPlayer.width > player.location.width) {
            if (futureLocation >= player.location.width) {
                lastLocationPlayer.width = player.location.width;
            }
        }
        player.location.width = futureLocation;
        // if ((player.location.width - lastLocationPlayer.width) / 10 > maxSpeed) {
        //     maxSpeed = (player.location.width - lastLocationPlayer.width) / 10;
        //     System.out.println(maxSpeed);
        // }
    }

    public void paintBackground(Graphics2D g2d) {
        g2d.drawImage(backgroundImage, 0, 0, this);
    }

    public void paintPlayer(Graphics2D g2d) {
        g2d.drawImage(playerImage, player.location.width, player.location.height, this);
    }

    public void paintBall(Graphics2D g2d) {
        g2d.drawImage(ballImage, ball.location.width - ball.radius, ball.location.height - ball.radius, this);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        playerMove();
        ballMove();

        checkContact();

        paintBackground(g2d);
        paintPlayer(g2d);
        paintBall(g2d);

        if (loopMusic == 1 && music != null) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    class TKeyboardAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (isPause == 1) {
                    isPause = -1;
                    timer.start();
                } else {
                    isPause = 1;
                    timer.stop();
                }
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

    public void getImages() {
        backgroundImage = new ImageIcon("image/background.png").getImage();
        playerImage = new ImageIcon("image/player.png").getImage();
        ballImage = new ImageIcon("image/ball.png").getImage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}