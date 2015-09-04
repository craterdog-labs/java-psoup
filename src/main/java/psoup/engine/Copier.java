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
import craterdog.smart.SmartObjectMapper;
import psoup.*;
import psoup.genes.*;
import psoup.pool.GenePool;
import psoup.util.*;


/**
 * This class implements the visitor pattern and randomly "copies" another
 * creature's genes based on a specifc probability that is associated with
 * this copier.  The probability is weighted based on the temperature of
 * the gene pool.  The actual copying depends on the type of gene the
 * copier is visiting.
 *
 * @author Derk Norton
 */
public final class Copier implements GeneVisitor {

    static private final SmartObjectMapper mapper = new SmartObjectMapper();

    static {
        mapper.addMixIn(craterdog.primitives.Probability.class, UseNullAsValueMixIn.class);
    }


    public Copier(Pool pool, Probability probability) {
        this.pool = pool;
        this.probability = probability;
        this.copy = null;
    }


    @Override
    public void visit(Branch gene) {

        // copy the gene itself
        Gene creature =  pool.getCreature(GenePool.BRANCH_ID);
        Branch branch = null;
        if (creature != null) {
            branch = (Branch) creature;

            // copy the probability
            branch.probability = gene.probability;

            // copy the left branch
            Gene left = gene.leftBranch;
            if (left != null && pool.weightedCoinFlip(probability)) {
                left.accept(this);
                branch.leftBranch = copy;
            }

            // copy the right branch
            Gene right = gene.rightBranch;
            if (right != null && pool.weightedCoinFlip(probability)) {
                right.accept(this);
                branch.rightBranch = copy;
            }

        }
        copy = branch;

    }


    @Override
    public void visit(Chop gene) {
        Chop creature = (Chop) pool.getCreature(GenePool.CHOP_ID);
        if (creature != null) {
            creature.probability = gene.probability;
        }
        copy = creature;
    }


    @Override
    public void visit(Copy gene) {
        Copy creature = (Copy) pool.getCreature(GenePool.COPY_ID);
        if (creature != null) {
            creature.probability = gene.probability;
        }
        copy = creature;
    }


    @Override
    public void visit(Get gene) {

        // copy the gene itself
        Gene creature = pool.getCreature(GenePool.GET_ID);
        Get get = null;
        if (creature != null) {
            get = (Get) creature;
            // copy the template
            Gene template = gene.template;
            if (template != null) {
                template.accept(this);
                get.template = copy;
            }
        }

        copy = get;

    }


    @Override
    public void visit(Merge gene) {
        Merge creature = (Merge) pool.getCreature(GenePool.MERGE_ID);
        if (creature != null) {
            creature.probability = gene.probability;
        }
        copy = creature;
    }


    @Override
    public void visit(Mutate gene) {
        Mutate creature = (Mutate) pool.getCreature(GenePool.MUTATE_ID);
        if (creature != null) {
            creature.probability = gene.probability;
        }
        copy = creature;
    }


    @Override
    public void visit(Put gene) {
        Gene creature = pool.getCreature(GenePool.PUT_ID);
        Put put = null;
        if (creature != null) {
            put = (Put) creature;
        }
        copy = put;
    }


    @Override
    public void visit(Sequence gene) {

        // copy the gene itself
        Gene creature = pool.getCreature(GenePool.SEQUENCE_ID);
        Sequence sequence = null;
        if (creature != null) {
            sequence = (Sequence) creature;
            for (Gene item : gene.genes) {
                item.accept(this);
                if (copy != null) {
                    sequence.genes.add(copy);
                }
            }
        }

        copy = sequence;

    }


    private final Pool pool;
    private final Probability probability;
    public Gene copy;


}
