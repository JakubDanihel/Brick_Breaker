import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Brick_pole extends Rectangle{
    public int map[][];
    public int brick_Width;
    public int brick_height;
    public int pocet_tehal;


    Brick_pole(int x, int y, int width, int height){
        super(x, y, width, height);

        //nastavenie poctov stlpcov(m) a riadkov(n)
        int m = 8;
        int n = 12;

        pocet_tehal = m*n;

        //rozdelenie pola na jednotlive tehly
        map = new int[m][n];
        for(int i = 0; i<map.length; i++){
            for(int  j = 0; j<map[0].length; j++){
                map[i][j] = 1;
            }
        }

        //nastavenie parametrov sirky a vysky tehli
        brick_Width = 500/m;
        brick_height = 250/n;
    }



    public void draw(Graphics2D g){
        for(int i = 0; i<map.length; i++){
            for(int j = 0; j<map[0].length; j++){
                if(map[i][j]>0){
                    //nastavenie farby
                    g.setColor(Color.YELLOW);
                    //vyfarbenie komponentov
                    g.fillRect(j*brick_Width+x, i*brick_height+y, brick_Width, brick_height);

                    
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j*brick_Width+x, i*brick_height+y, brick_Width, brick_height); 
                }
            }
        }
    }

    public void Hodna_tehli(int hodnota, int m, int n){
        map[m][n] = hodnota;
    }

}
