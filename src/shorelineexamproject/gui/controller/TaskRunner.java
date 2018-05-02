/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesper
 */
public class TaskRunner implements Runnable
{

    private final Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Runnable task;

    TaskRunner(Runnable runnable)
    {
        task = runnable;
        thread = new Thread(task);
    }

    public void start()
    {
        running.set(true);

        thread.start();
    }

    public void pause()
    {
        synchronized (thread)
        {
            try
            {
                running.set(false);
                thread.wait();
            } catch (InterruptedException ex)
            {
                Logger.getLogger(TaskRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resume()
    {
        synchronized (thread)
        {
            thread.notify();
        }
    }

    public void interrupt()
    {
        synchronized (thread)
        {
            running.set(false);
            thread.interrupt();
        }
    }

    boolean isRunning()
    {
        return running.get();
    }

    @Override
    public void run()
    {
        while(running.get() == true)
        {
            task.run();
        }
    }
}
