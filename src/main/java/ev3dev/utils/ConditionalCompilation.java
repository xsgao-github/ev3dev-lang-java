package ev3dev.utils;

/**
 * http://hannesdorfmann.com/annotation-processing/annotationprocessing101
 * 
 * @author Song
 *
 */
public interface ConditionalCompilation {

	public final static boolean DC_ERROR = true;
	public final static boolean DC_INFO = DC_ERROR && true;
	public final static boolean DC_DEBUG = DC_INFO && false;
	public final static boolean DC_TRACE = DC_DEBUG && true;
}
