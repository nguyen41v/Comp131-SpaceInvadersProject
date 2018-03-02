import java.awt.Color;
import java.awt.Graphics;


public class AlienBaby extends GraphicsObject {

    Color color = new Color(15,253,103);
    int size;


    public AlienBaby(double x, double y, double speed_x, int size) {
        super(x, y);
        this.speed_x = speed_x;
        this.size = size;
    }

    /* Draw the object
     *
     * @param g The Graphics for the JPanel
     */
    public void draw(Graphics g) {
        g.setColor(this.color);
        int nx = (int) x;
        int ny = (int) y;
        g.fillRect(nx, ny, this.size, this.size);
    }

    /* Update the object's location based on its speed
     *
     * @param pic_width   The width of the drawing window
     * @param pic_height  The height of the drawing window
     * @param frame       The number of frames since the start of the program
     */
    public void update(int pic_width, int pic_height, int frame) {
        this.x += this.speed_x;
        this.y += this.speed_y;
    }
}
