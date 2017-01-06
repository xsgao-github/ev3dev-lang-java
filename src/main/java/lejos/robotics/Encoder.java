package lejos.robotics;

/**
 * Abstraction for the tachometer built into NXT motors.
 * 
 * @author Lawrie Griffiths
 *
 */
public interface Encoder {
	
	  /**
	   * Returns the tachometer count.
	   * 
	   * @return tachometer count in degrees
	   */
	  int getTachoCount();

	  
	  /**
	   * Reset the tachometer count.
	   */
	  void resetTachoCount();

}
