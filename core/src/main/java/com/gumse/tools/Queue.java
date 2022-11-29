package com.gumse.tools;

public class Queue<T> {
    private class QueueItem
    {
        public T value;
        public QueueItem nextItem;

        public QueueItem(T val)
        {
            value = val;
        }
    }

    private QueueItem firstItem;
    private QueueItem lastItem;
    private int len;

    public Queue()
    {
        firstItem = null;
        lastItem = null;
        len = 0;
    }

    public void enqueue(T item)
    {
        if (lastItem == null) 
        {
            this.firstItem = this.lastItem = new QueueItem(item);
        } 
        else 
        {
            QueueItem q = new QueueItem(item);
            lastItem.nextItem = q;
            lastItem = q; 
        }
        len++;
    }

    public T dequeue()
    {
        if (firstItem != null) 
        {
            QueueItem q = firstItem;
            firstItem = q.nextItem;
            q.nextItem = null;
            len--;
            return q.value;
        }
        return null;
    }

    public T getFirst()
    {
        return firstItem.value;
    }

    public boolean isEmpty()
    {
        return firstItem == null;
    }

    public int getLength()
    {
        return len;
    }
}
