package com.treelogic.proteus;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class SuspendableThread {

    protected boolean suspended;

    public synchronized void suspend() {
        suspended = true;
    }

    public synchronized void suspendForAWhile(int ms) {
        try {
            this.suspend();
            Thread.sleep(ms);
            this.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
/**
        new Thread(() -> {
            try {
                this.suspend();
                Thread.sleep(ms);
                this.resume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
 **/

    }

    public synchronized void resume() {
        suspended = false;
        notifyAll();
    }

    public synchronized void waitWhileSuspended(String row) {
        while (suspended) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
