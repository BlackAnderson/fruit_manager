package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FixTimeDelayQueue<E extends Delayed> extends AbstractQueue<E> implements BlockingQueue<E>
{
    private final transient ReentrantLock lock;
    private final transient Condition available;
    private final PriorityQueue<E> q;
    private final long delayTime;
    
    public FixTimeDelayQueue(final long delayTime) {
        this.lock = new ReentrantLock();
        this.available = this.lock.newCondition();
        this.q = new PriorityQueue<E>();
        this.delayTime = delayTime;
    }
    
    public FixTimeDelayQueue(final long delayTime, final Collection<? extends E> c) {
        this.lock = new ReentrantLock();
        this.available = this.lock.newCondition();
        this.q = new PriorityQueue<E>();
        this.delayTime = delayTime;
        this.addAll(c);
    }
    
    @Override
    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (true) {
                final E first = this.q.peek();
                if (first == null) {
                    this.available.await();
                }
                else {
                    final long delay = first.getDelay(TimeUnit.MILLISECONDS);
                    if (delay <= 0L) {
                        break;
                    }
                    final boolean flag = this.available.await((delay < this.delayTime) ? delay : this.delayTime, TimeUnit.MILLISECONDS);
                    if (!flag) {
                        continue;
                    }
                    continue;
                }
            }
            final E x = this.q.poll();
            assert x != null;
            if (this.q.size() != 0) {
                this.available.signalAll();
            }
            return x;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (true) {
                final E first = this.q.peek();
                final long millis = unit.toMillis(timeout);
                if (first == null) {
                    if (millis <= 0L) {
                        return null;
                    }
                    final boolean flag = this.available.await((this.delayTime > millis) ? millis : this.delayTime, TimeUnit.MILLISECONDS);
                    if (!flag) {
                        continue;
                    }
                    continue;
                }
                else {
                    long delay = first.getDelay(TimeUnit.MILLISECONDS);
                    if (delay > 0L) {
                        if (millis <= 0L) {
                            return null;
                        }
                        if (delay > millis) {
                            delay = millis;
                        }
                        if (delay > this.delayTime) {
                            delay = this.delayTime;
                        }
                        final boolean flag2 = this.available.await(delay, TimeUnit.MILLISECONDS);
                        if (!flag2) {
                            continue;
                        }
                        continue;
                    }
                    else {
                        final E x = this.q.poll();
                        assert x != null;
                        if (this.q.size() != 0) {
                            this.available.signalAll();
                        }
                        return x;
                    }
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean add(final E e) {
        return this.offer(e);
    }
    
    @Override
    public boolean offer(final E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final E first = this.q.peek();
            this.q.offer(e);
            if (first == null || e.compareTo(first) < 0) {
                this.available.signalAll();
            }
            return true;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public void put(final E e) {
        this.offer(e);
    }
    
    @Override
    public boolean offer(final E e, final long timeout, final TimeUnit unit) {
        return this.offer(e);
    }
    
    @Override
    public E poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final E first = this.q.peek();
            if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0L) {
                return null;
            }
            final E x = this.q.poll();
            assert x != null;
            if (this.q.size() != 0) {
                this.available.signalAll();
            }
            return x;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.q.peek();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.q.size();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public int drainTo(final Collection<? super E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            while (true) {
                final E first = this.q.peek();
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0L) {
                    break;
                }
                c.add((E)this.q.poll());
                ++n;
            }
            if (n > 0) {
                this.available.signalAll();
            }
            return n;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n;
            for (n = 0; n < maxElements; ++n) {
                final E first = this.q.peek();
                if (first == null) {
                    break;
                }
                if (first.getDelay(TimeUnit.NANOSECONDS) > 0L) {
                    break;
                }
                c.add((E)this.q.poll());
            }
            if (n > 0) {
                this.available.signalAll();
            }
            return n;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            this.q.clear();
        }
        finally {
            lock.unlock();
        }
        lock.unlock();
    }
    
    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public Object[] toArray() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.q.toArray();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.q.toArray(a);
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean remove(final Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.q.remove(o);
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Itr(this.toArray());
    }
    private class Itr implements Iterator<E>
    {
        final Object[] array;
        int cursor;
        int lastRet;
        
        Itr(final Object[] array) {
            this.lastRet = -1;
            this.array = array;
        }
        
        @Override
        public boolean hasNext() {
            return this.cursor < this.array.length;
        }
        
        @Override
        public E next() {
            if (this.cursor >= this.array.length) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.cursor;
            return (E)this.array[this.cursor++];
        }
        
        @Override
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            final Object x = this.array[this.lastRet];
            this.lastRet = -1;
            FixTimeDelayQueue.this.lock.lock();
            try {
                final Iterator it = FixTimeDelayQueue.this.q.iterator();
                while (it.hasNext()) {
                    if (it.next() == x) {
                        it.remove();
                        return;
                    }
                }
            }
            finally {
                FixTimeDelayQueue.this.lock.unlock();
            }
            FixTimeDelayQueue.this.lock.unlock();
        }
    }
}