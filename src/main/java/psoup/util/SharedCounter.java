/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.util;

import craterdog.smart.SmartObject;


public final class SharedCounter extends SmartObject<SharedCounter> {

    public SharedCounter() {
    }


    public SharedCounter(int value) {
        currentValue = value;
        resetWaterMarks();
    }


    public synchronized void resetCounter() {
        currentValue = 0;
        resetWaterMarks();
    }


    public synchronized void resetWaterMarks() {
        lowWaterMark = currentValue;
        highWaterMark = currentValue;
    }


    public synchronized void increment() {
        currentValue++;
        if (highWaterMark < currentValue) {
            highWaterMark = currentValue;
        }
    }


    public synchronized void decrement() {
        currentValue--;
        if (lowWaterMark > currentValue) {
            lowWaterMark = currentValue;
        }
    }


    public synchronized int getCurrentValue() {
        return currentValue;
    }

    public synchronized void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public synchronized int getLowWaterMark() {
        return lowWaterMark;
    }

    public synchronized void setLowWaterMark(int lowWaterMark) {
        this.lowWaterMark = lowWaterMark;
    }

    public synchronized int getHighWaterMark() {
        return highWaterMark;
    }

    public synchronized void setHighWaterMark(int highWaterMark) {
        this.highWaterMark = highWaterMark;
    }

    private SharedCounter(int currentValue, int lowWaterMark, int highWaterMark) {
        this.currentValue = currentValue;
        this.lowWaterMark = lowWaterMark;
        this.highWaterMark = highWaterMark;
    }


    public int currentValue;
    public int lowWaterMark;
    public int highWaterMark;

}
