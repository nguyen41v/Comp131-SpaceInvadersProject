import java.awt.*;

public class Stars extends GraphicsObject {
    int size;
    Color color = Color.LIGHT_GRAY;

    public Stars (double x, double y, double speed_y, int size) {
        super(x, y);
        this.speed_y = speed_y;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        int nx = (int) x;
        int ny = (int) y;
        g.fillOval(nx, ny, size, size);
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
        if (this.y > pic_height * 2) {
            this.y = 0 - this.size * 2;
        }
    }
}

