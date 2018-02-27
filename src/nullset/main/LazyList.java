package nullset.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Version of a list designed to avoid concurrent modifications.
 * Adds and removes are delayed until update() is called.
 * @param <E>
 */
public class LazyList<E> extends ArrayList<E> {

    private List<E> addList;
    private List removeList;

    public LazyList() {
        super();
        addList = new ArrayList<>();
        removeList = new ArrayList();
    }

    public void update() {
        for (Object o : removeList)
            super.remove(o);
        removeList.clear();

        for (E e : addList)
            super.add(e);
        addList.clear();
    }

    @Override
    public boolean add(E e) {
        addList.add(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        removeList.add(o);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return !removeList.contains(o) && (super.contains(o) || addList.contains(o));
    }

}
