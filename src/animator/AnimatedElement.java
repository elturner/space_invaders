package animator;

/* AnimatedElement.java
 *
 * This interface represents anything
 * that gets signaled by an animator. */

public interface AnimatedElement
{
	public void doFrame(long uptimeMillis);
	public void justPaused();
	public void justUnpaused();
}
