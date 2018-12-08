//import com.sun.org.apache.regexp.internal.REDebugCompiler;

import java.awt.Color;
import java.awt.Graphics;

public class AProjectile extends GraphicsObject {
    int size = 8;
    int width;
    Color color = Color.RED;

    public AProjectile(double x, double y, double speed_y, int width) {
        super(x, y);
        this.speed_y = speed_y;
        this.width = width;
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        int nx = (int) x;
        int ny = (int) y;
        g.fillRect(nx, ny, this.width, this.size);
        g.fillRect(nx - this.size/2 + this.width/2, ny + this.size/2 - this.width/2, this.size, this.width);
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
