package game;

/* AlienChoreographer.java:
 *
 * 	This class houses functions that define how
 * 	aliens are placed and how they move in each
 * 	level.
 */

import java.util.ArrayList;

public class AlienChoreographer
{
	public static final double horizontalSpacing = 20; /* pixels */
	public static final double verticalSpacing = 20; /* pixels */
	public static final int maxAliensPerRow 
				= (int) Math.ceil( 
					(Game.width - horizontalSpacing) 
					/(Alien.width + horizontalSpacing));
	public static final int maxNumberAlienRows = 8;
	
	/* makeAliensForLevel:
	 *
	 * Call this function to get the list of aliens for
	 * the current level */
	public static ArrayList<Alien> makeAliensForLevel(int level)
	{
		/* figure out how many aliens to place */
		int aliensPerRow = maxAliensPerRow;
		if(level < 5)
			aliensPerRow = level * maxAliensPerRow / 5;
		
		int numberAlienRows = maxNumberAlienRows;
		if(level + 4 < maxNumberAlienRows)
			numberAlienRows = level + 4;

		/* determine the horizontal travel distance for
		 * these aliens */
		double ht = Game.width - aliensPerRow*(Alien.width 
					+ horizontalSpacing) 
					- horizontalSpacing;

		/* place aliens in new list */
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for(int i = 0; i < numberAlienRows; i++)
			for(int j = 0; j < aliensPerRow; j++)
			{
				aliens.add(new Alien(Alien.width 
						+ (horizontalSpacing 
						+ Alien.width)*j, 
						Alien.height 
						+ (verticalSpacing 
						+ Alien.height)*i,
						ht, level * 0.5, 
						0.002 * level));
			}

		/* return constructed choreography */
		return aliens;
	}
}
