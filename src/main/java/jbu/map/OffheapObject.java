package jbu.map;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 */
public class OffheapObject {

    public final Class<? extends Serializable> originalClass;
    public final long addr;
    public final int serializedSize;

    public OffheapObject(Class<? extends Serializable> originalClass, long addr, int serializedSize) {
        this.originalClass = originalClass;
        this.addr = addr;
        this.serializedSize = serializedSize;
    }
}
