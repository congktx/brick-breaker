package controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.io.File;

public class LoopSound {
    public static void main(String[] args) throws Exception {

        File file = new File("sound/bgm.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        // clip.loop(Clip.LOOP_CONTINUOUSLY);
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // // A GUI element to prevent the Clip's daemon Thread
        // // from terminating at the end of the main()
        // JOptionPane.showMessageDialog(null, "Close to exit!");
        // }
        // });

        Scanner scanner = new Scanner(System.in);

        String response = "";

        while (!response.equals("!")) {
            System.out.println("P = play, L = Loop,S = Stop, R = Reset, Q = Quit");
            System.out.print("Enter your choice: ");

            response = scanner.next();
            response = response.toUpperCase();

            switch (response) {
                case ("L"):
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    break;
                case ("P"):
                    clip.start();
                    break;
                case ("S"):
                    clip.stop();
                    break;
                case ("R"):
                    clip.setMicrosecondPosition(0);
                    break;
                case ("Q"):
                    clip.close();
                    // break;
                default:
                    System.out.println("Not a valid response");
            }

        }
        System.out.println("Byeeee!");
    }
}