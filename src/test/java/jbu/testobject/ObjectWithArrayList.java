package jbu.testobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectWithArrayList {

    public List<String> collection= new ArrayList<>();


    @Override
    public String toString() {
        return "ObjectWithArrayList{" +
                "collection=" + collection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectWithArrayList that = (ObjectWithArrayList) o;

        if (collection != null ? !Arrays.equals(collection.toArray(), (that.collection.toArray())) : that.collection != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return collection != null ? collection.hashCode() : 0;
    }
}
