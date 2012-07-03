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
	public static final Color deadColor = Color.gray;
	public static final Color oneHitColor = Color.green;
	public static final Color twoHitColor = Color.red;
	public static final Color manyHitColor = Color.blue;
	public static final int deathTimerLength = 30;

	/* position and dimensions */
	public double x; /* left-hand side */
	public double y; /* top */
	public double attackProbability; /* between 0 and 1 */

	/* travel itinerary */
	private double[] pathX;
	private double[] pathY;
	private int pathInd;

	/* stats */
	private int health;	
	private int deathTimer;

	/*** Constructors ***/

	/* make stationary alien */
	public Alien(double px, double py, double p)
	{
		/* store characteristics */
		x = px;
		y = py;
		attackProbability = p;

		/* calculate travel plan */
		pathX = new double[1];
		pathY = new double[1];
		pathX[0] = x;
		pathY[0] = y;
		pathInd = 0;
	
		/* default health */
		health = 1;
		deathTimer = deathTimerLength; 
	}

	/* make alien that follows path */
	public Alien(double[] px, double[] py, double p)
	{
		/* store characteristics */
		x = px[0];
		y = py[0];
		attackProbability = p;

		/* calculate travel plan */
		pathX = new double[px.length];
		pathY = new double[py.length];
		pathInd = 0;
		for(int i = 0; i < pathX.length; i++)
		{
			pathX[i] = px[i];
			pathY[i] = py[i];
		}
	
		/* default health */
		health = 1;
		deathTimer = deathTimerLength; 
	}

	/* make alien that follows path with given offset */
	public Alien(double[] px, double[] py, double p, 
				double offx, double offy)
	{
		/* store characteristics */
		x = px[0];
		y = py[0];
		attackProbability = p;

		/* calculate travel plan */
		pathX = new double[px.length];
		pathY = new double[py.length];
		pathInd = 0;
		for(int i = 0; i < pathX.length; i++)
		{
			pathX[i] = px[i] + offx;
			pathY[i] = py[i] + offy;
		}
	
		/* default health */
		health = 1;
		deathTimer = deathTimerLength; 
	}

	public void setHealth(int h)
	{
		health = h;
		deathTimer = deathTimerLength; 
	}

	/*** movement ***/

	/* move to next index in path */
	public void move()
	{
		/* check if dead.  If so, display corpse for some time */
		if(health <= 0)
			deathTimer--;

		/* move to next position */
		pathInd++;
		pathInd %= pathX.length;
		
		/* get coordinates for this position */
		x = pathX[pathInd];
		y = pathY[pathInd];
	}

	/* moves to index in path */
	public void moveToIndex(int i)
	{
		pathInd = (i % pathX.length);
		x = pathX[pathInd];
		y = pathY[pathInd];
	}

	/* makePhaser:
	 *
	 * 	Will cause this alien to attempt to fire phasers.
	 * 	Return value is null on failure, non-null on success.
	 */
	public Phaser makePhaser()
	{
		if(Math.random() < attackProbability)
		{
			return new Phaser(x + width/2 - Phaser.width/2,
					y + height);
		}
		return null;
	}

	/*** health ***/

	/* check if this alien collides with the given bounding rectangle,
	 * where (ox,oy) represents the top-left corner */
	public boolean collidesWith(double ox, double oy, 
					double ow, double oh)
	{
		return ( (ox + ow >= x) && (x + width >= ox) 
				&& (oy + oh >= y) && (y + height >= oy) );
	}

	/* getHit:
	 *
	 * 	call when alien hit by shell.
	 */
	public void getHit()
	{
		if(health > 0)
			health--;
	}

	/* isAlive:
	 *
	 * 	check if alien is still alive.
	 */
	public boolean isAlive()
	{
		return (deathTimer > 0);
	}

	/*** rendering ***/

	public void render(Graphics g)
	{
		/* set color based on health */
		switch(health)
		{
			case 0:
				g.setColor(deadColor);
				break;
			case 1:
				g.setColor(oneHitColor);
				break;
			case 2:
				g.setColor(twoHitColor);
				break;
			default:
				g.setColor(manyHitColor);
				break;
		}

		/* draw alien */
		g.fillRect((int) x, (int) y, 
				(int) width, (int) height);
		g.setColor(Color.black);
		g.fillRect((int) (x+width/4 - 1), (int) (y+height/2 - 1),
						3, 3);
		g.fillRect((int) (x+width*3/4 - 1), (int) (y+height/2 - 1),
						3, 3);
	}
}
