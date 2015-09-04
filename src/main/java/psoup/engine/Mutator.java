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
 * This class implements the visitor pattern and randomly "mutates" the genes
 * of a creature based on a specifc probability that is associated
 * with this mutator.  The probability is weighted based on the temperature of
 * the gene pool.  The actual mutating depends on the type of gene the
 * mutator is visiting.
 *
 * @author Derk Norton
 */
public final class Mutator implements GeneVisitor {

    public Mutator(Pool pool, Probability probability) {
        this.pool = pool;
        this.probability = probability;
    }


    @Override
    public void visit(Branch gene) {
        // mutate the left branch
        Gene left = gene.leftBranch;
        if (left != null) {
            left.accept(this);
        }

        // mutate the right branch
        Gene right = gene.rightBranch;
        if (right != null) {
            right.accept(this);
        }

        // mutate the gene itself
        if (pool.weightedCoinFlip(probability)) {
            gene.probability = new Probability();
        }
        if (pool.weightedCoinFlip(probability)) {
            gene.leftBranch = right;
            gene.rightBranch = left;
        }

    }


    @Override
    public void visit(Chop gene) {
        if (pool.weightedCoinFlip(probability)) {
            gene.probability = new Probability();
        }
    }


    @Override
    public void visit(Copy gene) {
        // nothing to mutate
    }


    @Override
    public void visit(Get gene) {
        // mutate the template
        Gene template = gene.template;
        if (template != null) {
            template.accept(this);
        }

    }


    @Override
    public void visit(Merge gene) {
        // nothing to mutate
    }


    @Override
    public void visit(Mutate gene) {
        if (pool.weightedCoinFlip(probability)) {
            gene.probability = new Probability();
        }
    }


    @Override
    public void visit(Put gene) {
        // nothing to mutate
    }


    @Override
    public void visit(Sequence gene) {
        // mutate the items
        for (Gene g : gene.genes) {
            g.accept(this);
        }

        // mutate the gene itself
        if (gene.genes.size() > 1 && pool.weightedCoinFlip(probability)) {
            Probability randomizer = new Probability();
            int index = (int) (((double) gene.genes.size() - 1) * randomizer.toDouble());
            Gene swap = gene.genes.remove(index);
            randomizer = new Probability();
            index = (int) (((double) gene.genes.size()) * randomizer.toDouble());
            gene.genes.add(index, swap);
        }
    }


    private final Pool pool;
    private final Probability probability;


}
