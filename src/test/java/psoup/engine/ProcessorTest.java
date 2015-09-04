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


public class ProcessorTest {

    static XLogger logger = XLoggerFactory.getXLogger(ProcessorTest.class);


    @Test
    public void testProcessEmptyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        GeneVisitor processor = new Processor(pool);
        branch.accept(processor);
    }


    @Test
    public void testProcessLeftOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        branch.leftBranch = left;
        GeneVisitor processor = new Processor(pool);
        branch.accept(processor);
    }


    @Test
    public void testProcessRightOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch right = new Branch();
        branch.rightBranch = right;
        GeneVisitor processor = new Processor(pool);
        branch.accept(processor);
    }


    @Test
    public void testProcessDualBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        Branch right = new Branch();
        branch.leftBranch = left;
        branch.rightBranch = right;
        GeneVisitor processor = new Processor(pool);
        branch.accept(processor);
    }


    @Test
    public void testProcessChop() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Chop chop = new Chop();
        GeneVisitor processor = new Processor(pool);
        chop.accept(processor);
    }


    @Test
    public void testProcessCopy() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Copy copy = new Copy();
        GeneVisitor processor = new Processor(pool);
        copy.accept(processor);
    }


    @Test
    public void testProcessNullGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        get.template = null;
        GeneVisitor processor = new Processor(pool);
        get.accept(processor);
    }


    @Test
    public void testProcessGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        Gene template = new Branch();
        get.template = template;
        GeneVisitor processor = new Processor(pool);
        get.accept(processor);
    }


    @Test
    public void testProcessMerge() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Merge merge = new Merge();
        GeneVisitor processor = new Processor(pool);
        merge.accept(processor);
    }


    @Test
    public void testProcessMutate() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Mutate mutate = new Mutate();
        GeneVisitor processor = new Processor(pool);
        mutate.accept(processor);
    }


    @Test
    public void testProcessPut() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Put put = new Put();
        GeneVisitor processor = new Processor(pool);
        put.accept(processor);
    }


    @Test
    public void testProcessEmptySequence() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        GeneVisitor processor = new Processor(pool);
        sequence.accept(processor);
    }


    @Test
    public void testProcessSequenceOfOne() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item = new Branch();
        sequence.genes.add(item);
        GeneVisitor processor = new Processor(pool);
        sequence.accept(processor);
    }


    @Test
    public void testProcessSequenceOfMany() {
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
        GeneVisitor processor = new Processor(pool);
        sequence.accept(processor);
    }


}
