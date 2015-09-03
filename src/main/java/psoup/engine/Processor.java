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
import java.util.*;
import psoup.*;
import psoup.genes.*;
import psoup.util.*;


/**
 * This class implements the gene visitor pattern and "executes"
 * the command that each gene specifies.  It is used by a processor
 * thread to do its work.
 *
 * @author Derk Norton
 */
public final class Processor implements GeneVisitor {

    public Processor(Pool pool) {
        this.pool = pool;
        this.stack = new Stack<>();
    }


    @Override
    public void visit(Branch gene) {
        Probability probability = gene.probability;
        if (Probability.coinToss(probability)) {
            Gene left = gene.leftBranch;
            if (left != null) {
                left.accept(this);
            }
        } else {
            Gene right = gene.rightBranch;
            if (right != null) {
                right.accept(this);
            }
        }
    }


    @Override
    public void visit(Chop gene) {
        if (!stack.empty()) {
            Gene top = stack.pop();
            Probability probability = gene.probability;
            Chopper chopper = new Chopper(pool, probability);
            top.accept(chopper);
            pool.putCreature(top);
        }
    }


    @Override
    public void visit(Copy gene) {
        if (!stack.empty()) {
            Gene top = stack.pop();
            Copier copier = new Copier(pool, gene.probability);
            top.accept(copier);
            pool.putCreature(top);
            Gene copy = copier.copy;
            if (copy != null) {
                pool.putCreature(copy);
            }
        }
    }


    @Override
    public void visit(Get gene) {
        Gene template = gene.template;
        if (template != null) {
            Gene creature = pool.getCreature(template.getSpeciesId());
            if (creature != null) {
                Gene match = creature;
                stack.push(match);
            }
        }
    }


    @Override
    public void visit(Merge gene) {
        if (!stack.empty()) {
            Gene top = stack.pop();
            Merger merger = new Merger(pool, stack);
            top.accept(merger);
            pool.putCreature(top);
        }
    }


    @Override
    public void visit(Mutate gene) {
        if (!stack.empty()) {
            Gene top = stack.pop();
            Probability probability = gene.probability;
            Mutator mutator = new Mutator(pool, probability);
            top.accept(mutator);
            pool.putCreature(top);
        }
    }


    @Override
    public void visit(Put gene) {
        if (!stack.empty()) {
            Gene top = stack.pop();
            pool.putCreature(top);
        }
    }


    @Override
    public void visit(Sequence gene) {
        for (Gene g : gene.genes) {
            g.accept(this);
        }
    }


    public void reset() {
        while (!stack.empty()) {
            Gene top = stack.pop();
            pool.putCreature(top);
        }
    }


    private final Pool pool;
    private final Stack<Gene> stack;


}
