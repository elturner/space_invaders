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
		if(level < 4)
			return makeBox(level, 1);
		if(level < 6)
			return makeFlippingBox(level);
		if(level < 8)
			return makeOval(level);
		if(level < 9)
			return makeBox(2, 3);
		return makeBox(3, level);
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

}
