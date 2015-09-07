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
 * This class implements the gene visitor pattern and recursively
 * "grows" each gene in a new creature based on the desired size
 * and complexity of the creatures..
 *
 * @author Derk Norton
 */
public final class Initializer implements GeneVisitor {

    public Initializer(Pool pool, Probability relativeComplexity, int maximumDepth, SharedCounter geneCounter) {
        this.pool = pool;
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
        // initialize the species
        gene.speciesId = pool.pickRandomSpecies();
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


    public Gene generateCreature() {
        // apply the visitor pattern
        Gene creature = generateGene();
        creature.accept(this);
        return creature;
    }


    private Gene generateGene() {
        Gene gene;

        // randomly create one of the eight gene types
        int pick = RandomUtils.pickRandomIndex(13);
        switch(pick) {
            case 0:
                gene = new Branch();
                break;
            case 1:
                gene = new Chop();
                break;
            case 2:
                gene = new Copy();
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                gene = new Get();
                break;
            case 8:
            case 9:
                gene = new Merge();
                break;
            case 10:
                gene = new Mutate();
                break;
            case 11:
                gene = new Put();
                break;
            case 12:
                gene = new Sequence();
                break;
            default: // should never happen!
                gene = new Sequence();
                break;
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


    private final Pool pool;
    private final Probability relativeComplexity;
    private int currentDepth;
    private final int maximumDepth;
    private final SharedCounter geneCounter;

}
