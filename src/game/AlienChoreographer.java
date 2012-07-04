package game;

/* AlienChoreographer.java:
 *
 * 	This class houses functions that define how
 * 	aliens are placed and how they move in each
 * 	level.
 */

import java.awt.Color;
import java.util.ArrayList;

public class AlienChoreographer
{
	/* useful spacing constants */
	public static final double horizontalSpacing = 20; /* pixels */
	public static final double verticalSpacing = 20; /* pixels */
	public static final int maxAliensPerRow 
				= (int) Math.ceil( 
					(Game.width - horizontalSpacing) 
					/(Alien.width + horizontalSpacing));
	
	/* makeAliensForLevel:
	 *
	 * Call this function to get the list of aliens for
	 * the current level */
	public static ArrayList<Alien> makeAliensForLevel(int level)
	{
		switch(level)
		{
			case 1:
			case 2:
				return makeBox(level, 1);
			case 3:
			case 4:
				return makeFlippingBox(level);
			case 5:
				return makeOval(level);
			case 6:
				return makeBox(2, 3);
			case 7:
				return makeFigureEight(level/2);
			case 8:
				return makeRace(level);
			case 9:
				return makeWave(level/3);
			case 10:
				return makeRandom(level);
			case 11:
				return makeOval(level);
			case 12:
				return makeFigureEight(level/2);
			default:
				return makeBox(3, level);
		}
	}

	public static ArrayList<Alien> makeBox(int level, int hp)
	{
		/* figure out how many aliens to place */
		int aliensPerRow = maxAliensPerRow;
		if(level < 5)
			aliensPerRow = level * maxAliensPerRow / 5;
		
		int numberAlienRows = 8;
		if(level + 4 < numberAlienRows)
			numberAlienRows = level + 4;

		/* determine the horizontal travel distance for
		 * these aliens */
		double ht = Game.width - aliensPerRow*(Alien.width 
					+ horizontalSpacing) 
					+ horizontalSpacing;

		/* make the path for the top-left alien */
		double s = 1 + level; /* speed (in pixels / frame) */
		int halfsize = 1 + (int) (Math.round(ht / s));
		double[] px = new double[2 * halfsize];
		double[] py = new double[ px.length ];
		for(int i = 0; i < halfsize; i++)
		{
			px[i] = s*i;
			py[i] = Alien.height;
			
			px[i+halfsize] = ht - px[i];
			py[i+halfsize] = py[i];
		}

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numberAlienRows; i++)
			for(int j = 0; j < aliensPerRow; j++)
			{
				Alien a = new Alien(px, py,
						0.002 * level,
						(horizontalSpacing 
						+ Alien.width)*j, 
						(verticalSpacing 
						+ Alien.height)*i);
				a.setHealth(hp);
				aliens.add(a);
			}

		/* return constructed choreography */
		return aliens;
	}
	
	public static ArrayList<Alien> makeFlippingBox(int level)
	{
		/* figure out how many aliens to place */
		int aliensPerRow = maxAliensPerRow;
		if(level < 5)
			aliensPerRow = level * maxAliensPerRow / 5;
		
		int numberAlienRows = 8;
		if(level + 4 < numberAlienRows)
			numberAlienRows = level + 4;

		/* determine the horizontal travel distance for
		 * these aliens */
		double ht = Game.width - Alien.width;

		/* make the path for the top-left alien */
		double s = 1 + level; /* speed (in pixels / frame) */
		int halfsize = 1 + (int) (Math.round(ht / s));
		double[] px = new double[2 * halfsize];
		double[] py = new double[ px.length ];
		for(int i = 0; i < halfsize; i++)
		{
			px[i] = s*i;
			py[i] = Alien.height;
			
			px[i+halfsize] = ht - px[i];
			py[i+halfsize] = py[i];
		}

		/* place aliens in new list */
		int indOffset = (int) Math.ceil( 
				(Alien.width + horizontalSpacing) / s );
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numberAlienRows; i++)
			for(int j = 0; j < aliensPerRow; j++)
			{
				Alien a = new Alien(px, py,
						0.001 * level,
						0, 
						(verticalSpacing 
						+ Alien.height)*i);
				a.moveToIndex(indOffset * j);
				aliens.add(a);
			}

		/* return constructed choreography */
		return aliens;
	}

	public static ArrayList<Alien> makeOval(int level)
	{
		/* figure out how many aliens to place */
		double v_rad = Game.height / 3;
		double h_rad = (Game.width - Alien.width) / 2;
		double circumference = (v_rad + h_rad) * Math.PI;
		int maxNumAliens = (int) Math.ceil(circumference 
				/ (horizontalSpacing + Alien.width));
		
		int numAliens = level * maxNumAliens / 10;
		if(level > 10)
			numAliens = maxNumAliens;	

		/* make the path for each alien */
		double s = 1 + level; /* speed of each alien */
		int len = (int) Math.round(circumference / s);
		double[] px = new double[ len ];
		double[] py = new double[ len ];
		for(int i = 0; i < len; i++)
		{
			px[i] = h_rad + h_rad*Math.cos(2*Math.PI * i/len);
			py[i] = v_rad + v_rad*Math.sin(2*Math.PI * i/len);
		}

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numAliens; i++)
		{
			Alien a = new Alien(px, py, 0.0005 * level);
			a.moveToIndex(i * len / numAliens); 
			
			if(level % 2 == 0)
				a.setHealth(2);

			aliens.add(a);
		}

		/* return constructed choreography */
		return aliens;
	}

	public static ArrayList<Alien> makeFigureEight(int level)
	{
		/* figure out how many aliens to place */
		double v_rad = Game.height / 3;
		double h_rad = (Game.width - Alien.width) / 2;
		double circumference = (v_rad + h_rad) * Math.PI;
		int maxNumAliens = (int) Math.ceil(circumference 
				/ (horizontalSpacing + Alien.width));
		
		int numAliens = level * maxNumAliens / 10;
		if(level > 10)
			numAliens = maxNumAliens;	

		/* make the path for each alien */
		double s = 1 + level; /* speed of each alien */
		int len = (int) Math.round(circumference / s);
		double[] px = new double[ len ];
		double[] py = new double[ len ];
		for(int i = 0; i < len; i++)
		{
			px[i] = h_rad + h_rad*Math.cos(2*Math.PI * i/len);
			py[i] = v_rad + v_rad*Math.sin(4*Math.PI * i/len);
		}

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numAliens; i++)
		{
			Alien a = new Alien(px, py, 0.001 * level);
			a.moveToIndex(i * len / numAliens); 
			
			if(level % 2 == 1)
				a.setHealth(2);
			else
				a.setHealth(3);

			aliens.add(a);
		}

		/* return constructed choreography */
		return aliens;
	}

	public static ArrayList<Alien> makeRace(int level)
	{
		/* figure out how many aliens to place */
		int numAliens = (int) ( 20 + 3*level*Math.random());

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numAliens; i++)
		{
			/* make straight line across screen at
			 * random height */
			int len = (int) ( ( Game.width + Alien.width ) 
					/ (1 + 9*Math.random()) );
			double[] px = new double[ len ];
			double[] py = new double[ len ];
			double h = Math.random() * Game.height * 2/3;
			for(int j = 0; j < len; j++)
			{
				px[j] = (Game.width + Alien.width)*j/len 
							- Alien.width;
				py[j] = h;
			}

			/* make a new alien */
			Alien a = new Alien(px, py, 0.0005 * level);
			a.setHealth(3);
			a.moveToIndex( (int) (Math.random() * len) );
			aliens.add(a);
		}

		/* return constructed choreography */
		return aliens;
	}

	public static ArrayList<Alien> makeWave(int level)
	{
		/* figure out how many aliens to place */
		int numAliens = (int) ((Game.width + horizontalSpacing)
				/ (Alien.width + horizontalSpacing));

		/* make the path for each alien */
		double s = 1 + level; /* speed of each alien */
		int len = (int) Math.round((Game.height * 2/3) / s);
		double[] px = new double[ len ];
		double[] py = new double[ len ];
		for(int i = 0; i < len; i++)
		{
			px[i] = 0;
			py[i] = Game.height * 5/12 
				* (1 + Math.sin(2*Math.PI*i/len));
		}

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numAliens; i++)
		{
			Alien a = new Alien(px, py, 0.005 * level,
				(Alien.width + horizontalSpacing) * i, 0);
			a.moveToIndex(i * len / numAliens); 
			a.setHealth(3);
			aliens.add(a);
		}

		/* return constructed choreography */
		return aliens;
	}
	
	public static ArrayList<Alien> makeRandom(int level)
	{
		/* figure out how many aliens to place */
		int numAliens = 2*level;

		/* make the path for each alien */
		int len = 10*level;

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numAliens; i++)
		{
			/* make random set of coordinates */
			double[] px = new double[ len ];
			double[] py = new double[ len ];
			for(int j = 0; j < len; j++)
			{
				/* stay at each location for a few
				 * timesteps */
				if(j % 30 == 0)
				{
					px[j] = (Game.width - Alien.width)
						* Math.random();
					py[j] = (Game.height * 3/4)
						* Math.random();
				}
				else
				{
					px[j] = px[j-1];
					py[j] = py[j-1];
				}
			}

			/* make new alien */
			Alien a = new Alien(px, py, 0.001 * level);
			aliens.add(a);
		}

		/* return constructed choreography */
		return aliens;
	}
}
