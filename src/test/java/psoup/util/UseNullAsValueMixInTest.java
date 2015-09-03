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

import craterdog.primitives.Probability;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.genes.Chop;


public class UseNullAsValueMixInTest {

    static XLogger logger = XLoggerFactory.getXLogger(UseNullAsValueMixInTest.class);


    @Test
    public void testComparison() {
        Chop chop1 = new Chop();
        chop1.probability = new Probability(0.3d);
        int id1 = chop1.getSpeciesId();

        Chop chop2 = new Chop();
        chop2.probability = new Probability(0.7d);
        int id2 = chop2.getSpeciesId();

        assertEquals(id1, id2);
    }

}
