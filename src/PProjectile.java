import java.awt.Color;
import java.awt.Graphics;


public class PProjectile extends GraphicsObject {
    int size = 8;
    int width;
    Color color = new Color(225, 0, 196);
    long time;

    public PProjectile(double x, double y, double speed_y, int width, long time) {
        super(x, y);
        this.speed_y = -speed_y;
        this.width = width;
        this.time = time;
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        int nx = (int) x;
        int ny = (int) y;
        g.fillRect(nx, ny, this.width, this.size);
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
