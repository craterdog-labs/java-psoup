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

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.genes.*;


public class GeneVisitorTest {

    static XLogger logger = XLoggerFactory.getXLogger(GeneVisitorTest.class);


    @Test
    public void testVisitBranch() {
        GeneVisitor visitor = new TestVisitor();
        Branch branch = new Branch();
        branch.accept(visitor);
    }


    @Test
    public void testVisitChop() {
        GeneVisitor visitor = new TestVisitor();
        Chop chop = new Chop();
        chop.accept(visitor);
    }


    @Test
    public void testVisitCopy() {
        GeneVisitor visitor = new TestVisitor();
        Copy copy = new Copy();
        copy.accept(visitor);
    }


    @Test
    public void testVisitGet() {
        GeneVisitor visitor = new TestVisitor();
        Get get = new Get();
        get.accept(visitor);
    }


    @Test
    public void testVisitMerge() {
        GeneVisitor visitor = new TestVisitor();
        Merge merge = new Merge();
        merge.accept(visitor);
    }


    @Test
    public void testVisitMutate() {
        GeneVisitor visitor = new TestVisitor();
        Mutate mutate = new Mutate();
        mutate.accept(visitor);
    }


    @Test
    public void testVisitPut() {
        GeneVisitor visitor = new TestVisitor();
        Put put = new Put();
        put.accept(visitor);
    }


    @Test
    public void testVisitSequence() {
        GeneVisitor visitor = new TestVisitor();
        Sequence sequence = new Sequence();
        sequence.accept(visitor);
    }


}
