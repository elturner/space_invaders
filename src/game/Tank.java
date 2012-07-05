package game;

import java.awt.Graphics;
import java.awt.Color;

/* Tank.java
 *
 * This is the player-controlled tank.
 */

public class Tank
{
	/* Default values */
	public static final double width = 20; /* pixels */
	public static final double height = 10; /* pixels */
	public static final double defaultSpeed = 5; /* pixels */
	public static final double defaultAmmoRechargeRate = 0.2;
						/* ammo per frame */
	public static final double defaultAmmoReloadTime = 8;
					/* # of frames between shots */
	public static final double defaultMaxAmmo = 15;
	
	public static final Color bodyColor = Color.green.darker().darker();
	public static final Color trimColor = Color.gray.brighter();
	public static final Color shieldColor = new Color(0, 150, 230, 60);

	public static final int EXPLODE_TIME = 30; /* timesteps */
	public static final int SHIELD_TIME = 400; /* timesteps */
	public static final int NUM_EXPLOSIONS_PER_FRAME = 3;

	/* position and dimensions */
	public double x; /* left-hand side */
	public double y; /* top */
	private double speed; /* pixels */

	/* tank state */
	private TankState currentState;
	private int stateCounter; /* how many frames has the current
					state been active? */
	private double ammo; /* how many shot you have left */
	private double ammoRechargeRate;
	private double ammoReloadTime; /* # of frames between shots */
	private double ammoReloadStage; /* # of frames until next shot */
	private double maxAmmo;
	private double ammoSizeFactor; /* how big are the shells */
	private boolean overheated;

	/*** Constructors ***/
	public Tank(double px, double py)
	{
		x = px;
		y = py;
		speed = defaultSpeed;
		
		stateCounter = 0;
		currentState = TankState.HEALTHY;
		
		maxAmmo = defaultMaxAmmo;
		ammo = maxAmmo;
		ammoRechargeRate = defaultAmmoRechargeRate;
		ammoReloadTime = defaultAmmoReloadTime;
		ammoReloadStage = 0;
		ammoSizeFactor = 1;
		overheated = false;
	}

	/**** weapons / state ****/
	
	/* getHit:
	 *
	 * 	Call when hit by enemy fire.
	 */
	public void getHit()
	{
		/* check how getting hit affects current state */
		switch(currentState)
		{
			case HEALTHY:
				/* oh no, we got hit!  we're going to
				 * explode! */
				currentState = TankState.EXPLODING;
				stateCounter = 0;
				break;
			default:
				/* all other states are not affected */
				break;
		}
	}

	/* isDead:
	 *
	 * 	Returns true if dead.
	 */
	public boolean isDead()
	{
		return (currentState == TankState.DEAD);
	}

	/* updateState:
	 *
	 * 	Call every frame.  Will keep track
	 * 	of the current tank state.
	 */
	public void updateState()
	{
		/* update counter */
		stateCounter++;

		/* check if we need to change state */
		switch(currentState)
		{
			case SHIELDED:
				/* check if shield wore off */
				if(stateCounter > SHIELD_TIME)
				{
					currentState = TankState.HEALTHY;
					stateCounter = 0;
				}
			case HEALTHY:
				
				/* reload if necessary */
				if(ammoReloadStage > 0)
					ammoReloadStage--;
				else if(ammo < maxAmmo) /* update ammo */
				{
					ammo += ammoRechargeRate;
					if(!overheated)
						ammo += 2*ammoRechargeRate;
					else if(overheated
						&& ammo > 0.5 * maxAmmo)
						overheated = false;
					if(ammo > maxAmmo)
						ammo = maxAmmo;
				}
				break;
			case EXPLODING:
				/* check if dead yet */
				if(stateCounter > EXPLODE_TIME)
				{
					currentState = TankState.DEAD;
					stateCounter = 0;
				}
				break;
			case DEAD:
				/* dead is dead */
				break;
		}
	}

	/* makeShell:
	 *
	 * make a new shell to be fired from current position */
	public Shell makeShell()
	{
		/* can only fire if ammo available */
		if(overheated || ammo <= 0)
		{
			overheated = true;
			return null;
		}

		/* make sure not reloading */
		if(ammoReloadStage > 0)
			return null;

		/* can only fire in certain states */
		switch(currentState)
		{
			case HEALTHY:
			case SHIELDED:

				/* spend one unit of ammo */
				ammo -= 1;
				ammoReloadStage = ammoReloadTime;

				/* create new shell */
				Shell s = new Shell(x + width/2 
					- ammoSizeFactor
					  	*Shell.defaultWidth/2,
					y - Shell.defaultHeight
						*ammoSizeFactor);
				
				/* set shell size */
				s.width *= ammoSizeFactor;
				s.height *= ammoSizeFactor;

				/* return this shell */
				return s;
			default:
				return null;
		}
	}

	/*** accessors ***/

	public double ammoPercentage()
	{
		return ammo / maxAmmo;
	}

	public boolean overheated()
	{
		return overheated;
	}

	public void setAmmoRechargeRate(double arr)
	{
		ammoRechargeRate = arr;
	}

	public void setAmmoReloadTime(double art)
	{
		ammoReloadTime = art;
	}

	public void setShielded()
	{
		currentState = TankState.SHIELDED;
		stateCounter = 0;
	}

	public void increaseSpeed()
	{
		speed += 2;
	}

	public void increaseAmmoSizeFactor()
	{
		ammoSizeFactor++;
	}

	/*** movement ***/
	
	public void moveLeft(double leftWall)
	{
		switch(currentState)
		{
			case HEALTHY:
			case SHIELDED:
				if(x - speed > leftWall)
					x -= speed;
			default:
				break;
		}
	}

	public void moveRight(double rightWall)
	{
		switch(currentState)
		{
			case HEALTHY:
			case SHIELDED:
				if(x + width + speed < rightWall)
					x += speed;
			default:
				break;
		}
	}

	/* check if this tank collides with the given bounding rectangle,
	 * where (ox,oy) represents the top-left corner */
	public boolean collidesWith(double ox, double oy, 
					double ow, double oh)
	{
		return ( (ox + ow >= x) && (x + width >= ox) 
				&& (oy + oh >= y) && (y + height >= oy) );
	}

	/*** rendering ***/

	/* render:
	 *
	 * 	Draw tank at current position
	 */
	public void render(Graphics g)
	{
		render(g, x, y);
	}

	/* render:
	 *
	 * 	Draw tank at given position.
	 */
	public void render(Graphics g, double xp, double yp)
	{
		switch(currentState)
		{
			case SHIELDED:
				/* draw shield behind tank */
				g.setColor(shieldColor);
				g.fillOval((int) (xp - width/2),
						(int) (yp - height/2),
						(int) (2*width),
						(int) (2*height));
			case HEALTHY:
				/* draw tank */
				g.setColor(Color.gray);
				g.drawLine((int) (xp + width/2), (int) yp, 
					(int) (xp + width/2), 
					(int) (yp - height*(1 -
						(ammoReloadStage
						/ (1+ammoReloadTime)))));
				g.setColor(bodyColor);
				g.fillRect((int) xp, (int) yp, 
					(int) width, (int) height);
				g.setColor(Color.gray.darker());
				g.fillRoundRect((int) xp, 
					(int) (yp + height/2), (int) width,
					(int) (height/2), 2, 2);
				g.setColor(trimColor);
				g.drawRect((int) xp, 
					(int) yp, (int) width, 
					(int) height);
				g.drawLine((int) (xp + width/4), 
					(int) (yp-1), 
					(int) (xp + 3*width/4),
					(int) (yp-1));
				break;
			case EXPLODING:
				for(int i = 0; 
					i < NUM_EXPLOSIONS_PER_FRAME; i++)
				{
					g.setColor(new Color(200 
						+ (int)(56*Math.random()), 
						(int) (100*Math.random()), 
						0));
					double cx = 2*width*Math.random()
							- width/2;
					double cy = height*Math.random();
					double r = height*Math.random();
					g.fillOval((int)(xp + cx - r), 
						(int) (yp + cy - r),
						(int) (2*r), (int) (2*r));
				}
				
				break;
			case DEAD:
				g.setColor(Color.gray);
				g.fillRect((int) (xp + width/4),
						(int) (yp + height/2),
						(int) (width/2),
						(int) (height/2));
				break;
		}
	}

	/* TankStates:
	 *
	 * This enum represents the different states a tank can
	 * be in at any given time.
	 */
	public enum TankState
	{
		HEALTHY, EXPLODING, DEAD, SHIELDED
	}
}
