package controller;

public class Ball {
    double x,y,dx,dy;
    int radius = 14;

    public Ball() {
    }

    public void setDXY(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
