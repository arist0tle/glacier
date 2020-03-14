package alpha.common.base.collection;

/**
 * @author tanghaiyang on 2019/9/28.
 */
public interface MyStack<Item> extends Iterable<Item> {

    MyStack<Item> push(Item item);

    Item pop() throws Exception;

    boolean isEmpty();

    int size();

}
