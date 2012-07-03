package game;

/* Game.java:
 *
 * This class represents the core mechanics of
 * gameplay.
 */

import animator.AnimatedElement;
import gui.Display;
import gui.Controller;

import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Game implements AnimatedElement
{
	/* constants */
	public static final int width = 1200;
	public static final int height = 800;
	public static final String bgimage = "../files/images/stars.gif";

	public static final String CONTROL_PAUSE = "pause";
	public static final String CONTROL_LEFT = "left";
	public static final String CONTROL_RIGHT = "right";
	public static final String CONTROL_FIRE = "fire";
	public static final String CONTROL_BOMB = "bomb";
	public static final String CONTROL_START = "start";

	/* gui */
	private Controller controller;
	private Display display;
	private ImageIcon background;

	/* game elements */
	private ArrayList<Alien> aliens;
	private ArrayList<Phaser> phasers;
	private ArrayList<Shell> shells;
	private Tank tank;

	/* game state */
	private GameState currentState;

	/* game stats */
	private int level;
	private int lives;

	/*** constructor ***/

	public Game(Display d, Controller c)
	{
		/* set up user input */
		controller = c;
		display = d;
	
		/* set up graphics */
		background = new ImageIcon(bgimage);

		/* set up game elements */
		currentState = GameState.MENU;
	}

	/* initGameElements:
	 *
	 * arranges the field of play
	 * for a new level, specified as
	 * the argument. */
	public void initGameElements()
	{
		/* initialize objects */
		phasers = new ArrayList<Phaser>();
		shells = new ArrayList<Shell>();
		tank = new Tank(width/2 - Tank.width/2,
					height - 2*Tank.height);
	
		/* populate aliens */
		aliens = AlienChoreographer.makeAliensForLevel(level);
	}

	/*** gameplay ***/

	public void doFrame(long uptimeMillis)
	{
		/* What to do depends on the current state of the game */
		switch(currentState)
		{
			case PLAYING:
				playFrame(uptimeMillis);
				break;
			case PAUSED:
				pauseFrame(uptimeMillis);
				break;
			case MENU:
				menuFrame(uptimeMillis);
				break;
		}
	}

	/* playFrame:
	 *
	 * What to do each frame of the gameplay.  Includes movement,
	 * game mechanics, and rendering */
	public void playFrame(long uptimeMillis)
	{
		/* check if player dead */
		tank.updateState();
		if(tank.isDead())
		{
			lives--;

			/* see if we are out of lives */
			if(lives <= 0)
			{
				/* end current game */
				currentState = GameState.MENU;
				return;
			}
			else
			{
				/* try current level again */
				initGameElements();
			}
		}

		/* check if all aliens dead */
		if(aliens.isEmpty())
		{
			level++;
			initGameElements();
		}
		
		/* check collisions between shells and phasers */
		for(Iterator<Phaser> itp = phasers.iterator(); 
					itp.hasNext();)
		{
			Phaser p = itp.next();
			for(Iterator<Shell> its = shells.iterator();
					its.hasNext(); )
			{
				Shell s = its.next();
				if(p.collidesWith(s.x, s.y, 
						s.width, s.height))
				{
					itp.remove();
					its.remove();
					break;
				}
			}
		}

		/* check collisions between tank and phasers */
		for(Iterator<Phaser> itp = phasers.iterator(); 
				itp.hasNext();)
		{
			Phaser p = itp.next();
			if(tank.collidesWith(p.x, p.y, p.width, p.height))
			{
				tank.getHit();
				itp.remove();
			}
		}

		/* check collisions between tank and aliens */
		for(Iterator<Alien> ita = aliens.iterator(); ita.hasNext();)
		{
			Alien a = ita.next();
			if(tank.collidesWith(a.x, a.y, a.width, a.height))
			{
				tank.getHit();
				ita.remove();
			}
		}
	
		/* check collisions between shells and aliens */
		for(Iterator<Alien> ita = aliens.iterator(); ita.hasNext();)
		{
			Alien a = ita.next();
			for(Iterator<Shell> its = shells.iterator();
					its.hasNext(); )
			{
				Shell s = its.next();
				if(a.collidesWith(s.x, s.y, 
						s.width, s.height))
				{
					ita.remove();
					its.remove();
					break;
				}
			}
		}
		
		/* check if tank fires weapon */
		if(controller.isToggled(CONTROL_FIRE))
		{
			Shell s = tank.makeShell();
			if(s != null)
				shells.add(s);
		}
		// TODO bomb?

		/* check if aliens fire weapons */
		for(Iterator<Alien> ita = aliens.iterator(); ita.hasNext();)
		{
			Alien a = ita.next();
			Phaser p = a.makePhaser();
			if(p != null)
				phasers.add(p);
		}

		/* move aliens */
		for(Iterator<Alien> ita = aliens.iterator(); ita.hasNext();)
		{
			Alien a = ita.next();
			a.move();
		}

		/* move tank */
		if(controller.isToggled(CONTROL_LEFT))
			tank.moveLeft(0);
		if(controller.isToggled(CONTROL_RIGHT))
			tank.moveRight(width);

		/* move shells */
		for(Iterator<Shell> its = shells.iterator(); its.hasNext();)
		{
			/* move current shell */
			Shell s = its.next();
			s.moveUp();

			/* check if out of bounds */
			if(s.y < 0)
				its.remove();
		}

		/* move phasers */
		for(Iterator<Phaser> itp = phasers.iterator(); 
				itp.hasNext(); )
		{
			/* move current phaser */
			Phaser p = itp.next();
			p.moveDown();

			/* check if out of bounds */
			if(p.y > height)
				itp.remove();
		}

		/* render */
		renderPlay();

		/* check for pause */
		if(controller.isActivated(CONTROL_PAUSE))
			currentState = GameState.PAUSED;

		/* reset all persistant controls */
		controller.resetAllPersistant();
	}
	
	/* render:
	 *
	 * Renders the current frame */
	public void renderPlay()
	{
		/* get renderer */
		Graphics g = display.draw();

		/* render background */
		g.setColor(Color.black);
		if(background == null)
			g.fillRect(0, 0, width, height);
		else
		{
			int w = background.getIconWidth();
			int h = background.getIconHeight();
			int nx = (int) Math.ceil( ((double) width) / w );
			int ny = (int) Math.ceil( ((double) height) / h );
			
			for(int i = 0; i < nx; i++)
				for(int j = 0; j < ny; j++)
					g.drawImage(background.getImage(), 
							i*w, j*h, null);
		}

		/* render shells */
		for(int i = 0; i < shells.size(); i++)
			shells.get(i).render(g);

		/* render phasers */
		for(int i = 0; i < phasers.size(); i++)
			phasers.get(i).render(g);

		/* render aliens */
		for(int i = 0; i < aliens.size(); i++)
			aliens.get(i).render(g);

		/* render tank */
		tank.render(g);

		/* display game statistics */
		renderStats();

		/* display frame to screen */
		display.repaint();
	}

	public void renderStats()
	{
		Graphics g = display.draw();

		/* display level */
		g.setFont(new Font("lives font", Font.PLAIN, 18));
		g.setColor(Color.white);
		g.drawString("Level " + level, 18, 20);
		
		/* Draw number of lives */
		g.setColor(Color.black);
		g.fillRect(20, 50, 50, 50);
		g.setColor(Color.white);
		g.drawRect(20, 50, 50, 50);
		g.drawString("" + lives, 40, 68);
		tank.render(g, 35, 85);
	
		/* draw ammo */
		int ap = (int) (100 * tank.ammoPercentage());
		g.setColor(tank.overheated() ? Color.red : Color.yellow);
		g.fillRect(40, 220 - ap, 10, ap);
		g.setColor(Color.white);
		g.drawRect(40, 120, 10, 100);
	}

	/* pauseFrame:
	 *
	 * 	What to do when paused.  Will display
	 * 	the pause icon and check for unpause.
	 */
	public void pauseFrame(long uptimeMillis)
	{
		/* display pause icon */
		renderPause(uptimeMillis);

		/* check for unpause */
		if(controller.isActivated(CONTROL_PAUSE))
			currentState = GameState.PLAYING;

		/* reset all persistant controls */
		controller.resetAllPersistant();

	}

	/* renderPause:
	 *
	 * 	Renders the pause screen.
	 */
	public void renderPause(long uptimeMillis)
	{
		Graphics g = display.draw();

		/* draw over middle third of screen */
		g.setColor(Color.black);
		g.fillRect(width/3, height/3, width/3, height/3);
		
		/* border color changes over time -- because
		 * we can! */
		int re = (int) (128 + 127*Math.cos(uptimeMillis / 5000.0));
		int gr = (int) (128 + 127*Math.sin(uptimeMillis / 5000.0));
		int bl = (int) (128 + 127*Math.cos(uptimeMillis / 20000.0));
		Color bdr = new Color(re,gr,bl);
		g.setColor(bdr);
		g.drawRect(width/3, height/3, width/3, height/3);

		/* Label as paused */
		g.setFont(new Font("PauseFont", Font.PLAIN, 32));
		g.drawString("PAUSED", width/2 - 60, height/2);
		
		/* repaint */
		display.repaint();
	}

	/* menuFrame:
	 *
	 * 	What to do in main menu
	 */
	public void menuFrame(long uptimeMillis)
	{
		/* render menu screen */
		renderMenu(uptimeMillis);
		
		/* check if user wants to start playing */
		if(controller.isActivated(CONTROL_START))
		{
			level = 1;
			lives = 3;
			initGameElements();
			currentState = GameState.PLAYING;
		}

		/* reset all persistant controls */
		controller.resetAllPersistant();
	}

	/* renderMenu:
	 *
	 * 	Renders the menu to start a new game.
	 */
	public void renderMenu(long uptimeMillis)
	{
		Graphics g = display.draw();

		/* draw over middle third of screen */
		g.setColor(Color.black);
		g.fillRect(width/3, height/3, width/3, height/3);
		
		/* border color changes over time -- because
		 * we can! */
		int re = (int) (128 + 127*Math.cos(uptimeMillis / 5000.0));
		int gr = (int) (128 + 127*Math.sin(uptimeMillis / 5000.0));
		int bl = (int) (128 + 127*Math.cos(uptimeMillis / 20000.0));
		Color bdr = new Color(re,gr,bl);
		g.setColor(bdr);
		g.drawRect(width/3, height/3, width/3, height/3);

		/* Label */
		g.setFont(new Font("MenuFont", Font.PLAIN, 32));
		g.drawString("New Game?", width/2 - 90, height/2);
		g.setFont(new Font("small", Font.PLAIN, 14));
		g.drawString("(press space)", width/2 - 40, 5*height/9);

		/* repaint */
		display.repaint();
	}

	public void justPaused() {}

	public void justUnpaused() {}

	/* GameState:
	 *
	 * This enum represents the current state of the game,
	 * whether a game is currently being played, whether a 
	 * pause is in effect, or if the main menu is showing. */
	public enum GameState
	{
		MENU, PLAYING, PAUSED
	}
}
