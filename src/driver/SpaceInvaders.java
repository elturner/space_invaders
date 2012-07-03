package driver;

/* SpaceInvaders.java:
 *
 * The main class of this game.
 */

import animator.Animator;
import game.Game;
import gui.Controller;
import gui.Display;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class SpaceInvaders
{
	/* components of the game */
	private Controller controller;
	private Animator animator;
	private Game game;

	/*** constructors ***/

	public static void main(String[] args)
	{
		new SpaceInvaders();
	}

	public SpaceInvaders()
	{
		/* init controls */
		initControls();

		/* create display */
		Display display = new Display(Game.width, Game.height);
		display.setFocusable(true);
		display.addKeyListener(controller);

		JFrame frame = new JFrame("Space Invaders");
		frame.setSize(display.width(), display.height());
		frame.setLocation(30,10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(display);
		frame.setVisible(true);
		
		/* init game */
		animator = new Animator(20);
		game = new Game(display, controller);
		animator.addElement(game);
	
		/* START! */
		animator.begin();
	}
	
	/* initControls:
	 *
	 *	Initializes the control scheme for the game.
	 */
	public void initControls()
	{
		controller = new Controller(); 
		controller.bindPersistant(KeyEvent.VK_P, 
						game.CONTROL_PAUSE);
		controller.bindToggled(KeyEvent.VK_LEFT,
						game.CONTROL_LEFT);
		controller.bindToggled(KeyEvent.VK_RIGHT,
						game.CONTROL_RIGHT);
		controller.bindToggled(KeyEvent.VK_SPACE,
						game.CONTROL_FIRE);
		controller.bindPersistant(KeyEvent.VK_B, 
						game.CONTROL_BOMB);
		controller.bindPersistant(KeyEvent.VK_SPACE,
						game.CONTROL_START);
	}
}
