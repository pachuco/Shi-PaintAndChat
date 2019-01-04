package syi.util;

public class ThreadPool extends Thread {
    private static Object lock = new Object();
    private static int threadLimit = 0;
    private static int priority = 1;
    private static ThreadPool[] pool = null;
    private int poolNumber;
    private boolean isLive = true;
    private boolean isEmpty = false;
    private Runnable runnable = null;

    private ThreadPool(Runnable var1, String var2, int var3) {
        super(var2);
        this.poolNumber = var3;
        this.runnable = var1;
        this.setPriority(priority);
        this.setDaemon(true);
        this.start();
    }

    protected void finalize() throws Throwable {
        this.kill();
    }

    public static int getCountOfSleeping() {
        int var0 = 0;
        Object var1 = lock;
        synchronized (lock) {
            if (pool == null) {
                return 0;
            } else {
                for (int var3 = 0; var3 < pool.length; ++var3) {
                    ThreadPool var2 = pool[var3];
                    var0 += var2 != null && var2.isEmpty ? 1 : 0;
                }

                return var0;
            }
        }
    }

    public static int getCountOfWorking() {
        int var0 = 0;
        Object var1 = lock;
        synchronized (lock) {
            if (pool == null) {
                return 0;
            } else {
                for (int var3 = 0; var3 < pool.length; ++var3) {
                    ThreadPool var2 = pool[var3];
                    var0 += var2 != null && !var2.isEmpty ? 1 : 0;
                }

                return var0;
            }
        }
    }

    public void interrupt() {
        try {
            synchronized (this) {
                if (!this.isEmpty) {
                    super.interrupt();
                }
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    private void kill() {
        try {
            if (pool == null) {
                return;
            }

            Object var1 = lock;
            synchronized (lock) {
                if (this.isLive) {
                    pool[this.poolNumber] = null;
                    this.runnable = null;
                }

                this.isLive = false;
                if (this.isEmpty) {
                    this.notify();
                } else {
                    this.interrupt();
                }
            }
        } catch (RuntimeException var3) {
            ;
        } catch (ThreadDeath var4) {
            ;
        }

    }

    public static void poolGcAll() {
        if (pool != null) {
            Object var1 = lock;
            synchronized (lock) {
                for (int var2 = 0; var2 < pool.length; ++var2) {
                    ThreadPool var0 = pool[var2];
                    if (var0 != null && var0.isEmpty) {
                        var0.kill();
                    }
                }

            }
        }
    }

    public static void poolKillAll() {
        if (pool != null) {
            Object var0 = lock;
            synchronized (lock) {
                for (int var1 = 0; var1 < pool.length; ++var1) {
                    pool[var1].kill();
                }

            }
        }
    }

    public static void poolSetLimit(int var0) {
        Object var1 = lock;
        synchronized (lock) {
            poolKillAll();
            threadLimit = var0;
            pool = new ThreadPool[var0 <= 0 ? 25 : threadLimit];
        }
    }

    public static void poolStartThread(Runnable var0, char var1) {
        poolStartThread(var0, String.valueOf(var1));
    }

    public static ThreadPool poolStartThread(Runnable var0, String var1) {
        if (var1 == null || var1.length() <= 0) {
            var1 = "pool";
        }

        try {
            Object var3 = lock;
            synchronized (lock) {
                if (pool == null) {
                    poolSetLimit(threadLimit);
                }

                int var4 = pool.length;

                int var5;
                for (var5 = 0; var5 < var4; ++var5) {
                    ThreadPool var2 = pool[var5];
                    if (var2 != null && var2.isEmpty) {
                        if (var2.restart(var0, var1)) {
                            return var2;
                        }

                        var2.kill();
                    }
                }

                for (var5 = 0; var5 < var4; ++var5) {
                    if (pool[var5] == null) {
                        pool[var5] = new ThreadPool(var0, var1, var5);
                        return pool[var5];
                    }
                }

                if (threadLimit <= 0) {
                    ThreadPool[] var9 = new ThreadPool[var4 + Math.min(Math.max((int) ((double) var4 * 0.4D), 1), 100)];

                    for (int var6 = 0; var6 < var4; ++var6) {
                        var9[var6] = pool[var6];
                    }

                    pool = var9;
                    pool[var4] = new ThreadPool(var0, var1, var4);
                    return pool[var4];
                }
            }
        } catch (RuntimeException var8) {
            var8.printStackTrace();
        }

        return null;
    }

    private boolean restart(Runnable var1, String var2) {
        try {
            if (this.isLive && this.isEmpty) {
                synchronized (this) {
                    if (this.isEmpty && this.isLive) {
                        this.runnable = var1;
                        this.isEmpty = false;
                        this.setName(var2);
                        this.notify();
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (RuntimeException var5) {
            return false;
        }
    }

    public void run() {
        while (true) {
            if (this.isLive) {
                this.run2();

                try {
                    if (this.isLive) {
                        synchronized (this) {
                            this.isEmpty = true;
                            this.wait(30000L);
                            if (this.isEmpty) {
                                this.kill();
                                return;
                            }
                            continue;
                        }
                    }
                } catch (Throwable var3) {
                    this.kill();
                    return;
                }
            }

            return;
        }
    }

    private void run2() {
        try {
            if (this.runnable != null) {
                this.runnable.run();
            }
        } catch (Throwable var1) {
            ;
        }

        this.runnable = null;
    }
}
