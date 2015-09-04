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
 * This class implements the visitor pattern and randomly "merges" the genes
 * from different creatures based on a specifc probability that is associated
 * with this merger.  The probability is weighted based on the temperature of
 * the gene pool.  The actual merging depends on the type of gene the
 * merger is visiting.
 *
 * @author Derk Norton
 */
public final class Merger implements GeneVisitor {

    public Merger(Pool pool, Stack<Gene> stack) {
        this.stack = stack;
    }


    @Override
    public void visit(Branch gene) {
        Gene left = gene.leftBranch;
        if (left == null && !stack.empty()) {
            left = stack.pop();
            gene.leftBranch = left;
        }

        Gene right = gene.rightBranch;
        if (right == null && !stack.empty()) {
            right = stack.pop();
            gene.rightBranch = right;
        }

        if (left != null && !stack.empty()) {
            left.accept(this);
        }

        if (right != null && !stack.empty()) {
            right.accept(this);
        }
    }


    @Override
    public void visit(Chop gene) {
        // nothing to merge
    }


    @Override
    public void visit(Copy gene) {
        // nothing to merge
    }


    @Override
    public void visit(Get gene) {
        Gene template = gene.template;
        if (template == null && !stack.empty()) {
            template = stack.pop();
            gene.template = template;
        }

        if (template != null && !stack.empty()) {
            template.accept(this);
        }
    }


    @Override
    public void visit(Merge gene) {
        // nothing to merge
    }


    @Override
    public void visit(Mutate gene) {
        // nothing to merge
    }


    @Override
    public void visit(Put gene) {
        // nothing to merge
    }


    @Override
    public void visit(Sequence gene) {
        if (!stack.empty()) {
            Probability probability = new Probability();
            int index = (int) (((double) gene.genes.size()) * probability.toDouble());
            Gene top = stack.pop();
            gene.genes.add(index, top);
        }
        Iterator<Gene> iterator = gene.genes.iterator();
        while (iterator.hasNext() && !stack.empty()) {
            Gene item = iterator.next();
            item.accept(this);
        }
    }


    private final Stack<Gene> stack;


}
