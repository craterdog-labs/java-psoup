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
import psoup.*;
import psoup.genes.*;


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
        // flip a coin to decide which branch to process
        if (Probability.coinToss(gene.probability)) {
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
        // if there is a creature on the stack, chop it and put its pieces back on the stack
        if (!stack.empty()) {
            Gene creature = stack.pop();
            Chopper chopper = new Chopper(pool, gene.probability);
            creature.accept(chopper);
            stack.push(creature);
        }
    }


    @Override
    public void visit(Copy gene) {
        // if there is a creature on the stack, copy it and put them both back on the stack
        if (!stack.empty()) {
            Gene creature = stack.pop();
            Copier copier = new Copier(pool, gene.probability);
            creature.accept(copier);
            stack.push(creature);
            Gene copy = copier.copy;
            if (copy != null) {
                stack.push(copy);
            }
        }
    }


    @Override
    public void visit(Get gene) {
        // if the gene has a template, get a matching creature from the pool and put it on the stack
        Gene template = gene.template;
        if (template != null) {
            Gene match = pool.getCreature(template.getSpeciesId());
            if (match != null) {
                stack.push(match);
            }
        }
    }


    @Override
    public void visit(Merge gene) {
        // if there are multiple creatures on the stack, merge them and put them back on the stack
        if (stack.size() > 1) {
            Gene creature = stack.pop();
            Merger merger = new Merger(pool, stack);
            creature.accept(merger);
            stack.push(creature);
        }
    }


    @Override
    public void visit(Mutate gene) {
        // if there is a creature on the stack, mutate it and put it back on the stack
        if (!stack.empty()) {
            Gene creature = stack.pop();
            Mutator mutator = new Mutator(pool, gene.probability);
            creature.accept(mutator);
            stack.push(creature);
        }
    }


    @Override
    public void visit(Put gene) {
        // if there is a creature on the stack, throw it back in the pool
        if (!stack.empty()) {
            Gene creature = stack.pop();
            pool.putCreature(creature);
        }
    }


    @Override
    public void visit(Sequence gene) {
        // process each gene in the sequence in order
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
