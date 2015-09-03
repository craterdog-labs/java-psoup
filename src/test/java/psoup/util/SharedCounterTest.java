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

import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


public class SharedCounterTest {

    static XLogger logger = XLoggerFactory.getXLogger(SharedCounterTest.class);


    @Test
    public void testResetCounter() {
        SharedCounter counter = new SharedCounter();
        counter.increment();
        counter.resetCounter();
        if (counter.getCurrentValue() != 0) {
            fail("The SharedCounter reset() method failed to reset to zero.");
        }
        if (counter.getLowWaterMark() != 0) {
            fail("The SharedCounter reset() method failed to reset the low water mark.");
        }
        if (counter.getHighWaterMark() != 0) {
            fail("The SharedCounter reset() method failed to reset the high water mark.");
        }
    }

    @Test
    public void testResetWaterMarks() {
        SharedCounter counter = new SharedCounter();
        counter.increment();
        counter.increment();
        counter.decrement();
        counter.resetWaterMarks();
        if (counter.getCurrentValue() != 1) {
            fail("The SharedCounter resetWaterMarks() method reset the current value.");
        }
        if (counter.getLowWaterMark() != 1) {
            fail("The SharedCounter resetWaterMarks() method failed to reset the low water mark.");
        }
        if (counter.getHighWaterMark() != 1) {
            fail("The SharedCounter resetWaterMarks() method failed to reset the high water mark.");
        }
    }

    @Test
    public void testIncrement() {
        SharedCounter counter = new SharedCounter();
        int firstValue = counter.getCurrentValue();
        counter.increment();
        int secondValue = counter.getCurrentValue();
        if (firstValue + 1 != secondValue) {
            fail("The SharedCounter increment() method failed to increment the value.");
        }
    }

    @Test
    public void testDecrement() {
        SharedCounter counter = new SharedCounter();
        int firstValue = counter.getCurrentValue();
        counter.decrement();
        int secondValue = counter.getCurrentValue();
        if (firstValue - 1 != secondValue) {
            fail("The SharedCounter increment() method failed to decrement the value.");
        }
    }

}
