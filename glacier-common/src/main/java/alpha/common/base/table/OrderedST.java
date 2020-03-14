package alpha.common.base.table;

import java.util.List;

/**
 * @author tanghaiyang on 2019/9/28.
 */
public interface OrderedST<Key extends Comparable<Key>, Value> {

    int size();

    void put(Key key, Value value);

    Value get(Key key);

    Key min();

    Key max();

    int rank(Key key);

    List<Key> keys(Key l, Key h);
}