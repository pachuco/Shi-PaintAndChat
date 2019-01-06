package syi.util;

public class Vector2 {
    private Object[] elements;
    private int seek;
    private float sizeAdd;
    private int sizeDefault;

    public Vector2() {
        this(10);
    }

    public Vector2(int var1) {
        this.elements = null;
        this.seek = 0;
        this.sizeAdd = 1.5F;
        this.sizeDefault = 25;
        var1 = var1 <= 0 ? 50 : var1;
        this.sizeDefault = var1;
        this.elements = new Object[var1];
    }

    public final synchronized void add(Object[] var1) {
        if (var1.length + this.seek >= this.elements.length) {
            this.moreData(var1.length + this.seek);
        }

        System.arraycopy(var1, 0, this.elements, this.seek, var1.length);
        this.seek += var1.length;
    }

    public final synchronized void add(Object var1) {
        if (this.seek >= this.elements.length) {
            this.moreData(this.seek + 1);
        }

        this.elements[this.seek++] = var1;
    }

    public final synchronized void copy(Object[] var1, int var2, int var3, int var4) {
        var4 = var2 + var4 > this.seek ? this.seek - var2 : var4;
        if (var4 > 0) {
            System.arraycopy(this.elements, var2, var1, var3, var4);
        }
    }

    public final synchronized void copy(Vector2 var1) {
        int var2 = this.seek;

        for (int var3 = 0; var3 < var2; ++var3) {
            var1.add(this.elements[var3]);
        }

    }

    public final synchronized void gc() {
        int var1 = Math.max(this.seek, this.sizeDefault) + 3;
        if (this.elements.length > var1) {
            Object[] var2 = new Object[var1];
            System.arraycopy(this.elements, 0, var2, 0, this.seek);
            this.elements = var2;
        }

    }

    public final synchronized Object get(int var1) {
        return var1 >= this.seek ? null : this.elements[var1];
    }

    private final void moreData(int var1) {
        Object[] var2 = new Object[var1 + Math.max((int) ((float) var1 * this.sizeAdd), 1)];
        System.arraycopy(this.elements, 0, var2, 0, this.elements.length);
        this.elements = var2;
    }

    public final synchronized void remove(int var1) {
        var1 = Math.min(var1, this.seek);
        if (var1 > 0) {
            if (this.seek != var1) {
                System.arraycopy(this.elements, var1, this.elements, 0, this.seek - var1);
            }

            for (int var2 = this.seek - var1; var2 < this.seek; ++var2) {
                this.elements[var2] = null;
            }

            this.seek -= var1;
        }
    }

    public final synchronized void removeAll() {
        this.remove(this.seek);
    }

    public final int size() {
        return this.seek;
    }
}
