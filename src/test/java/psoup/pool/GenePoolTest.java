/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.pool;

import craterdog.primitives.Probability;
import psoup.*;
import psoup.genes.*;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


public class GenePoolTest {

    static XLogger logger = XLoggerFactory.getXLogger(GenePoolTest.class);


    @Test
    public void testAccessors() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        pool.putCreature(new Branch());
        pool.putCreature(new Chop());
        pool.putCreature(new Copy());
        pool.putCreature(new Get());
        pool.putCreature(new Merge());
        pool.putCreature(new Mutate());
        pool.putCreature(new Put());
        pool.putCreature(new Sequence());

        Gene creature = pool.getCreature(GenePool.BRANCH_ID);
        if (creature == null) {
            fail("No Branch gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.CHOP_ID);
        if (creature == null) {
            fail("No Chop gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.COPY_ID);
        if (creature == null) {
            fail("No Copy gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.GET_ID);
        if (creature == null) {
            fail("No Get gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.MERGE_ID);
        if (creature == null) {
            fail("No Merge gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.MUTATE_ID);
        if (creature == null) {
            fail("No Mutate gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.PUT_ID);
        if (creature == null) {
            fail("No Put gene returned from GenePool.");
        }
        creature = pool.getCreature(GenePool.SEQUENCE_ID);
        if (creature == null) {
            fail("No Sequence gene returned from GenePool.");
        }

    }


    @Test
    public void testInitialize() {
        Pool pool = new GenePool();
        pool.initialize(100, new Probability(0.75), 10, new Probability(0.5));
        int numberOfCreatures = pool.getCurrentNumberOfCreatures();
        if (numberOfCreatures != 100) {
            fail("Number of creatures in GenePool (" + numberOfCreatures + ") not equal to initialized number (100).");
        }
    }


    public void testLoadandStore() {
        GenePool poolA = new GenePool();
        GenePool poolB;
        poolA.initialize(20, new Probability(0.75), 3, new Probability(0.5));
        GenePool.storeGenePool(poolA, "target/gene-pool.json");
        logger.info("First Pool: {}", poolA);
        poolB = GenePool.loadGenePool("target/gene-pool.json");
        logger.info("Second Pool: {}", poolB);
        if (!poolA.equals(poolB)) {
            fail("The GenePool store() and load() methods don't result in equal gene pools.");
        }
    }


}
