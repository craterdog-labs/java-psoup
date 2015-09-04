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

import psoup.GeneVisitor;
import craterdog.primitives.Probability;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.genes.*;
import psoup.util.*;


public class InitializerTest {

    static XLogger logger = XLoggerFactory.getXLogger(InitializerTest.class);


    @Test
    public void testInitializeBranch() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Branch branch = new Branch();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        branch.accept(initializer);
        logger.info("BRANCH: {}", branch);
    }


    @Test
    public void testInitializeChop() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Chop chop = new Chop();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        chop.accept(initializer);
        logger.info("CHOP: {}", chop);
    }


    @Test
    public void testInitializeCopy() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Copy copy = new Copy();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        copy.accept(initializer);
        logger.info("COPY: {}", copy);
    }


    @Test
    public void testInitializeGet() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Get get = new Get();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        get.accept(initializer);
        logger.info("GET: {}", get);
    }


    @Test
    public void testInitializeMerge() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Merge merge = new Merge();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        merge.accept(initializer);
        logger.info("MERGE: {}", merge);
    }


    @Test
    public void testInitializeMutate() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Mutate mutate = new Mutate();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        mutate.accept(initializer);
        logger.info("MUTATE: {}", mutate);
    }


    @Test
    public void testInitializePut() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Put put = new Put();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        put.accept(initializer);
        logger.info("PUT: {}", put);
    }


    @Test
    public void testInitializeSequence() {
        Probability probability = new Probability(0.5);
        SharedCounter counter = new SharedCounter();
        Sequence sequence = new Sequence();
        GeneVisitor initializer = new Initializer(probability, 10, counter);
        sequence.accept(initializer);
        logger.info("SEQUENCE: {}", sequence);
    }


    @Test
    public void testInitializeGenerateGenes() {
        Probability probability = new Probability(0.85);
        SharedCounter counter = new SharedCounter();
        for (int i = 0; i < 10; i++) {
            Gene creature = Initializer.generateCreature(probability, 10, counter);
            logger.info("CREATURE: {}", creature);
        }
    }


}
