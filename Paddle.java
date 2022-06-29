import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle{
    int xVelocity;
    int speed = 10;

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT){
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        
    }

    //urcenie smeru ako sa bude pohybovat
    public void setXDirection(int xDirection){
        xVelocity = xDirection;
    }

    //urcenie ako sa bude pohybovat
    public void move(){
        x = x+xVelocity;
    }

    //nakreslenie hraca
    public void draw(Graphics g){
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }

    //zaznamenanie klaves a ako sa bude spravat pocas stlacenia klaves
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            setXDirection(-speed);
            move();

        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            setXDirection(speed);
            move();
        }
    }

    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            setXDirection(0);
            move();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            setXDirection(0);
            move();
        }
    }
}
