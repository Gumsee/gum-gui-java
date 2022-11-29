package com.gumse.tools;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {

    private List<T> items;

    public Stack()
    {
        items = new ArrayList<>();
    }

    public void push(T item)
    {
        items.add(item);
    }

    public T pop()
    {
        if(items.size() == 0)
            return null;
        
        T val = items.get(items.size() - 1);
        items.remove(items.size() - 1);

        return val;
    }

    public T getLast()
    {
        return items.get(items.size() - 1);
    }

    public boolean isEmpty()
    {
        return items.size() == 0;
    }

    public int getLength()
    {
        return items.size();
    }
}
