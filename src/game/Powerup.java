package game;

/* Powerup.java:
 *
 * An object of this class represents
 * a power-up in the game.  When a tank
 * collides with a power-up, it upgrades
 * the tank's characteristics randomly. */

import java.awt.*;

public class Powerup
{
	/* The different types of power-ups */
	public static final int SHIELDS = 0;
	public static final int SPEEDBOOST = 1;
	public static final int AMMOBOOST = 2;
	public static final int RAPIDFIRE = 3;
	public static final int BIGGERAMMO = 4;
	public static final int NUM_POWERUP_TYPES = 5;

	/* constants */
	public static final int lifespan = 400;
	public static final double width = 15;
	public static final double height = 15;
	public static final double speed = 5;
	public static final double dropRate = 0.02;

	/* characteristics */
	private int type;
	private int timeToLive;
	public double x;
	public double y;

	/*** constructors ***/

	public Powerup(double px, double py)
	{
		x = px;
		y = py;
		type = -1; /* unchosen */
		timeToLive = lifespan;
	}

	/*** movement ***/

	public boolean isExpired()
	{
		return (timeToLive <= 0);
	}

	public void update()
	{
		/* check for expiration */
		timeToLive--;

		/* check if on ground */
		if(type < 0 && y < (Game.height - height))
		{
			/* fall down */
			y += speed;
			
			/* check if fell too far */
			if(y > (Game.height - height))
				y = Game.height - height;
		}
	}

	/* check if this powerup collides with the given bounding rectangle,
	 * where (ox,oy) represents the top-left corner */
	public boolean collidesWith(double ox, double oy, 
					double ow, double oh)
	{
		return ( (ox + ow >= x) && (x + width >= ox) 
				&& (oy + oh >= y) && (y + height >= oy) );
	}

	/*** activate power-up on a tank ***/

	/* upgradeTank:
	 *
	 * 	Upgrades this tank.
	 */
	public void upgradeTank(Tank t)
	{
		/* check if this powerup already has a type */
		if(type >= 0)
			return;

		/* pick a type for this power-up */
		type = (int) (Math.random() * NUM_POWERUP_TYPES);

		/* upgrade tank based on type */
		switch(type)
		{
			case SHIELDS:
				/* turn the shields on for this tank */
				t.setShielded();
				timeToLive = 30;
				break;
			case SPEEDBOOST:
				/* set tank to double-speed */
				t.increaseSpeed();
				timeToLive = 30;
				break;
			case AMMOBOOST:
				/* set recharge rate for tank ammo
				 * to be huge (so that it instantly
				 * recharges */
				t.setAmmoRechargeRate(100);
				timeToLive = 30;
				break;
			case RAPIDFIRE:
				/* set reload rate to 0 */
				t.setAmmoReloadTime(0);
				timeToLive = 30;
				break;
			case BIGGERAMMO:
				/* increase the size of the shells
				 * that this tank fires */
				t.increaseAmmoSizeFactor();
				timeToLive = 30;
				break;
			default:
				System.out.println(
					"Unknown powerup type: " + type);
				timeToLive = 30;
				break;
		}
	}

	/*** rendering ***/

	public void render(Graphics g)
	{
		switch(type)
		{
			case -1:
				/* draw a powerup box */
				if(timeToLive > 100 || (timeToLive/5)%2==0)
				{
					/* this if-statement will cause
					 * box to flash when nearly
					 * expired */
					g.setColor(Color.orange);
					g.fillRect((int)x, (int)y,
						(int)width, (int)height);
					g.setColor(Color.white);
					g.drawRect((int)x, (int)y,
						(int)width, (int)height);
					g.setFont(new Font("powerup",
						Font.PLAIN, 14));
					g.drawString("?",
						(int)(x+width/2-2),(int) y);
				}
				break;
			case SHIELDS:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Shields", (int)x, 
						(int)(y-30+timeToLive));
				break;
			case SPEEDBOOST:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Speed Boost", (int)x, 
						(int)(y-30+timeToLive));
				break;
			case AMMOBOOST:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Ammo Boost", (int)x, 
						(int)(y-30+timeToLive));
				break;
			case RAPIDFIRE:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Rapid Fire", (int)x, 
						(int)(y-30+timeToLive));
				break;
			case BIGGERAMMO:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Bigger Ammo", (int)x, 
						(int)(y-30+timeToLive));
				break;
			default:
				g.setColor(Color.white);
				g.setFont(new Font("powerup",
					Font.PLAIN, 14));
				g.drawString("Unknown Powerup", (int)x, 
						(int)(y-30+timeToLive));
				break;
		}
	}
}
