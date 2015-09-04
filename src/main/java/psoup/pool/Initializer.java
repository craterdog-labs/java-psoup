/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.pool;

import psoup.GeneVisitor;
import craterdog.primitives.Probability;
import craterdog.utils.RandomUtils;
import psoup.*;
import psoup.genes.*;
import psoup.util.*;


/**
 * This class implements the gene visitor pattern and initializes
 * each gene in a new creature based on the desired size and
 * complexity of the creatures..
 *
 * @author Derk Norton
 */
public final class Initializer implements GeneVisitor {

    public Initializer(Probability relativeComplexity, int maximumDepth, SharedCounter geneCounter) {
        this.relativeComplexity = relativeComplexity;
        this.currentDepth = 0;
        this.maximumDepth = maximumDepth;
        this.geneCounter = geneCounter;
    }


    @Override
    public void visit(Branch gene) {

        // initialize the left branch
        if (coinFlip()) {
            Gene left = generateGene();
            gene.leftBranch = left;
            initializeGene(left);
        }

        // initialize the right branch
        if (coinFlip()) {
            Gene right = generateGene();
            gene.rightBranch = right;
            initializeGene(right);
        }

        // initialize the probability
        gene.probability = new Probability();

    }


    @Override
    public void visit(Chop gene) {
        // initialize the probability
        gene.probability = new Probability();
    }


    @Override
    public void visit(Copy gene) {
        // nothing to initialize
    }


    @Override
    public void visit(Get gene) {
        // initialize the template
        if (coinFlip()) {
            Gene template = generateGene();
            gene.template = template;
            initializeGene(template);
        }
    }


    @Override
    public void visit(Merge gene) {
        // nothing to initialize
    }


    @Override
    public void visit(Mutate gene) {
        // initialize the probability
        gene.probability = new Probability();
    }


    @Override
    public void visit(Put gene) {
        // nothing to initialize
    }


    @Override
    public void visit(Sequence gene) {
        // initialize the sequence
        int currentLength = currentDepth;
        while (coinFlip() && currentLength < maximumDepth) {
            Gene item = generateGene();
            gene.genes.add(item);
            currentLength++;
            initializeGene(item);
        }
    }


    static public Gene generateCreature(Probability relativeComplexity, int maximumDepth, SharedCounter geneCounter) {
        // apply the visitor pattern
        Initializer initializer = new Initializer(relativeComplexity, maximumDepth, geneCounter);
        Gene creature = initializer.generateGene();
        creature.accept(initializer);
        return creature;
    }


    private Gene generateGene() {
        Gene gene;

        // randomly create one of the eight gene types
        int pick = RandomUtils.pickRandomIndex(8);
        switch(pick) {
            case 0: gene = new Branch(); break;
            case 1: gene = new Chop(); break;
            case 2: gene = new Copy(); break;
            case 3: gene = new Get(); break;
            case 4: gene = new Merge(); break;
            case 5: gene = new Mutate(); break;
            case 6: gene = new Put(); break;
            case 7: gene = new Sequence(); break;
            default: gene = new Sequence(); break; // should never happen!
        }
        geneCounter.increment();

        return gene;
    }


    private void initializeGene(Gene gene) {
        if (currentDepth < maximumDepth) {
            currentDepth++;
            gene.accept(this);
            currentDepth--;
        }
    }


    private boolean coinFlip() {
        Probability probability = Probability.or(relativeComplexity, new Probability());
        return Probability.coinToss(probability);
    }


    private final Probability relativeComplexity;
    private int currentDepth;
    private final int maximumDepth;
    private final SharedCounter geneCounter;

}
