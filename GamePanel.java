import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    //parametre velkosti okna
    static final int GAME_WIDTH = 1024;
    static final int GAME_HEIGHT = 768;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);

    //parametre velkosti lopty
    static final int BALL_DIAMETER = 20;

    //parametre velkosti hraca
    static final int PADDLE_WIDTH = 150;
    static final int PADDLE_HEIGHT = 25;

    //parametre pola tehal
    static final int BRICK_POLE_WIDTH = 768;
    static final int BRICK_POLE_HEIGHT = 256;
    static final int BRICK_POLE_POCET = 32;
    static final int BRICK_POLE_X = 128;
    static final int BRICK_POLE_Y = 96;

    
    int m = 4;
    int n = 12;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;

    Paddle paddle;
    Ball ball;
    Brick_pole brick_pole;

    Skore skore;

    GamePanel(){
        //vykreslenie objektov
        newPaddle();
        newBall();
        newBrick_pole();

        //nastavenie panelu
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);


        gameThread = new Thread(this);
        gameThread.start();

    }

    //vytvorenie loptz
    public void newBall(){
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),GAME_HEIGHT-PADDLE_HEIGHT-BALL_DIAMETER,BALL_DIAMETER,BALL_DIAMETER);
    }

    //vytvorenie hraca
    public void newPaddle(){
        paddle = new Paddle((GAME_WIDTH/2)-(PADDLE_WIDTH/2), GAME_HEIGHT-PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public void newBrick_pole(){
        brick_pole = new Brick_pole(BRICK_POLE_X, BRICK_POLE_Y, BRICK_POLE_WIDTH, BRICK_POLE_HEIGHT);
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
        brick_pole.draw((Graphics2D) g);
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

        //kolizia lopty od pola
        if(ball.intersects(brick_pole)){
            for(int i = 0; i<brick_pole.map.length; i++){
                for(int j = 0; j<brick_pole.map[0].length; j++){
                    int tehlaX = j*brick_pole.brick_Width+brick_pole.x;
                    int tehlaY = i*brick_pole.brick_height+brick_pole.y;
                    int brick_Width = brick_pole.brick_Width;
                    int brick_height = brick_pole.brick_height;

                    Rectangle hranol = new Rectangle(tehlaX, tehlaY, brick_Width, brick_height);
                    Rectangle ballRect = new Rectangle(ball.x, ball.y, BALL_DIAMETER, BALL_DIAMETER);
                    Rectangle tehla_rect = hranol;

                    if(ballRect.intersects(tehla_rect)){
                        brick_pole.Hodna_tehli(0, i, j);

                        if(ball.x<=brick_pole.x || ball.x>=brick_pole.x + brick_pole.width){
                            //ball.xVelocity = Math.abs(ball.xVelocity);
                            ball.setXDirection(-ball.xVelocity);
                        }
                        else{
                            ball.setYDirection(-ball.yVelocity);
                        }
                    }


                }
            }


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
