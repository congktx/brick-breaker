package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class test {
    public static void main(String[] args) {
        readSizeImage("image/number/2.png");
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
}
