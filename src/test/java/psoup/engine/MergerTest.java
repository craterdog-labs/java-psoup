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
import java.util.*;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.genes.*;
import psoup.pool.*;

public class MergerTest {

    static XLogger logger = XLoggerFactory.getXLogger(MergerTest.class);


    @Test
    public void testMergeEmptyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        branch.accept(merger);
    }


    @Test
    public void testMergeLeftOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        branch.leftBranch = left;
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        branch.accept(merger);
    }


    @Test
    public void testMergeRightOnlyBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch right = new Branch();
        branch.rightBranch = right;
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        branch.accept(merger);
    }


    @Test
    public void testMergeDualBranch() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Branch branch = new Branch();
        Branch left = new Branch();
        Branch right = new Branch();
        branch.leftBranch = left;
        branch.rightBranch = right;
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        branch.accept(merger);
    }


    @Test
    public void testMergeChop() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Chop chop = new Chop();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        chop.accept(merger);
    }


    @Test
    public void testMergeCopy() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Copy copy = new Copy();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        copy.accept(merger);
    }


    @Test
    public void testMergeNullGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        get.template = null;
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        get.accept(merger);
    }


    @Test
    public void testMergeGet() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Get get = new Get();
        Gene template = new Branch();
        get.template = template;
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        get.accept(merger);
    }


    @Test
    public void testMergeMerge() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Merge merge = new Merge();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        merge.accept(merger);
    }


    @Test
    public void testMergeMutate() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Mutate mutate = new Mutate();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        mutate.accept(merger);
    }


    @Test
    public void testMergePut() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Put put = new Put();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        put.accept(merger);
    }


    @Test
    public void testMergeEmptySequence() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        sequence.accept(merger);
    }


    @Test
    public void testMergeSequenceOfOne() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item = new Branch();
        sequence.genes.add(item);
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        sequence.accept(merger);
    }


    @Test
    public void testMergeSequenceOfMany() {
        Pool pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(0.5));
        Sequence sequence = new Sequence();
        Gene item1 = new Branch();
        Gene item2 = new Merge();
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
        Stack<Gene> stack = new Stack<>();
        stack.push(new Branch());
        stack.push(new Chop());
        stack.push(new Copy());
        stack.push(new Get());
        stack.push(new Merge());
        stack.push(new Mutate());
        stack.push(new Put());
        stack.push(new Sequence());
        GeneVisitor merger = new Merger(pool, stack);
        sequence.accept(merger);
    }


}
