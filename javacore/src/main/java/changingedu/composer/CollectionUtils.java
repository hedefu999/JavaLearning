package changingedu.composer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    /**
     * List提取字段
     */
    public static <F,O> List<F> extractFields(Collection<O> collection, Extractor<F,O> extractor){
        List<F> fields = new LinkedList<>();
        Iterator<O> iterator = collection.iterator();
        while (iterator.hasNext()){
            O object = iterator.next();
            fields.add(extractor.getTargetField(object));
        }
        return fields;
    }

    /**
     * List转Map
     */
    public static <E, V> Map<E, V> mapComposerId(Collection<V> collection, Extractor<E, V> extractor) {
        Map<E, V> map = new HashMap<E, V>(collection.size());
        for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
            V v = iterator.next();
            map.put(extractor.getTargetField(v), v);
        }
        return map;
    }
}
