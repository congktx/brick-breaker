package controller;

import java.awt.Dimension;

import javax.swing.*;

public class App extends JFrame {
    Dimension screenSize = new Dimension(960, 540);

    public App() {
        add(new Board());
        setTitle("BreakOut");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(screenSize.width + 15, screenSize.height + 35);
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}