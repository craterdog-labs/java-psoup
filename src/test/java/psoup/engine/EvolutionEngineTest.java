/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.engine;

import craterdog.primitives.Probability;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.pool.*;


public class EvolutionEngineTest {

    static XLogger logger = XLoggerFactory.getXLogger(EvolutionEngineTest.class);

    @Test
    public void testSimpleEvolution() {
        int numberOfCreatures = 1000;
        Probability relativeComplexity = new Probability(0.75);
        Pool pool = new GenePool();
        pool.initialize(numberOfCreatures, relativeComplexity, 10, new Probability(0.5));
        Evolver evolver = new EvolutionEngine(new GenePool());
        if (evolver.isEvolving()) {
            fail("EvolutionEngine should not be evolving before startEvolving() call.");
        }
        evolver.startEvolving(10);
        if (!evolver.isEvolving()) {
            fail("EvolutionEngine should be evolving after startEvolving() call.");
        }
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
        }
        evolver.stopEvolving();
        if (evolver.isEvolving()) {
            fail("EvolutionEngine should not be evolving after stopEvolving() call.");
        }
    }


}
