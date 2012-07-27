package jbu;

/**
 * Helper for finding memory size of each primitive TYPE
 */
public interface Primitive {
    /**
     * Size of a {@code boolean} in memory
     */
    public static final int BOOLEAN_LENGTH = 1;
    /**
     * Size of a {@code char} in memory
     */
    public static final int CHAR_LENGTH = 2;
    /**
     * Size of a {@code byte} in memory
     */
    public static final int BYTE_LENGTH = 1;
    /**
     * Size of a {@code short} in memory
     */
    public static final int SHORT_LENGTH = 2;
    /**
     * Size of a {@code int} in memory
     */
    public static final int INT_LENGTH = 4;
    /**
     * Size of a {@code long} in memory
     */
    public static final int LONG_LENGTH = 8;
    /**
     * Size of a {@code float} in memory
     */
    public static final int FLOAT_LENGTH = 4;
    /**
     * Size of a {@code double} in memory
     */
    public static final int DOUBLE_LENGTH = 8;
}
