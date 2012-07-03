package gui;

import java.util.*;
import java.awt.event.*;

/* Controller:
 *
 * 	This class is in charge of listening to user input
 * 	through the keyboard, and recording which keys were
 * 	pressed.
 */

public class Controller implements KeyListener
{	
	/* The following lists keep track of which
	 * controls are active */
	private Set<String> persistantControls; /* activated on release */
	private Set<String> toggledControls; /* only active when pushed */ 

	/* The following maps keys to controls */
	private Map<Integer, String> persistantControlsMap;
	private Map<Integer, String> toggledControlsMap;

	/*** Constructors ***/

	public Controller()
	{
		/* init fields */
		persistantControls = new HashSet<String>();
		toggledControls = new HashSet<String>();
		persistantControlsMap = new HashMap<Integer, String>();
		toggledControlsMap = new HashMap<Integer, String>();
	}

	/*** Modify control scheme ***/

	/* bindPersistant:
	 *
	 * 	Binds a key to be a control, which is
	 * 	activated when the user releases that button.
	 */
	public void bindPersistant(int key, String val)
	{
		persistantControlsMap.put(new Integer(key), val);
	}

	/* bindToggled:
	 *
	 * 	Binds a key to be a control, which is active
	 * 	only when the button is being pressed
	 */
	public void bindToggled(int key, String val)
	{
		toggledControlsMap.put(new Integer(key), val);
	}

	/*** accessors ***/

	/* isToggled:
	 *
	 * 	Checks if the given control value is toggled.
	 */
	public boolean isToggled(String val)
	{
		return toggledControls.contains(val);
	}

	/* isActivated:
	 *
	 * 	Checks if the given persisant control value is active.
	 */
	public boolean isActivated(String val)
	{
		return persistantControls.contains(val);
	}

	/* resetAllPersistant:
	 *
	 *	Sets all persistant controls to be inactive.
	 */
	public void resetAllPersistant()
	{
		persistantControls.clear();
	}

	/*** listener ***/
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e)
	{
		/* get keyboard button being pressed */
		Integer i = new Integer(e.getKeyCode());

		/* see if this keyboard button is significant */
		if(toggledControlsMap.containsKey(i))
			toggledControls.add(toggledControlsMap.get(i));
	}

	public void keyReleased(KeyEvent e)
	{
		/* get keyboard button being pressed */
		Integer i = new Integer(e.getKeyCode());

		/* release any toggles */
		if(toggledControlsMap.containsKey(i))
			toggledControls.remove(toggledControlsMap.get(i));

		/* activate any persistant controls */
		if(persistantControlsMap.containsKey(i))
			persistantControls.add(
				persistantControlsMap.get(i));
	}
}
