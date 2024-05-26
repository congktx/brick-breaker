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
import javax.sound.sampled.FloatControl;
public class Board extends JPanel {
    Timer timer;
    int timeDelay = 12;

    Dimension screenSize = new Dimension(960, 540);
    Dimension wallSize = new Dimension(40, 540);
    Dimension heartSize = new Dimension(49, 43);
    Dimension numberSize = new Dimension(30, 45);
    Dimension xSize = new Dimension(18, 24);

    Dimension cursorLocation = new Dimension(0, 0);

    Player player = new Player();

    Ball[] balls = new Ball[10];
    int numberBall;

    Brick[] bricks = new Brick[200];
    int numberBrick;

    Item[] items = new Item[8];
    int numberItem;

    int NumberOfTurn = 12;
    int TimeEachTurn = 30 * timeDelay;
    Bullet[] bullets = new Bullet[NumberOfTurn * 2];
    int numberBullet;

    int haveGun, longPlayer, lives = 0, score = 0, high_score = 0, mapIndex;
    int isHomePage = 1, isPlaying = 0, pauseAfterBallZero = 0, isSetting = 0;

    Image animeGirl1, shigureImage;

    Image startButton, settingButton, pauseButton, scrollButton, scrollBar, homeButton, playButton, restartButton;
    Dimension startButtonLocation = new Dimension(360, 400);
    Dimension startButtonSize = new Dimension(226, 63);
    Dimension settingButtonLocation = new Dimension(360, 470);
    Dimension settingButtonSize = new Dimension(226, 63);

    Image HomePageImage, SettingPageImage, ResultPageImage, backgroundImage;
    Image heartImage, xImage, wallLeftImage, wallRightImage, headPlayerImage, bodyPlayerImage, tailPlayerImage, ballImage, gunImage, bulletImage;
    Image[] numbersImage = new Image[11];
    ArrayList<Image> brickImage = new ArrayList<Image>();
    ArrayList<Image> itemImage = new ArrayList<Image>();

    Clip bgmSound, ballSound, buttonSound, fireSound, itemSound;
    FloatControl bgmControl, ballControl, buttonControl, fireControl, itemControl;
    int timeBallSound = 0, timeButtonSound = 0, timeFireSound = 0, timeItemSound = 0;
    int timeBallSoundDefault = 156, timeButtonSoundDefault = 312, timeFireSoundDefault = 240, timeItemSoundDefault = 864;
    float minVolume = -24, maxVolume = 6;
    float musicVolume = 6, gameVolume = 6;

    public Board() {
        setFocusable(true);
        setDoubleBuffered(true);

        addMouseListener(new TMouseAdapter());
        addMouseMotionListener(new TMouseMotionAdapter());
        
        getImages();
        getMusic();

        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 0, timeDelay);
    }

    //reset
    public void resetNewGame() {
        lives = 3; score = 0;
        longPlayer = haveGun = numberBall = numberBrick = numberItem = numberBullet = 0;
        resetBallPlayer();
        mapIndex = 1;
        setupMap();
    }

    public void resetBallPlayer() {
        Ball ball = new Ball();
        ball.setXY(480, 493);
        ball.setDXY(0, -6.32455532);
        balls[0] = ball;
        numberBall = 1;

        player.location.width = 480;
        player.location.height = 507;

        longPlayer = haveGun = numberBullet = 0;

        pauseAfterBallZero = 1;
    }

    public void nextMap() {
        mapIndex++;
        setupMap();
        resetBallPlayer();
        longPlayer = haveGun = numberBullet = numberItem = 0;
    }

    // listener
    class TMouseAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            cursorLocation.width = e.getX();
            cursorLocation.height = e.getY();
            if (isSetting == 1) {
                if (isHomePage == 1) {
                    if (420 <= e.getX() && e.getX() <= 420 + 119)
                    if (350 <= e.getY() && e.getY() <= 350 + 119) {
                        timeButtonSound = timeButtonSoundDefault;
                        isSetting = 0;
                        return;
                    }
                }
                if (isPlaying == 1) {
                    if (290 <= e.getX() && e.getX() <= 290 + 119)
                    if (370 <= e.getY() && e.getY() <= 370 + 119) {
                        timeButtonSound = timeButtonSoundDefault;
                        isSetting = isPlaying = 0;
                        isHomePage = 1;
                        return;
                    }
                    if (410 <= e.getX() && e.getX() <= 410 + 157)
                    if (350 <= e.getY() && e.getY() <= 350 + 157) {
                        timeButtonSound = timeButtonSoundDefault;
                        isSetting = 0;
                        return;
                    }
                    if (568 <= e.getX() && e.getX() <= 568 + 119)
                    if (370 <= e.getY() && e.getY() <= 370 + 119) {
                        timeButtonSound = timeButtonSoundDefault;
                        isSetting = 0;
                        resetNewGame();
                        return;
                    }
                }
                return;
            }
            if (isHomePage == 1)
            {
                if (startButtonLocation.width <= e.getX() && e.getX() <= startButtonLocation.width + startButtonSize.width)
                if (startButtonLocation.height <= e.getY() && e.getY() <= startButtonLocation.height + startButtonSize.height) {
                    timeButtonSound = timeButtonSoundDefault;
                    isHomePage = 0;
                    isPlaying = 1;
                    resetNewGame();
                    return;
                }

                if (settingButtonLocation.width <= e.getX() && e.getX() <= settingButtonLocation.width + startButtonSize.width)
                if (settingButtonLocation.height <= e.getY() && e.getY() <= settingButtonLocation.height + startButtonSize.height) {
                    timeButtonSound = timeButtonSoundDefault;
                    isSetting = 1;
                    return;
                }

                return;
            }
            if (lives == 0) {
                if (350 <= e.getX() && e.getX() <= 350 + 119)
                if (350 <= e.getY() && e.getY() <= 350 + 119) {
                    timeButtonSound = timeButtonSoundDefault;
                    isHomePage = 1;
                    return;
                }
                if (500 <= e.getX() && e.getX() <= 500 + 119)
                if (350 <= e.getY() && e.getY() <= 350 + 119) {
                    timeButtonSound = timeButtonSoundDefault;
                    resetNewGame();
                    return;
                }
                return;
            }
            if (isPlaying == 1)
            { 
                if (wallSize.width + 5 <= e.getX() && e.getX() <= wallSize.width + 5 + 63)
                if (5 <= e.getY() && e.getY() <= 5 + 63) {
                    timeButtonSound = timeButtonSoundDefault;
                    isSetting = 1;
                    return;
                }
                if (pauseAfterBallZero == 1) {
                    pauseAfterBallZero = 0;
                    return;
                }
            }
        }
    }
    class TMouseMotionAdapter extends MouseAdapter {
        public void mouseMoved(MouseEvent e) {
            cursorLocation.width = e.getX();
            cursorLocation.height = e.getY();
        }
        public void mouseDragged(MouseEvent e) {
            cursorLocation.width = e.getX();
            cursorLocation.height = e.getY();
            if (isSetting == 1) {

                int gameVolumeLocation = (int)Math.floor((gameVolume - minVolume) * 329 / (maxVolume - minVolume));
                if (330 + gameVolumeLocation - 25 <= e.getX() && e.getX() <= 330 + gameVolumeLocation + 25)
                if (180 + 15 - 25 <= e.getY() && e.getY() <= 180 + 15 + 25) {
                    gameVolume = (float)(e.getX() - 330) * (maxVolume - minVolume) / 329 + minVolume;
                    gameVolume = Math.max(minVolume, Math.min(maxVolume, gameVolume));
                    ballControl.setValue(gameVolume);
                    fireControl.setValue(gameVolume);
                    itemControl.setValue(gameVolume);
                    buttonControl.setValue(musicVolume);
                }

                int musicVolumeLocation = (int)Math.floor((musicVolume - minVolume) * 329 / (maxVolume - minVolume));
                if (330 + musicVolumeLocation - 25 <= e.getX() && e.getX() <= 330 + musicVolumeLocation + 25)
                if (240 + 15 - 25 <= e.getY() && e.getY() <= 240 + 15 + 25) {
                    musicVolume = (float)(e.getX() - 330) * (maxVolume - minVolume) / 329 + minVolume;
                    musicVolume = Math.max(minVolume, Math.min(maxVolume, musicVolume));
                    bgmControl.setValue(musicVolume);
                }

                soundStop();
                playSound();
            }
        }
    }

    //game run
    class ScheduleTask extends TimerTask {
        public void run() {
            if (lives == 0) isPlaying = 0;
            if (isPlaying == 1 && pauseAfterBallZero != 1 && isSetting != 1 && lives > 0) {
                moveAll();
                checkContact();
            }
            repaint();
        }
    }
    public void paint(Graphics g) {
        playSound();
        paintAll(g);
    }

    //play sound
    public void playBgmSound() {
        if (bgmSound != null) {
            bgmSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void playBallSound() {
        if (timeBallSound > 0) {
            timeBallSound -= timeDelay;
            ballSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            ballSound.stop();
        }
    }

    public void playFireSound() {
        if (timeFireSound > 0) {
            timeFireSound -= timeDelay;
            fireSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            fireSound.stop();
        }
    }

    public void playItemSound() {
        if (timeItemSound > 0) {
            timeItemSound -= timeDelay;
            itemSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            itemSound.stop();
        }
    }

    public void playButtonSound() {
        if (timeButtonSound > 0) {
            timeButtonSound -= timeDelay;
            buttonSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            buttonSound.stop();
        }
    }

    public void playSound() {
        playBgmSound();
        playBallSound();
        playFireSound();
        playItemSound();
        playButtonSound();
    }

    public void soundStop() {
        bgmSound.stop();
        ballSound.stop();
        buttonSound.stop();
        fireSound.stop();
        itemSound.stop();
    }

    // move objects
    public void ballMove() {
        for (int i=0; i<numberBall; i++) {
            balls[i].x += balls[i].dx;
            balls[i].y += balls[i].dy;
        }
    }

    public void playerMove() {
        player.location.width = cursorLocation.width;
        player.location.width = Math.max(player.location.width, player.size.width / 2);
        player.location.width = Math.min(player.location.width, screenSize.width - player.size.width / 2);
    }

    public void itemMove() {
        for (int i=0; i<numberItem; i++) {
            items[i].location.height += 2;
        }
    }

    public void bulletMove() {
        for (int i=0; i<numberBullet; i++) {
            bullets[i].location.height -= 6;
        }
    }

    public void moveAll() {
        playerMove();
        ballMove();
        itemMove();
        bulletMove();
    }

    // paint all objects
    public void paintBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
    }

    public void paintWall(Graphics g) {
        g.drawImage(wallLeftImage, 0, 0, this);
        g.drawImage(wallRightImage, screenSize.width - wallSize.width, 0, this);
    }

    public void paintPlayer(Graphics g) {
        g.drawImage(headPlayerImage, player.location.width - player.size.width / 2, player.location.height, this);
        for (int i=player.location.width - player.size.width/2 + 12; i<=player.location.width + player.size.width/2 - 80; i+=2) {
            g.drawImage(bodyPlayerImage, i, player.location.height, this);
        }
        g.drawImage(tailPlayerImage, player.location.width + player.size.width / 2 - 12, player.location.height, this);
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

    public void paintGun(Graphics g) {
        if (haveGun > 0) {
            g.drawImage(gunImage, player.location.width - player.size.width/2 + 6, player.location.height - 18, this);
            g.drawImage(gunImage, player.location.width + player.size.width/2 - 24, player.location.height - 18, this);
        }
    }

    public void paintBullets(Graphics g) {
        for (int i=0; i<numberBullet; i++) {
            g.drawImage(bulletImage, bullets[i].location.width, bullets[i].location.height, this);
        }
    }

    public void paintLives(Graphics g) {
        Dimension init = new Dimension(screenSize.width - wallSize.width, 0);
        g.drawImage(heartImage, init.width - heartSize.width, init.height + 45 - heartSize.height, this);
        init.width -= heartSize.width;
        g.drawImage(xImage, init.width - xSize.width, init.height + 45 - xSize.height, this);
        init.width -= xSize.width;
        int lives_clone = lives;
        while (lives_clone > 0) {
            g.drawImage(numbersImage[lives_clone % 10], init.width - numberSize.width, init.height + 45 - numberSize.height, this);
            init.width -= numberSize.width;
            lives_clone /= 10;
        }
    }

    public void paintScore(Graphics g, int score, int x, int y) {
        int scs = (int) Math.log10(score);
        x = (int) ((double)x + (double)(scs*numberSize.width)/2); 
        if (score == 0) g.drawImage(numbersImage[0], x - numberSize.width, y, this);
        while (score > 0) {
            g.drawImage(numbersImage[score % 10], x - numberSize.width, y, this);
            x -= numberSize.width;
            score /= 10;
        }
    }

    public void paintAll(Graphics g) {
        paintBackground(g);
        if (isSetting == 1) {
            g.drawImage(SettingPageImage, 260, 140, this);
            g.drawImage(scrollBar, 330, 180, this);
            g.drawImage(scrollBar, 330, 240, this);
            int gameVolumeLocation = (int)Math.floor((gameVolume - minVolume) * 329 / (maxVolume - minVolume));
            int musicVolumeLocation = (int)Math.floor((musicVolume - minVolume) * 329 / (maxVolume - minVolume));
            g.setColor(Color.WHITE);
            g.fillRect(330 + gameVolumeLocation, 180 , 329 - gameVolumeLocation, 30);
            g.fillRect(330 + musicVolumeLocation, 240 , 329 - musicVolumeLocation, 30);
            g.drawImage(scrollButton, 330 + gameVolumeLocation - 25, 180 + 15 - 25, this);
            g.drawImage(scrollButton, 330 + musicVolumeLocation - 25, 240 + 15 - 25, this);
            if (isHomePage == 1) {
                g.drawImage(homeButton, 420, 350, this);
            }
            if (isPlaying == 1) {
                g.drawImage(homeButton, 290, 370, this);
                g.drawImage(playButton, 410, 350, this);
                g.drawImage(restartButton, 568, 370, this);
            }
            return;
        }
        if (isHomePage == 1) {
            g.drawImage(HomePageImage, 0, 0, this);
            g.drawImage(startButton, startButtonLocation.width, startButtonLocation.height, this);
            g.drawImage(settingButton, settingButtonLocation.width, settingButtonLocation.height, this);
            g.drawImage(shigureImage, -40, 200, this);
            return;
        }
        if (lives == 0) {
            g.drawImage(ResultPageImage, 80, 160, this);
            g.drawImage(homeButton, 350, 350, this);
            g.drawImage(restartButton, 500, 350, this);
            paintScore(g, score, 290, 250);
            paintScore(g, high_score, 700, 250);
            return;
        }
        if (isPlaying == 1)
        {
            paintWall(g);
            paintPlayer(g);
            paintBall(g);
            paintBricks(g);
            paintItems(g);
            paintBullets(g);
            paintGun(g);
            paintLives(g);
            paintScore(g, score, 480, 10);
            g.drawImage(pauseButton, wallSize.width + 5, 5, this);
        }
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
        boolean ok = false;
        for (int i=0; i<numberBall; i++) {
            if (balls[i].x - balls[i].radius <= wallSize.width) {
                balls[i].dx = Math.abs(balls[i].dx);
                ok = true;
            }

            if (balls[i].x + balls[i].radius >= screenSize.width - wallSize.width) {
                balls[i].dx = - Math.abs(balls[i].dx);
                ok = true;
            }

            if (balls[i].y - balls[i].radius <= 0) {
                balls[i].dy = Math.abs(balls[i].dy);
                ok = true;
            }
        }
        if (ok) timeBallSound = timeBallSoundDefault;
    }

    public void BallPlayer() {
        for (int i=0; i<numberBall; i++) {
            if (balls[i].y + balls[i].radius < player.location.height) continue;
            if (balls[i].x + balls[i].radius < player.location.width - player.size.width / 2) continue;
            if (balls[i].x - balls[i].radius > player.location.width + player.size.width / 2) continue;

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
                double changeDX = Math.min(player.location.width + player.size.width / 2, Math.max(player.location.width - player.size.width / 2, balls[i].x)) - (player.location.width - player.size.width / 2);
                changeDX = ((changeDX / player.size.width) - 0.5) * 2 * 6;
                balls[i].setDXY(changeDX, - Math.sqrt(40 - changeDX * changeDX));
            }

            timeBallSound = timeBallSoundDefault;
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

                score += 1;
                high_score = Math.max(high_score, score);
            }

            timeBallSound = timeBallSoundDefault;

            break;
        }
    }

    public void power(int type) {
        if (type == 1) {
            for (int i=numberBall-1; i>=0; i--) 
            for (int j=1; j<=2; j++) {
                Ball ball = new Ball();
                ball.x = balls[i].x; ball.y = balls[i].y;
                double changeDX = (Math.random() * 2 - 1) * 6;
                ball.setDXY(changeDX, - Math.sqrt(40 - changeDX * changeDX));
                balls[numberBall++] = ball;
            }
        }
        if (type == 2) {
            longPlayer = 6000;
        }
        if (type == 3) {
            lives++;
        }
        if (type == 4) {
            haveGun = NumberOfTurn * TimeEachTurn;
        }
    }

    public void ItemPlayer() {
        for (int i=numberItem - 1; i>=0; i--) {
            if (items[i].location.width + items[i].size.width < player.location.width - player.size.width / 2) continue;
            if (items[i].location.width > player.location.width + player.size.width / 2) continue;
            if (items[i].location.height + items[i].size.height < player.location.height) continue;
            if (items[i].location.height > player.location.height + player.size.height) continue;
            power(items[i].type);
            items[i] = items[numberItem - 1];
            numberItem--;
            timeItemSound = timeItemSoundDefault;
        }
    }

    public void TimeRunItem() {
        //item2
        if (longPlayer <= 0) {
            if (player.size.width > player.normalSize) {
                player.size.width -= 8;
            }
        }
        else {
            if (player.size.width < player.longSize) {
                player.size.width += 8;
            }
        }
        longPlayer -= timeDelay;

        //item4
        if (haveGun > 0  && haveGun % TimeEachTurn == 0) {
            Bullet bullet1 = new Bullet();
            bullet1.location.width = player.location.width - player.size.width/2 + 6;
            bullet1.location.height = player.location.height - 18;
            bullets[numberBullet++] = bullet1;

            Bullet bullet2 = new Bullet();
            bullet2.location.width = player.location.width + player.size.width/2 - 24;
            bullet2.location.height = player.location.height - 18;
            bullets[numberBullet++] = bullet2;

            timeFireSound = timeFireSoundDefault;
        } 
        haveGun -= timeDelay;
    }

    public void BulletBrick() {
        for (int i=numberBullet - 1; i>=0; i--) 
        for (int j=numberBrick - 1; j>=0; j--) {
            if (bullets[i].location.width + bullets[i].size.width < bricks[j].location.width) continue;
            if (bullets[i].location.width > bricks[j].location.width + bricks[j].size.width) continue;
            if (bullets[i].location.height + bullets[i].size.height < bricks[j].location.height) continue;
            if (bullets[i].location.height > bricks[j].location.height + bricks[j].size.height) continue;

            bullets[i] = bullets[numberBullet - 1];
            numberBullet--;

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

                score += 1;
                high_score = Math.max(high_score, score);
            }

            break;
        }
    }

    public void BulletOut() {
        for (int i=numberBullet-1; i>=0; i--) 
            if (bullets[i].location.height + bullets[i].size.height < 0) {
                bullets[i] = bullets[numberBullet - 1];
                numberBullet--;
            }
    }

    public void checkContact() {
        if (numberBrick == 0) {
            nextMap();
            return;
        }
        if (numberBall == 0) {
            lives--;
            resetBallPlayer();
            return;
        }
        BallOut();
        BallWall();
        BallPlayer();
        BallBrick();
        ItemPlayer();
        TimeRunItem();
        BulletBrick();  
        BulletOut();
    }

    // setup
    public void getImages() {
        HomePageImage = new ImageIcon("image/home-page.png").getImage();

        SettingPageImage = new ImageIcon("image/setting-page.png").getImage();

        ResultPageImage = new ImageIcon("image/result-page.png").getImage();

        backgroundImage = new ImageIcon("image/background.png").getImage();

        wallLeftImage = new ImageIcon("image/wall_left.png").getImage();
        wallRightImage = new ImageIcon("image/wall_right.png").getImage();

        headPlayerImage = new ImageIcon("image/head_player.png").getImage();
        bodyPlayerImage = new ImageIcon("image/body_player.png").getImage();
        tailPlayerImage = new ImageIcon("image/tail_player.png").getImage();

        ballImage = new ImageIcon("image/ball.png").getImage();

        for (int i=1; i<=5; i++)
        for (int j=1; j<=2; j++) {
            brickImage.add(new ImageIcon("image/brick"+i+"_"+j+".png").getImage());
        }

        for (int i=1; i<=4; i++) {
            itemImage.add(new ImageIcon("image/item"+i+".png").getImage());
        }

        gunImage = new ImageIcon("image/gun.png").getImage();

        bulletImage = new ImageIcon("image/bullet.png").getImage();

        for (int i=0; i<=9; i++)
            numbersImage[i] = new ImageIcon("image/number/"+i+".png").getImage();

        heartImage = new ImageIcon("image/heart.png").getImage();

        xImage = new ImageIcon("image/number/x.png").getImage();

        startButton = new ImageIcon("image/button/start-button.png").getImage();

        settingButton = new ImageIcon("image/button/setting-button.png").getImage();

        pauseButton = new ImageIcon("image/button/pause-button.png").getImage();

        scrollButton = new ImageIcon("image/button/scroll-button.png").getImage();

        scrollBar = new ImageIcon("image/button/scroll-bar.png").getImage();

        homeButton = new ImageIcon("image/button/home-button.png").getImage();

        playButton = new ImageIcon("image/button/play-button.png").getImage();

        restartButton = new ImageIcon("image/button/restart-button.png").getImage();

        //anime
        animeGirl1 = new ImageIcon("image/anime/anime-girl-1.png").getImage(); 

        shigureImage = new ImageIcon("image/anime/shigure-unscreen.gif").getImage();
    }

    public void getMusic() {
        try {
            File file = new File("sound/remix.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            bgmSound = AudioSystem.getClip();
            bgmSound.open(audioStream);
            bgmControl = (FloatControl) bgmSound.getControl(FloatControl.Type.MASTER_GAIN);
            bgmControl.setValue(musicVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("sound/ball.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            ballSound = AudioSystem.getClip();
            ballSound.open(audioStream);
            ballControl = (FloatControl) ballSound.getControl(FloatControl.Type.MASTER_GAIN);
            ballControl.setValue(gameVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("sound/button.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            buttonSound = AudioSystem.getClip();
            buttonSound.open(audioStream);
            buttonControl = (FloatControl) buttonSound.getControl(FloatControl.Type.MASTER_GAIN);
            buttonControl.setValue(gameVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("sound/fire.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            fireSound = AudioSystem.getClip();
            fireSound.open(audioStream);
            fireControl = (FloatControl) fireSound.getControl(FloatControl.Type.MASTER_GAIN);
            fireControl.setValue(gameVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("sound/item.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            itemSound = AudioSystem.getClip();
            itemSound.open(audioStream);
            itemControl = (FloatControl) itemSound.getControl(FloatControl.Type.MASTER_GAIN);
            itemControl.setValue(gameVolume);
        } catch (Exception e) {
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

    public void setUpMap2() {

        int initHeight = 80, indexBrick = 0;

        for (int i=1; i<=5; i++)
        for (int j=1; j<=3; j++) {
            Brick br1 = new Brick();
            br1.location.width = wallSize.width + br1.size.width * (j-1);
            br1.location.height = initHeight + br1.size.height * (i-1);
            br1.color = 1;
            if ((i+j) % 2 == 0) br1.hp = 2;
            else br1.hp = 1;
            bricks[indexBrick++]=br1;

            Brick br2 = new Brick();
            br2.location.width = screenSize.width - wallSize.width - br2.size.width * j;
            br2.location.height = initHeight + br2.size.height * (i-1);
            br2.color = 4;
            if ((i+j) % 2 == 0) br2.hp = 2;
            else br2.hp = 1;
            bricks[indexBrick++]=br2;
        }
        
        for (int i=1; i<=8; i++) {
            Brick br1 = new Brick();
            br1.location.width = 270;
            br1.location.height = initHeight + br1.size.height * (i-1);
            br1.color = 5;
            br1.hp = 2;
            bricks[indexBrick++]=br1;

            Brick br2 = new Brick();
            br2.location.width = 630;
            br2.location.height = initHeight + br2.size.height * (i-1);
            br2.color = 5;
            br2.hp = 2;
            bricks[indexBrick++]=br2;
        }

        for (int i=1; i<=2; i++)
        for (int j=1; j<=5; j++) {
            Brick br = new Brick();
            br.location.width = 270 + br.size.width * j;
            br.location.height = initHeight + br.size.height * (i+5);
            br.color = 5;
            br.hp = 2;
            bricks[indexBrick++]=br;
        }

        for (int i=1; i<=5; i++)
        for (int j=1; j<=3; j++) {
            Brick br = new Brick();
            br.location.width = 390 + br.size.width * (j-1);
            br.location.height = initHeight + br.size.height * (i-1);
            br.color = 2;
            br.hp = 2;
            bricks[indexBrick++]=br;
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

    public void setupMap() {
        if (mapIndex == 1) {
            setUpMap1();
            return;
        }
        if (mapIndex == 2) {
            setUpMap2();
            return;
        }
    }
}