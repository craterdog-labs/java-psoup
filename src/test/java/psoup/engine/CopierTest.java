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


public class CopierTest {

    static XLogger logger = XLoggerFactory.getXLogger(CopierTest.class);


    @Test
    public void testCopyEmptyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        GeneVisitor copier = new Copier(pool, new Probability());
        branch.accept(copier);
    }


    @Test
    public void testCopyLeftOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        branch.leftBranch = left;
        GeneVisitor copier = new Copier(pool, new Probability());
        branch.accept(copier);
    }


    @Test
    public void testCopyRightOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch right = new Branch();
        branch.rightBranch = right;
        GeneVisitor copier = new Copier(pool, new Probability());
        branch.accept(copier);
    }


    @Test
    public void testCopyDualBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        Branch right = new Branch();
        branch.leftBranch = left;
        branch.rightBranch = right;
        GeneVisitor copier = new Copier(pool, new Probability());
        branch.accept(copier);
    }


    @Test
    public void testCopyChop() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Chop chop = new Chop();
        GeneVisitor copier = new Copier(pool, new Probability());
        chop.accept(copier);
    }


    @Test
    public void testCopyCopy() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Copy copy = new Copy();
        GeneVisitor copier = new Copier(pool, new Probability());
        copy.accept(copier);
    }


    @Test
    public void testCopyNullGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        get.template = null;
        GeneVisitor copier = new Copier(pool, new Probability());
        get.accept(copier);
    }


    @Test
    public void testCopyGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        Gene template = new Branch();
        get.template = template;
        GeneVisitor copier = new Copier(pool, new Probability());
        get.accept(copier);
    }


    @Test
    public void testCopyMerge() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Merge merge = new Merge();
        GeneVisitor copier = new Copier(pool, new Probability());
        merge.accept(copier);
    }


    @Test
    public void testCopyMutate() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Mutate mutate = new Mutate();
        GeneVisitor copier = new Copier(pool, new Probability());
        mutate.accept(copier);
    }


    @Test
    public void testCopyPut() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Put put = new Put();
        GeneVisitor copier = new Copier(pool, new Probability());
        put.accept(copier);
    }


    @Test
    public void testCopyEmptySequence() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        GeneVisitor copier = new Copier(pool, new Probability());
        sequence.accept(copier);
    }


    @Test
    public void testCopySequenceOfOne() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item = new Branch();
        sequence.genes.add(item);
        GeneVisitor copier = new Copier(pool, new Probability());
        sequence.accept(copier);
    }


    @Test
    public void testCopySequenceOfMany() {
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
        GeneVisitor copier = new Copier(pool, new Probability());
        sequence.accept(copier);
    }


}
