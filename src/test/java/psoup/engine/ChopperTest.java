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

import psoup.GeneVisitor;
import craterdog.primitives.Probability;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.genes.*;
import psoup.pool.*;


public class ChopperTest {

    static XLogger logger = XLoggerFactory.getXLogger(ChopperTest.class);


    @Test
    public void testChopEmptyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        branch.accept(chopper);
    }


    @Test
    public void testChopLeftOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        branch.leftBranch = left;
        GeneVisitor chopper = new Chopper(pool, new Probability());
        branch.accept(chopper);
    }


    @Test
    public void testChopRightOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch right = new Branch();
        branch.rightBranch = right;
        GeneVisitor chopper = new Chopper(pool, new Probability());
        branch.accept(chopper);
    }


    @Test
    public void testChopDualBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        Branch right = new Branch();
        branch.leftBranch = left;
        branch.rightBranch = right;
        GeneVisitor chopper = new Chopper(pool, new Probability());
        branch.accept(chopper);
    }


    @Test
    public void testChopChop() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Chop chop = new Chop();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        chop.accept(chopper);
    }


    @Test
    public void testChopCopy() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Copy copy = new Copy();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        copy.accept(chopper);
    }


    @Test
    public void testChopNullGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        get.accept(chopper);
    }


    @Test
    public void testChopGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        get.speciesId = pool.pickRandomSpecies();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        get.accept(chopper);
    }


    @Test
    public void testChopMerge() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Merge merge = new Merge();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        merge.accept(chopper);
    }


    @Test
    public void testChopMutate() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Mutate mutate = new Mutate();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        mutate.accept(chopper);
    }


    @Test
    public void testChopPut() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Put put = new Put();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        put.accept(chopper);
    }


    @Test
    public void testChopEmptySequence() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        GeneVisitor chopper = new Chopper(pool, new Probability());
        sequence.accept(chopper);
    }


    @Test
    public void testChopSequenceOfOne() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item = new Branch();
        sequence.genes.add(item);
        GeneVisitor chopper = new Chopper(pool, new Probability());
        sequence.accept(chopper);
    }


    @Test
    public void testChopSequenceOfMany() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item1 = new Branch();
        Gene item2 = new Chop();
        Gene item3 = new Copy();
        Gene item4 = new Get();
        Gene item5 = new Merge();
        Gene item6 = new Mutate();
        Gene item7 = new Put();
        Gene item8 = new Sequence();
        sequence.genes.add(item1);
        sequence.genes.add(item2);
        sequence.genes.add(item3);
        sequence.genes.add(item4);
        sequence.genes.add(item5);
        sequence.genes.add(item6);
        sequence.genes.add(item7);
        sequence.genes.add(item8);
        GeneVisitor chopper = new Chopper(pool, new Probability());
        sequence.accept(chopper);
    }


}
