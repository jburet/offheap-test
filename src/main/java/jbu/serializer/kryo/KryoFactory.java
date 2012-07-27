package jbu.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;

final class KryoFactory {

    private static final ThreadLocal<Kryo> KRYOS = new ThreadLocal<Kryo>();

    private KryoFactory() {
    }

    static Kryo getInstance() {
        Kryo res = KRYOS.get();
        if (res == null) {
            res = new Kryo();
            res.setAutoReset(true);
            KRYOS.set(res);
        }
        return res;
    }

}
