package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class test {
    public static void main(String[] args) {
        // readSizeImage("image/button/play-button.png");
        readSizeSound("sound/button.wav");
    }

    public static void readSizeImage(String path) {
        File imgFile = new File(path);
        try {
            BufferedImage bimg = ImageIO.read(imgFile);
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            System.out.println(width);
            System.out.println(height);
        } catch (IOException e) {
        }
    }

    public static void readSizeSound(String path) {
        File soundFile = new File(path);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = audioStream.getFormat();
            long audioFileLength = soundFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            System.out.println(durationInSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
