// utility
import java.util.ArrayList;
import java.util.Random;

// graphics
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Font;

// events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// swing
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener, Runnable {

    private final int canvasWidth;
    private final int canvasHeight;
    private final Color backgroundColor;

    private final int framesPerSecond = 25;
    private final int msPerFrame = 1000 / framesPerSecond;
    private Timer timer;
    private int frame = 0;

    private ArrayList<AlienBaby> ALIENS;
    private ArrayList<ArrayList<AlienBaby>> ALIENS_EACH_LEVEL = new ArrayList<>();
    private ArrayList<AProjectile> aprojectiles = new ArrayList<>();
    private int APROJECTILE_SPEED = 5;
    private int APROJECTILE_SIZE = 4;
    private int NUM_OF_LEVELS = 10;
    private int NUM_OF_ALIENS_PER_LEVEL = 48;
    private int ALIEN_X_START = 320;
    private int ALIEN_Y_START = 60;
    private int ALIEN_SIZE = 40;
    private int ALIEN_SPACE = 20;
    private ArrayList<PProjectile> pprojectiles = new ArrayList<>();
    private int PPROJECTILE_SPEED = 5;
    private int PPROJECTILE_WIDTH = 3;
    private double PROJECTILE_DELAY = 1;
    private int level = 0;
    private Player player;
    private int PLAYER_X_START = 580;
    private int PLAYER_Y_START = 680;
    private int PLAYER_MOVE_SPEED = 10;
    private int PLAYER_SIZE = 40;
    private Font font = new Font("TimesRoman", Font.PLAIN, 30);
    private long start_t = System.currentTimeMillis();
    private long current_t = System.currentTimeMillis();
    private long level_t = (current_t - start_t)/1000;
    private Boolean next_level = false;
    private Boolean alien_touch = false;
    private Random random = new Random();
    private ArrayList<Stars> STARS = new ArrayList<>();
    private int STARS_MAX_SPEED = 10 * 10;
    private int STARS_MAX_SIZE = 3;
    private int NUM_OF_STARS = 100;


    /* Constructor for a Space Invaders game
     */
    public SpaceInvaders() {
        // fix the window size and background color
        this.canvasWidth = 1200;
        this.canvasHeight = 800;
        this.backgroundColor = Color.BLACK;
        setPreferredSize(new Dimension(this.canvasWidth, this.canvasHeight));

        // set the drawing timer
        this.timer = new Timer(msPerFrame, this);

        int si = 0;
        while (si < NUM_OF_STARS) {
            STARS.add(new Stars(random.nextInt(canvasHeight * 2), random.nextInt(canvasWidth), random.nextInt(STARS_MAX_SPEED) / (double) 10 + 0.1, random.nextInt(STARS_MAX_SIZE) + 1));
            si ++;
        }
        int i = 0;
        while (i < NUM_OF_LEVELS) {
            ALIENS = new ArrayList<>();
            int alienx = this.ALIEN_X_START;
            int alieny = this.ALIEN_Y_START;
            int ii = 0;
            while (ii < NUM_OF_ALIENS_PER_LEVEL) {
                AlienBaby alien = new AlienBaby(alienx, alieny, Math.pow(2, i) / (double) 10, this.ALIEN_SIZE);
                ALIENS.add(alien);
                alienx += 60;
                if (alienx > 860) {
                    if (alieny % 120 == 0){
                        alienx = this.ALIEN_X_START;
                        alieny += 60;
                    }
                    else {
                        alienx = this.ALIEN_X_START + this.ALIEN_SPACE;
                        alieny += 60;
                    }
                }
                ii ++;
            }
            this.ALIENS_EACH_LEVEL.add(ALIENS);
            i ++;
        }
        this.player = new Player(this.PLAYER_X_START, this.PLAYER_Y_START, this.PLAYER_SIZE);
    }

    /* Start the game
     */
    @Override
    public void run() {
        // show this window
        display();

        // set a timer to redraw the screen regularly
        this.timer.start();
    }

    /* Create the window and display it
     */
    private void display() {
        JFrame jframe = new JFrame();
        jframe.addKeyListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setContentPane(this);
        jframe.pack();
        jframe.setVisible(true);
    }

    /* Run all timer-based events
     *
     * @param e  An object describing the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // update the game objects
        update();
        // draw every object (this calls paintComponent)
        repaint(0, 0, this.canvasWidth, this.canvasHeight);
        // increment the frame counter
        this.frame++;
    }

    /* Paint/Draw the canvas.
     *
     * This function overrides the paint function in JPanel. This function is
     * automatically called when the panel is made visible.
     *
     * @param g The Graphics for the JPanel
     */
    @Override
    protected void paintComponent(Graphics g) {
        // clear the canvas before painting
        clearCanvas(g);
        if (hasWonGame()) {
            paintWinScreen(g);
        } else if (hasLostGame()) {
            paintLoseScreen(g);
        } else if (this.next_level) {
            paintNextLevelScreen(g);
        } else {
            paintGameScreen(g);
        }
    }

    /* Clear the canvas
     *
     * @param g The Graphics representing the canvas
     */
    private void clearCanvas(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, this.canvasWidth, this.canvasHeight);
        g.setColor(oldColor);
    }

    /* Respond to key release events
     *
     * A key release is when you let go of a key
     * 
     * @param e  An object describing what key was released
     */
    public void keyReleased(KeyEvent e) {
        // you can leave this function empty
    }

    /* Respond to key type events
     *
     * A key type is when you press then let go of a key
     * 
     * @param e  An object describing what key was typed
     */
    public void keyTyped(KeyEvent e) {
        // you can leave this function empty
    }

    /* Respond to key press events
     *
     * A key type is when you press then let go of a key
     * 
     * @param e  An object describing what key was typed
     */
    public void keyPressed(KeyEvent e) {
        if (!this.next_level) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (this.player.x > this.PLAYER_MOVE_SPEED) {
                    this.player.x -= this.PLAYER_MOVE_SPEED;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (this.player.x < this.canvasWidth - this.PLAYER_SIZE) {
                    this.player.x += this.PLAYER_MOVE_SPEED;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (this.pprojectiles.size() == 0) {
                    this.pprojectiles.add(new PProjectile(this.player.x + this.player.size / 2, this.player.y, this.PPROJECTILE_SPEED, this.PPROJECTILE_WIDTH, System.currentTimeMillis()));
                } else if ((System.currentTimeMillis() - this.pprojectiles.get(this.pprojectiles.size() - 1).time)/250 > this.PROJECTILE_DELAY) {
                    this.pprojectiles.add(new PProjectile(this.player.x + this.player.size / 2, this.player.y, this.PPROJECTILE_SPEED, this.PPROJECTILE_WIDTH, System.currentTimeMillis()));
                }
            }
        }
    }

    /* Update the game objects
     */
    private Boolean alienDead(int i) {
        for (PProjectile projectile : this.pprojectiles) {
            if (projectile.x < ALIENS.get(i).x + ALIENS.get(i).size &&
                    ALIENS.get(i).x < projectile.x + projectile.width &&
                    projectile.y < ALIENS.get(i).y + ALIENS.get(i).size &&
                    ALIENS.get(i).y < projectile.y) {
                this.ALIENS.remove(i);
                this.pprojectiles.remove(projectile);
                return true;
            }
        }
        return false;
    }
    private void update() {
        this.ALIENS = this.ALIENS_EACH_LEVEL.get(this.level);
        //check if projectiles hit any aliens and removes if it does
        int i = 0;
        while (i < ALIENS.size()) {
            if (!alienDead(i)) {
                i ++;
            }
        }
        //check to see if aliens reached edge of screen, switch directions + go down if true
        for(AlienBaby alien: ALIENS) {
            if (alien.x < -alien.speed_x ||
                    alien.x > this.canvasWidth - alien.size - alien.speed_x) {
                for(AlienBaby aliens : ALIENS) {
                    aliens.speed_x = -aliens.speed_x;
                    aliens.speed_y = 10;
                }
                break;
            }
            else {
                alien.speed_y = 0;
            }
        }
        for (AlienBaby alien: ALIENS) {
            alien.update(this.canvasWidth, this.canvasHeight, this.frame);
            if (random.nextInt(10000)/(this.level + 1) <= 1) {
                aprojectiles.add(new AProjectile(alien.x + alien.size / 2, alien.y + alien.size, this.APROJECTILE_SPEED + level / 2, this.APROJECTILE_SIZE));
            }
        }
        for (PProjectile projectile : pprojectiles) {
            projectile.update(this.canvasWidth, this.canvasHeight, this.frame);
            if (projectile.x < projectile.size - projectile.speed_y) {
                pprojectiles.remove(projectile);
            }
        }
        //make the projectiles split
        int ai = 0;
        int current_size = aprojectiles.size();
        while (ai < current_size) {
            if (random.nextInt(10000)/(Math.pow(this.level, 2) + 1) <= 1) {
                aprojectiles.get(ai).speed_x = -aprojectiles.get(ai).speed_y;
                AProjectile nprojectile = new AProjectile(aprojectiles.get(ai).x, aprojectiles.get(ai).y, aprojectiles.get(ai).speed_y, aprojectiles.get(ai).size);
                nprojectile.speed_x = -aprojectiles.get(ai).speed_x;
                aprojectiles.add(nprojectile);
            }
            ai ++;
        }
        for (AProjectile aprojectile : aprojectiles) {
            aprojectile.update(this.canvasWidth, this.canvasHeight, this.frame);
        }
        for (Stars star : STARS) {
            star.update(this.canvasWidth, this.canvasHeight, this.frame);
        }
    }

    /* Check if the player has lost the game
     * 
     * @returns  true if the player has lost, false otherwise
     */
    private boolean hasLostGame() {
        if (alien_touch) {
            return true;
        }
        for (AProjectile aprojectile : aprojectiles) {
            if ((this.player.x < aprojectile.x + aprojectile.size &&
                    aprojectile.x < this.player.x + this.player.size &&
                    this.player.y < aprojectile.y + aprojectile.size &&
                    aprojectile.y < this.player.y + this.player.size) ||
                (this.player.x < aprojectile.x + aprojectile.size/2 + aprojectile.width/2 &&
                        aprojectile.x - aprojectile.size/2 + aprojectile.width/2 < this.player.x + this.player.size &&
                        this.player.y < aprojectile.y + aprojectile.size/2 + aprojectile.width/2 &&
                        aprojectile.y + aprojectile.size/2 - aprojectile.width/2< this.player.y + this.player.size)) {
                this.alien_touch = true;
                return true;
            }
        }
        for (AlienBaby alien : ALIENS) {
            if (this.player.x < alien.x + alien.size &&
                    alien.x < this.player.x &&
                    this.player.y < alien.y + alien.size &&
                    alien.y < this.player.y) {
                this.alien_touch = true;
                return true;
            }
        }
        return false;
    }

    /* Check if the player has won the game
     * 
     * @returns  true if the player has won, false otherwise
     */
    private boolean hasWonGame() {
        return (this.level == this.NUM_OF_LEVELS - 1) && (ALIENS.size() == 0);
    }

    /* Paint the screen during normal gameplay
     *
     * @param g The Graphics for the JPanel
     */
    private void paintGameScreen(Graphics g) {
        this.ALIENS = this.ALIENS_EACH_LEVEL.get(this.level);
        for (Stars star : STARS) {
            star.draw(g);
        }
        for (AlienBaby alien: this.ALIENS) {
            alien.draw(g);
        }
        for (PProjectile projectile : this.pprojectiles) {
            projectile.draw(g);
        }
        for (AProjectile aprojectile : this.aprojectiles) {
            aprojectile.draw(g);
        }

        this.player.draw(g);
        if (this.ALIENS.size() == 0 && level < NUM_OF_LEVELS - 1) {
            this.next_level = true;
            this.player.speed_x = this.player.speed_x * 1.1;
            this.pprojectiles = new ArrayList<>();
            this.aprojectiles = new ArrayList<>();
            this.player.x = this.PLAYER_X_START;
            this.PROJECTILE_DELAY = this.PROJECTILE_DELAY * 0.8;
            this.start_t = System.currentTimeMillis();

        }
    }

    private void paintNextLevelScreen(Graphics g) {
        this.current_t = System.currentTimeMillis();
        this.level_t = (current_t - start_t)/1000;
        if (this.level_t <= 3) {
            String next = "The next level begins in \n" + Integer.toString(Math.round(3 - (int) this.level_t)) + " seconds";
            g.setFont(this.font);
            g.setColor(new Color(255, 255, 0));
            g.drawString(next, 380, 300);
            repaint(0, 0, this.canvasWidth, this.canvasHeight);
        }
        if (this.level_t > 3) {
            this.next_level = false;
            this.level++;
        }
    }

    /* Paint the screen when the player has won
     *
     * @param g The Graphics for the JPanel
     */
    private void paintWinScreen(Graphics g) {
        for (Stars star : STARS) {
            star.draw(g);
        }
        String win = "You have won!";
        g.setFont(font);
        g.setColor(new Color (255, 255, 0));
        g.drawString(win, 500,300);
    }

    /* Paint the screen when the player has lost
     *
     * @param g The Graphics for the JPanel
     */
    private void paintLoseScreen(Graphics g) {
        for (Stars star : STARS) {
            star.draw(g);
        }
        String lost = "Game over";
        g.setFont(font);
        g.setColor(new Color (255, 255, 0));
        g.drawString(lost, 500,300);
    }

    public static void main(String[] args) {
        SpaceInvaders invaders = new SpaceInvaders();
        EventQueue.invokeLater(invaders);
    }
}
