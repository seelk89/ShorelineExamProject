/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.dal.database.connection;

import java.util.Enumeration;
import java.util.Hashtable;
import mechachatapp.dal.exceptions.DalException;

/**
 *
 * @author pgn
 */
abstract class ObjectPool<T>
{

    private long expirationTime;

    private Hashtable<T, Long> locked, unlocked;

    protected ObjectPool()
    {
        expirationTime = 30000; // 30 seconds
        locked = new Hashtable<T, Long>();
        unlocked = new Hashtable<T, Long>();
    }

    protected abstract T create() throws DalException;

    public abstract boolean validate(T o) throws DalException;

    public abstract void expire(T o) throws DalException;

    public synchronized T checkOut() throws DalException
    {
        long now = System.currentTimeMillis();
        T t;
        if (unlocked.size() > 0)
        {
            Enumeration<T> e = unlocked.keys();
            while (e.hasMoreElements())
            {
                t = e.nextElement();
                if ((now - unlocked.get(t)) > expirationTime)
                {
                    // object has expired
                    unlocked.remove(t);
                    expire(t);
                    t = null;
                } else if (validate(t))
                {
                    unlocked.remove(t);
                    locked.put(t, now);
                    return (t);
                } else
                {
                    // object failed validation
                    unlocked.remove(t);
                    expire(t);
                    t = null;
                }
            }
        }
        // no objects available, create a new one
        t = create();
        locked.put(t, now);
        return (t);
    }

    public synchronized void checkIn(T t)
    {
        locked.remove(t);
        unlocked.put(t, System.currentTimeMillis());
    }

}
