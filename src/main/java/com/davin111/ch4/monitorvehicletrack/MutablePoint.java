package com.davin111.ch4.monitorvehicletrack;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class MutablePoint {
    public int x, y;

    public MutablePoint() { x = 0; y = 0; }

    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
