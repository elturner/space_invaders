package game;

import java.awt.Graphics;
import java.awt.Color;

/* Phaser.java:
 *
 * An object of this class represents a shot
 * from the attacking aliens directed downwards
 */

public class Phaser
{
	/* Default values */
	public static final double width = 7; /* pixels */
	public static final double height = 7; /* pixels */
	public static final double speed = 5; /* pixels, should be
						* smaller than height */

	/* position and dimensions */
	public double x; /* left-hand side */
	public double y; /* top */
	
	/*** Constructors ***/
	public Phaser(double px, double py)
	{
		/* store characteristics */
		x = px;
		y = py;
	}

	/*** movement ***/

	public void moveDown()
	{
		y += speed;
	}

	/* check if this alien collides with the given bounding rectangle,
	 * where (ox,oy) represents the top-left corner */
	public boolean collidesWith(double ox, double oy, 
					double ow, double oh)
	{
		return ( (ox + ow >= x) && (x + width >= ox) 
				&& (oy + oh >= y) && (y + height >= oy) );
	}

	/*** rendering ***/

	public void render(Graphics g)
	{
		g.setColor(Color.orange);
		g.fillOval((int) x, (int) y, (int) width, (int) height);
	}
}
