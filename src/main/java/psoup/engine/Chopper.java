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
import psoup.*;
import psoup.genes.*;


/**
 * This class implements the visitor pattern and randomly "chops" up another
 * creature's genes based on a specifc probability that is associated with
 * this chopper.  The probability is weighted based on the temperature of
 * the gene pool.  The actual chopping depends on the type of gene the
 * chopper is visiting.
 *
 * @author Derk Norton
 */
public final class Chopper implements GeneVisitor {

    public Chopper(Pool pool, Probability probability) {
        this.pool = pool;
        this.probability = probability;
    }


    @Override
    public void visit(Branch gene) {
        // chop up the left branch
        Gene left = gene.leftBranch;
        if (left != null) {
            left.accept(this);
            if (pool.weightedCoinFlip(probability)) {
                gene.leftBranch = null;
                pool.putCreature(left);
            }
        }

        // chop up the right branch
        Gene right = gene.rightBranch;
        if (right != null) {
            right.accept(this);
            if (pool.weightedCoinFlip(probability)) {
                gene.rightBranch = null;
                pool.putCreature(right);
            }
        }

    }


    @Override
    public void visit(Chop gene) {
        // nothing to chop
    }


    @Override
    public void visit(Copy gene) {
        // nothing to chop
    }


    @Override
    public void visit(Get gene) {
        // nothing to chop
    }


    @Override
    public void visit(Merge gene) {
        // nothing to chop
    }


    @Override
    public void visit(Mutate gene) {
        // nothing to chop
    }


    @Override
    public void visit(Put gene) {
        // nothing to chop
    }


    @Override
    public void visit(Sequence gene) {
        for (int i = 0; i < gene.genes.size(); i++) {
            Gene item = gene.genes.get(i);
            item.accept(this);
            if (pool.weightedCoinFlip(probability)) {
                gene.genes.remove(i--);
                pool.putCreature(item);
            }
        }
    }


    private final Pool pool;
    private final Probability probability;


}
