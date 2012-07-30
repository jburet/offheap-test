package jbu;

/**
 * Helper for finding memory size of each primitive TYPE
 */
public interface Primitive {
    /**
     * Size of a {@code boolean} in memory
     */
    static final int BOOLEAN_LENGTH = 1;
    /**
     * Size of a {@code char} in memory
     */
    static final int CHAR_LENGTH = 2;
    /**
     * Size of a {@code byte} in memory
     */
    static final int BYTE_LENGTH = 1;
    /**
     * Size of a {@code short} in memory
     */
    static final int SHORT_LENGTH = 2;
    /**
     * Size of a {@code int} in memory
     */
    static final int INT_LENGTH = 4;
    /**
     * Size of a {@code long} in memory
     */
    static final int LONG_LENGTH = 8;
    /**
     * Size of a {@code float} in memory
     */
    static final int FLOAT_LENGTH = 4;
    /**
     * Size of a {@code double} in memory
     */
    static final int DOUBLE_LENGTH = 8;
}
