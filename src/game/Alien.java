package game;

import java.awt.Graphics;
import java.awt.Color;

/* Alien.java
 *
 * This class represents the individual aliens 
 * in the game.
 */

public class Alien
{
	/* Default values */
	public static final double width = 20; /* pixels */
	public static final double height = 10; /* pixels */
	public static final double verticalTravel = 30; /* pixels */

	/* position and dimensions */
	public double x; /* left-hand side */
	public double y; /* top */
	public double horizontalTravel; /* pixels */
	public double speed; /* pixels */
	public double attackProbability; /* between 0 and 1 */

	/* travel itinerary */
	public double xPosMin;
	public double xPosMax;
	public boolean movingRight;

	/*** Constructors ***/
	public Alien(double px, double py, double ht, double s, double p)
	{
		/* store characteristics */
		x = px;
		y = py;
		horizontalTravel = ht;
		speed = s;
		attackProbability = p;

		/* calculate travel plan */
		xPosMin = x;
		xPosMax = x + horizontalTravel;
		movingRight = true;
	}

	/*** movement ***/

	public void move()
	{
		boolean flip = false;

		/* move */
		if(movingRight)
		{
			moveRight();
			flip = (x > xPosMax);	
		}
		else
		{
			moveLeft();
			flip = (x < xPosMin);
		}

		/* check if we should reverse */
		if(flip)
		{
			moveDown();
			movingRight = !movingRight;
		}
	}

	public Phaser makePhaser()
	{
		if(Math.random() < attackProbability)
		{
			return new Phaser(x + width/2 - Phaser.width/2,
					y + height);
		}
		return null;
	}

	public void moveLeft()
	{
		x -= speed;
	}

	public void moveRight()
	{
		x += speed;
	}

	public void moveDown()
	{
		y += verticalTravel;
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
		g.setColor(Color.green);
		g.fillRect((int) x, (int) y, 
				(int) width, (int) height);
		g.setColor(Color.black);
		g.fillRect((int) (x+width/4 - 1), (int) (y+height/2 - 1),
						3, 3);
		g.fillRect((int) (x+width*3/4 - 1), (int) (y+height/2 - 1),
						3, 3);
	}
}
