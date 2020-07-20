package ev3dev.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * The class responsible to interact with Sysfs on EV3Dev
 *
 * @author Juan Antonio Breña Moral
 */
public class Sysfs implements ConditionalCompilation {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Sysfs.class);
    
    /**
     * Cached RandomAccessFile's that write to sysfs.
     */
    private static final Map<String, RandomAccessFile> writers = new HashMap<>();
    
    /**
     * Cached RandomAccessFile's that read from sysfs.
     */
    private static final Map<String, RandomAccessFile> readers = new HashMap<>();
    
    /**
     * Write a value in a file.
     *
     * @param filePath File path
     * @param value    value to write
     * @return A boolean value if the operation was written or not.
     */
    public static boolean writeString(final String filePath, final String value) {
        if (DC_TRACE && log.isTraceEnabled()) {
            log.trace("echo " + value + " > " + filePath);
        }
        try {
        	RandomAccessFile raf = writers.get(filePath);
        	if (raf == null) {
        		raf = new RandomAccessFile(filePath, "rws");
        		writers.put(filePath, raf);
        	}
        	raf.seek(0);
			raf.write(value.getBytes());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean writeInteger(final String filePath, final int value) {
        return writeString(filePath, String.valueOf(value));
    }

    /**
     * Read an Attribute in the Sysfs with containing String values
     *
     * @param filePath path
     * @return value from attribute
     */
    public static String readString(final String filePath) {
    	if (DC_TRACE && log.isTraceEnabled()) {
            log.trace("cat " + filePath);
        }
        try {
        	RandomAccessFile raf = readers.get(filePath);
        	if (raf == null) {
        		raf = new RandomAccessFile(filePath, "r");
        		writers.put(filePath, raf);
        	}
        	raf.seek(0);
        	byte[] b = new byte[64];
        	return new String(b, 0, raf.read(b));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException("Problem reading path: " + filePath, e);
        }
    }

    /**
     * Read an Attribute in the Sysfs with containing Integer values
     *
     * @param filePath path
     * @return value from attribute
     */
    public static int readInteger(final String filePath) {
        return Integer.parseInt(readString(filePath));
    }

    public static float readFloat(final String filePath) {
        return Float.parseFloat(readString(filePath));
    }

    /**
     * @param filePath path
     * @return an List with options from a path
     */
    public static List<File> getElements(final String filePath) {
        final File f = new File(filePath);
        if (existPath(filePath) && (f.listFiles().length > 0)) {
            return new ArrayList<>(Arrays.asList(f.listFiles()));
        } else {
            throw new RuntimeException("The path doesn't exist: " + filePath);
        }
    }

    /**
     * This method is used to detect folders in /sys/class/
     *
     * @param filePath path
     * @return boolean
     */
    public static boolean existPath(final String filePath) {
    	if (DC_TRACE && log.isTraceEnabled()) {
            log.trace("ls " + filePath);
        }
        final File f = new File(filePath);
        return f.exists() && f.isDirectory();
    }

    public static boolean existFile(Path pathToFind) {
        return Files.exists(pathToFind);
    }

    /**
     * Method to write bytes in a path
     *
     * @param path path
     * @param value value to write
     * @return Result
     */
    public static boolean writeBytes(final String path, final byte[] value) {
        try {
            Files.write(Paths.get(path), value, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Unable to draw the LCD", e);
        }
        return true;
    }

}
