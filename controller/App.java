package controller;

import java.awt.Dimension;

import javax.swing.*;

public class App extends JFrame {
    Dimension screenSize = new Dimension(960, 540);
    boolean RESIZABLE = true;

    public App() {
        add(new Board());
        setTitle("Brick Breaker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(screenSize.width + 14, screenSize.height + 37);
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        setResizable(RESIZABLE);
        setVisible(true);
    }
// dau buoi long
    public static void main(String[] args) {
        new App();
    }
}