package controller;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
class text extends JFrame {
 
    static JFrame f;
    static JPanel p;
    static JLabel l;

    public text() {
        l = new JLabel();
 
        l.setText("label text");

        l.setLocation(200, 200);

        p = new JPanel();
 
        p.add(l);
 
        f = new JFrame("label");

        f.add(p);
 
        f.setSize(300, 300);
 
        f.show();
    }
 
    public static void main(String[] args)
    {
        new text();
    }
}