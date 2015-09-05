/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup;

import craterdog.primitives.Probability;


/**
 * This interface defines the methods the must be implemented
 * by a gene pool.  It specifies methods for adding and removing
 * creatures from the pool and accessing metrics about the pool.
 *
 * @author Derk Norton
 */
public interface Pool {

    public void initialize(int numberOfCreatures, Probability relativeComplexity, int maximumDepth, Probability temperature);
    public Gene getCreature(int speciesId);
    public void putCreature(Gene creature);
    public int pickRandomSpecies();
    public int getCurrentNumberOfGenes();
    public int getLowestNumberOfGenes();
    public int getHighestNumberOfGenes();
    public int getCurrentNumberOfSpecies();
    public int getLowestNumberOfSpecies();
    public int getHighestNumberOfSpecies();
    public int getCurrentNumberOfCreatures();
    public int getLowestNumberOfCreatures();
    public int getHighestNumberOfCreatures();
    public void resetWaterMarks();
    public Probability getTemperature();
    public void setTemperature(Probability temperature);
    public boolean weightedCoinFlip(Probability probability);

}
