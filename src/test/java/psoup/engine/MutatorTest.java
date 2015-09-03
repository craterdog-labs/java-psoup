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
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.genes.*;
import psoup.util.*;
import psoup.pool.*;


public class MutatorTest {

    static XLogger logger = XLoggerFactory.getXLogger(MutatorTest.class);


    @Test
    public void testMutateEmptyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        branch.accept(mutator);
    }


    @Test
    public void testMutateLeftOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        branch.leftBranch = left;
        GeneVisitor mutator = new Mutator(pool, new Probability());
        branch.accept(mutator);
    }


    @Test
    public void testMutateRightOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch right = new Branch();
        branch.rightBranch = right;
        GeneVisitor mutator = new Mutator(pool, new Probability());
        branch.accept(mutator);
    }


    @Test
    public void testMutateDualBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        Branch right = new Branch();
        branch.leftBranch = left;
        branch.rightBranch = right;
        GeneVisitor mutator = new Mutator(pool, new Probability());
        branch.accept(mutator);
    }


    @Test
    public void testMutateChop() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Chop chop = new Chop();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        chop.accept(mutator);
    }


    @Test
    public void testMutateCopy() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Copy copy = new Copy();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        copy.accept(mutator);
    }


    @Test
    public void testMutateNullGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        get.template = null;
        GeneVisitor mutator = new Mutator(pool, new Probability());
        get.accept(mutator);
    }


    @Test
    public void testMutateGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        Gene template = new Branch();
        get.template = template;
        GeneVisitor mutator = new Mutator(pool, new Probability());
        get.accept(mutator);
    }


    @Test
    public void testMutateMerge() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Merge merge = new Merge();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        merge.accept(mutator);
    }


    @Test
    public void testMutateMutate() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Mutate mutate = new Mutate();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        mutate.accept(mutator);
    }


    @Test
    public void testMutatePut() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Put put = new Put();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        put.accept(mutator);
    }


    @Test
    public void testMutateEmptySequence() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        GeneVisitor mutator = new Mutator(pool, new Probability());
        sequence.accept(mutator);
    }


    @Test
    public void testMutateSequenceOfOne() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item = new Branch();
        sequence.genes.add(item);
        GeneVisitor mutator = new Mutator(pool, new Probability());
        sequence.accept(mutator);
    }


    @Test
    public void testMutateSequenceOfMany() {
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
        GeneVisitor mutator = new Mutator(pool, new Probability());
        sequence.accept(mutator);
    }


}
