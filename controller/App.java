package controller;

import javax.swing.*;

public class App extends JFrame {

    public App() {
        add(new Board());
    }

    public static void main(String[] args) {
        new App();
    }
}