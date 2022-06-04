import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT )/ UNIT_SIZE;
    static final int TIMER_DELAY = 150;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 4;
    int fruitsEaten;
    int fruitX;
    int fruitY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }
    //game methods
    public void startGame() {
        createFruit();
        running = true;
        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    //draw objects
    public void draw(Graphics g) {

        if (running){
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.green);
            g.fillOval(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);

            for (int j = 0; j < bodyParts; j++) {
                if (j == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(255, 215, 0));
                    g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Monospace", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + fruitsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + fruitsEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    //movement
    public void move() {
        for (int k = bodyParts; k > 0; k--){
            x[k] = x[k-1];
            y[k] = y[k-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    //populate game with fruit
    public void createFruit() {
        fruitX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        fruitY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    //check if the snake has eaten
    public void checkFruit() {
        if ((x[0] == fruitX) && (y[0] == fruitY)){
            bodyParts++;
            fruitsEaten++;
            createFruit();
        }
    }
    //check collisions
    public void checkCollisions() {
        //if snake bites self like ouroboros
        for (int k=bodyParts; k>0; k--){
            if((x[0] == x[k]) && y[0] == y[k]){
                running = false;
            }
        }
        //if snake leaves game view
        //left border
        if(x[0] < 0) {
            running = false;
        }
        //right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //bottom border
        if(y[0] < 0) {
            running = false;
        }
        //top border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
    }
    //game over
    public void gameOver(Graphics g) {
        //Game over
        g.setColor(Color.red);
        g.setFont(new Font("Monospace", Font.BOLD, 55));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("You lose!", (SCREEN_WIDTH - metrics.stringWidth("You lose!"))/2, SCREEN_HEIGHT/2);
        //score text
        g.setFont(new Font("Monospace", Font.BOLD, 35));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score: " + fruitsEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + fruitsEaten))/2, g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(running) {
            move();
            checkFruit();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
