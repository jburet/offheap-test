package jbu.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;

public class KryoFactory {

    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>();

    public static Kryo getInstance() {
        Kryo res = null;
        if ((res = kryos.get()) == null) {
            res = new Kryo();
            res.setAutoReset(true);
            kryos.set(res);
        }
        return res;
    }

}
