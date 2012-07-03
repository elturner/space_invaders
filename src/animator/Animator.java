package animator;

import javax.swing.Timer;
import java.util.ArrayList;
import java.awt.event.*;

/* Animator.java:
 *
 * 	This class controls the timer
 * 	that is used to iterate over a new frame of the
 * 	game every timestep.
 */

public class Animator
{
	private Timer t; /* The timer used to signal new frame */
	private int timestep; /* # milliseconds between game frames */
	private long uptimeMillis; /* number of milliseconds of playtime */
	private long lastFrameTimestamp; /* when last frame occurred */

	/* This list of elements will be signaled each frame */
	private ArrayList<AnimatedElement> elements;

	/* constructor */
	public Animator(int ts)
	{
		timestep = ts;
		t = null;
		elements = new ArrayList<AnimatedElement>();
	}

	/******* Accessors ****/

	public long uptimeMillis()
	{
		return uptimeMillis;
	}

	public void addElement(AnimatedElement ae)
	{
		elements.add(ae);
	}

	/******* Animation Methods ****/

	/* begin:
	 *
	 * 	Will initialize the animation and start
	 * 	performing animation tasks every timestep milliseconds.
	 */
	public void begin()
	{
		/* construction of what to do each frame */
		ActionListener taskPerformer = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				/* Run the current frame on each element */
				for(int i = 0; i < elements.size(); i++)
					elements.get(i).doFrame(
							uptimeMillis);
				
				/* update the record of uptime */
				long curr = System.currentTimeMillis();
				uptimeMillis += (curr - lastFrameTimestamp);
				lastFrameTimestamp = curr;
			}
		};

		/* begin timer */
		t = new Timer(timestep, taskPerformer);
		t.start();
		uptimeMillis = 0;
		lastFrameTimestamp = System.currentTimeMillis();
	}

	/* pause:
	 *
	 * 	If timer is running, will stop the animation.
	 * 	If timer is not running, will begin animation.
	 */
	public void pause()
	{
		/* check status of timer */
		if(t.isRunning())
		{
			t.stop();
			for(int i = 0; i < elements.size(); i++)
				elements.get(i).justPaused();
		}
		else
		{
			for(int i = 0; i < elements.size(); i++)
				elements.get(i).justUnpaused();

			lastFrameTimestamp = System.currentTimeMillis();
			t.start();
		}
	}
}
