import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int BOARD_WIDTH = 360;
    int BOARD_HEIGHT = 640;

    //Images
    Image birdImg;
    Image backgroundImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird blueprint
    int birdX = BOARD_WIDTH/8;
    int birdY = BOARD_HEIGHT/2;
    int birdWidth=34;
    int birdHeight=24;

    //pipes blueprint
    int pipeX = BOARD_WIDTH;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    int velocityX=-4;//ACTUALLY MOVES PIPES(BUT LOOKS LIKE BIRB IS MOVING)
    int velocityY = -9;
    int gravity=1;
    ArrayList<Pipe> pipes;
    Random random = new Random();




    //TimerLoop
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score =0;


    FlappyBird(){
        setPreferredSize(new Dimension(BOARD_WIDTH,BOARD_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        //loading images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();
        gameLoop = new Timer(1000/60,this);
        gameLoop.start();
        placePipesTimer = new Timer(1000, (@SuppressWarnings("unused") ActionEvent e) -> {
            placePipes();
        });
        placePipesTimer.start();
    }

    //drawing and components setup
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
       //background
        g.drawImage(backgroundImg,0,0,BOARD_WIDTH,BOARD_HEIGHT,null);
        //birb
        g.drawImage(birdImg , bird.x,bird.y,bird.width,bird.height,null);
        //pipes
        for(int i =0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null); 
        }
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 32)); // Change to a cartoonish font
        if (gameOver) {
            g.drawString("Game Over", BOARD_WIDTH / 2 - 80, BOARD_HEIGHT / 4);
            g.drawString(String.valueOf((int) score), BOARD_WIDTH / 2 - 20, BOARD_HEIGHT / 3);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 24)); // Smaller font for restart message
            g.drawString("Press Space to Restart", BOARD_WIDTH / 2 - 120, BOARD_HEIGHT / 2); // Centered
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    } 

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
       repaint();
       if(gameOver){
           gameLoop.stop();
           placePipesTimer.stop();
       }
    }

    //moving birb
    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
    
        // Limit downward speed to avoid too fast falling
        if (velocityY > 10) {
            velocityY = 10;
        }
    
        // Prevent bird from going above the screen
        if (bird.y < 0) {
            bird.y = 0;
            velocityY = 0; // Stop going higher
        }

        for(int i =0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && pipe.x < bird.x){
                pipe.passed = true;
                score += 0.5;
            }
            if(collision(bird, pipe)){
                gameOver = true;
            }
            
        }
        if(bird.y>BOARD_HEIGHT){
            gameOver = true;
        }

    }
    public boolean collision(Bird a , Pipe b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner

    }
     

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -12;
            if(gameOver){
                //restarting by reseting all the conditions
                bird.y=birdY;
                pipes.clear();
                velocityY=0;
                score=0;
                gameOver=false;
                gameLoop.start();
                placePipesTimer.start();
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //pipes ka logic
    public void placePipes(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = BOARD_HEIGHT/4;
        Pipe toppipe= new Pipe(topPipeImg);
        toppipe.y=randomPipeY;
        pipes.add(toppipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = randomPipeY + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
}
