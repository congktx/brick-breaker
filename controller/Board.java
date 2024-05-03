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
    Dimension playerSize = new Dimension(117, 23);

    int locationPlayer = player.location.width;
    int lastLocationPlayer = player.location.width;
    int lastTimeMove = 0;

    int loopMusic = 1;

    Image backgroundImage, playerImage, ballImage;
    Clip music;

    public Board() {
        getImages();

        setTitle("Dodge game");
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

        addMouseListener(new TMouseAdapter());
        addMouseMotionListener(new TMouseMotionAdapter());
    }

    public void ballMove() {
        ball.location.width += ball.vector.width;
        ball.location.height += ball.vector.height;

        if (ball.location.width - ball.radius <= 0) {
            ball.location.width = ball.radius;
            ball.vector.width = Math.abs(ball.vector.width);
        }

        if (ball.location.width + ball.radius >= screenSize.width) {
            ball.location.width = screenSize.width - ball.radius;
            ball.vector.width = -Math.abs(ball.vector.width);
        }

        if (ball.location.height - ball.radius <= 30) {
            ball.location.height = 30 + ball.radius;
            ball.vector.height = Math.abs(ball.vector.height);
        }

        if (ball.location.height + ball.radius >= screenSize.height) {
            ball.location.height = screenSize.height - ball.radius;
            ball.vector.height = -Math.abs(ball.vector.height);
        }

        if (player.location.height <= ball.location.height + ball.radius
                && ball.location.height + ball.radius <= player.location.height + 10)
            if (utils.max(ball.location.width - ball.radius, player.location.width) <= utils
                    .min(ball.location.width + ball.radius, player.location.width + playerSize.width)) {
                ball.vector.height = -ball.vector.height;
            }
    }

    public void playerMove() {
        if (ball.location.height + ball.radius > player.location.height + 10) {
            if (ball.location.width - ball.radius <= player.location.width
                    && player.location.width <= ball.location.width + ball.radius) {
                ball.vector = new Dimension(-7, -1);
                return;
            }
            if (ball.location.width - ball.radius <= player.location.width + playerSize.width
                    && player.location.width + playerSize.width <= ball.location.width + ball.radius) {
                ball.vector = new Dimension(7, -1);
                return;
            }
        }
    }

    public void paintBackground(Graphics2D g2d) {
        g2d.drawImage(backgroundImage, 0, 0, this);
    }

    public void paintPlayer(Graphics2D g2d) {
        player.location.width = utils.min(locationPlayer, screenSize.width - playerSize.width - 10);
        player.location.height = screenSize.height - playerSize.height - 10;
        g2d.drawImage(playerImage, player.location.width, player.location.height, this);
    }

    public void paintBall(Graphics2D g2d) {
        ballMove();
        g2d.drawImage(ballImage, ball.location.width - ball.radius, ball.location.height - ball.radius, this);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        paintBackground(g2d);
        paintPlayer(g2d);
        paintBall(g2d);

        if (loopMusic == 1 && music != null) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    class TMouseAdapter extends MouseAdapter {
        public void mouseClicked() {
        }
    }

    class TMouseMotionAdapter extends MouseAdapter {
        public void mouseMoved(MouseEvent e) {
            locationPlayer = e.getX();
            int currentTime = (int) System.currentTimeMillis();
            if (currentTime - lastTimeMove > 100) {
                lastTimeMove = currentTime;
                lastLocationPlayer = locationPlayer;
            }
            playerMove();
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