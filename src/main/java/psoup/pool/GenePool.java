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

import craterdog.primitives.Probability;
import craterdog.smart.SmartObject;
import craterdog.smart.SmartObjectMapper;
import craterdog.utils.RandomUtils;
import psoup.*;
import psoup.genes.*;
import psoup.util.*;
import java.util.*;
import java.io.*;


/**
 * This class implements the gene pool.  It is shared by all processors
 * that pull creatures out of it and operate on them before returning
 * the creatures to the pool.  This class is thread-safe.
 *
 * @author Derk Norton
 */
public final class GenePool extends SmartObject<GenePool> implements Pool {

    static public final int BRANCH_ID = new Branch().getSpeciesId();
    static public final int CHOP_ID = new Chop().getSpeciesId();
    static public final int COPY_ID = new Copy().getSpeciesId();
    static public final int GET_ID = new Get().getSpeciesId();
    static public final int MERGE_ID = new Merge().getSpeciesId();
    static public final int MUTATE_ID = new Mutate().getSpeciesId();
    static public final int PUT_ID = new Put().getSpeciesId();
    static public final int SEQUENCE_ID = new Sequence().getSpeciesId();

    public GenePool() {
        pool = new HashMap<>();
    }


    @Override
    public synchronized void initialize(int numberOfCreatures, Probability relativeComplexity, int maximumDepth, Probability temperature) {
        // reset the existing state
        this.pool.clear();
        this.geneCounter.resetCounter();
        this.speciesCounter.resetCounter();
        this.creatureCounter.resetCounter();

        // initialize the pool
        this.temperature = temperature;
        Initializer initializer = new Initializer(this, relativeComplexity, maximumDepth, geneCounter);
        for (int i = 0; i < numberOfCreatures; i++) {
            Gene creature = initializer.generateCreature();
            putCreature(creature);
        }

        // set the high and low water marks
        this.geneCounter.resetWaterMarks();
        this.speciesCounter.resetWaterMarks();
        this.creatureCounter.resetWaterMarks();
    }


    static public GenePool loadGenePool(String fileName) throws IOException {
        SmartObjectMapper mapper = new SmartObjectMapper();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            GenePool pool = mapper.readValue(fileInputStream, GenePool.class);
            return pool;
        }
    }


    static public void storeGenePool(GenePool pool, String fileName) throws IOException {
        SmartObjectMapper mapper = new SmartObjectMapper();
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            mapper.writeValue(fileOutputStream, pool);
        }
   }


    @Override
    public synchronized Gene getCreature(int speciesId) {

        // if the gene pool is empty, return null
        int currentNumberOfSpecies = getCurrentNumberOfSpecies();
        if (currentNumberOfSpecies == 0L) {
            return null;
        }

        // if looking for any species, pick one at random
        if (speciesId == 0) {
            speciesId = pickRandomSpecies();
        }

        // remove a random creature of the desired species (if any exist)
        Species species = pool.get(speciesId);
        if (species == null) return null;
        Gene creature = species.selectMember();
        if (creature == null) return null;
        creatureCounter.decrement();
        if (species.isExtinct()) {
            pool.remove(speciesId);
            speciesCounter.decrement();
            raiseTemperature();
        }
        return creature;
    }


    @Override
    public synchronized void putCreature(Gene creature) {
        // retrieve the list of creatures that belong to the same species
        int speciesId = creature.getSpeciesId();
        Species species = pool.get(speciesId);

        // if this is a new species create a new creature list for it
        if (species == null) {
            species = new Species();
            pool.put(speciesId, species);
            speciesCounter.increment();
            lowerTemperature();
        }

        // add the new creature to the list
        species.members.add(creature);
        creatureCounter.increment();
    }


    @Override
    public synchronized int pickRandomSpecies() {
        int speciesId = 0;
        int numberOfSpecies = getCurrentNumberOfSpecies();
        if (numberOfSpecies > 0) {
            int index = RandomUtils.pickRandomIndex(numberOfSpecies);
            speciesId = (Integer) pool.keySet().toArray()[index];
        }
        return speciesId;
    }


    @Override
    public synchronized int getCurrentNumberOfGenes() {
        return geneCounter.getCurrentValue();
    }


    @Override
    public synchronized int getLowestNumberOfGenes() {
        return geneCounter.getLowWaterMark();
    }


    @Override
    public synchronized int getHighestNumberOfGenes() {
        return geneCounter.getHighWaterMark();
    }


    @Override
    public synchronized int getCurrentNumberOfSpecies() {
        return speciesCounter.getCurrentValue();
    }


    @Override
    public synchronized int getLowestNumberOfSpecies() {
        return speciesCounter.getLowWaterMark();
    }


    @Override
    public synchronized int getHighestNumberOfSpecies() {
        return speciesCounter.getHighWaterMark();
    }


    @Override
    public synchronized int getCurrentNumberOfCreatures() {
        return creatureCounter.getCurrentValue();
    }


    @Override
    public synchronized int getLowestNumberOfCreatures() {
        return creatureCounter.getLowWaterMark();
    }


    @Override
    public synchronized int getHighestNumberOfCreatures() {
        return creatureCounter.getHighWaterMark();
    }


    @Override
    public synchronized void resetWaterMarks() {
        geneCounter.resetWaterMarks();
        speciesCounter.resetWaterMarks();
        creatureCounter.resetWaterMarks();
    }


    @Override
    public synchronized Probability getTemperature() {
        return temperature;
    }


    @Override
    public synchronized void setTemperature(Probability temperature) {
        this.temperature = temperature;
    }


    @Override
    public synchronized boolean weightedCoinFlip(Probability probability) {
        boolean result = Probability.coinToss(Probability.and(probability, temperature));
        return result;
    }


    private void raiseTemperature() {
        double current = temperature.toDouble();
        if (current < 1.0d - TEMPERATURE_DELTA) {
            temperature = new Probability(current + TEMPERATURE_DELTA);
        }
    }


    private void lowerTemperature() {
        double current = temperature.toDouble();
        if (current > TEMPERATURE_DELTA) {
            temperature = new Probability(current - TEMPERATURE_DELTA);
        }
    }


    public final Map<Integer, Species> pool;
    public Probability temperature;
    public final SharedCounter geneCounter = new SharedCounter();
    public final SharedCounter speciesCounter = new SharedCounter();
    public final SharedCounter creatureCounter = new SharedCounter();

    static private final double TEMPERATURE_DELTA = 0.00001d;


}
