import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1024;
    static final int GAME_HEIGHT = 768;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 150;
    static final int PADDLE_HEIGHT = 25;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;

    Paddle paddle;
    Ball ball;

    Skore skore;

    GamePanel(){
        //vykreslenie objektov
        newPaddle();
        newBall();

        //nastavenie panelu
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();

    }

    //vytvorenie loptz
    public void newBall(){
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER/2),BALL_DIAMETER,BALL_DIAMETER);
    }

    //vytvorenie hraca
    public void newPaddle(){
        paddle = new Paddle((GAME_WIDTH/2)-(PADDLE_WIDTH/2), GAME_HEIGHT-PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
    }


    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    //nakreslenie objektu v okne
    public void draw(Graphics g){
        paddle.draw(g);
        ball.draw(g);
    }

    //zistenie ci nastane kolizia a ako sa objek sprava
    public void checkCollision(){
        //nastavenie aby sa hrac zastavil na hranici okna
        //zastavenie o lavu stranu
        if(paddle.x<=0){
            paddle.x=0;
        }
        //zastavenie o pravu stranu
        if(paddle.x>=(GAME_WIDTH-PADDLE_WIDTH)){
            paddle.x=(GAME_WIDTH-PADDLE_WIDTH);
        }

        //kolizie lopty
        //odrazenie lopty od hranic
        //od bokov
        if(ball.x <= 0)
        {
            ball.setXDirection(-ball.xVelocity);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER)
        {
            ball.setXDirection(-ball.xVelocity);
        }

        //od hornej strany
        if(ball.y <= 0)
        {
            ball.setYDirection(-ball.yVelocity);
        }

        //kolizia lopty od hraca
        if(ball.intersects(paddle)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.yVelocity = Math.abs(ball.yVelocity);
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(-ball.yVelocity);
        }

    }

    //umoznenie pohybovania
    public void move(){
        paddle.move();
        ball.move();
    }


    @Override
    public void run() {

        //gameloop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;

        while(true)
        {
            long now = System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime = now;
            if(delta >=1)
            {
                move();
                checkCollision();
                repaint();
                delta--;
                //System.out.println("test");
            }
        }
    }

    //zaznamenanie ci je klavesa stlacena
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle.keyPressed(e);
        }

        public void keyReleased(KeyEvent e){
            paddle.keyReleased(e);
        }
    }
    
}
