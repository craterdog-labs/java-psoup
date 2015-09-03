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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import craterdog.smart.SmartObject;
import craterdog.smart.SmartObjectMapper;
import craterdog.utils.UniversalHashFunction;
import psoup.util.UseNullAsValueMixIn;

 
/**
 * This class encapsulates the genes associated with a specific creature.  Each creature
 * belongs to a particulary species based on its gene structure.
 *
 * @author Derk Norton
 */
public final class Creature extends SmartObject<Creature> {

    static private final SmartObjectMapper mapper = new SmartObjectMapper();

    // define a universal hash function that provides consistent hashes across time
    static private final UniversalHashFunction hasher = new UniversalHashFunction(32, 1491978955, 0);

    static {
        mapper.addMixIn(craterdog.primitives.Probability.class, UseNullAsValueMixIn.class);
    }


    public Creature() {
        this.genes = null;
    }


    public Creature(Gene genes) {
        this.genes = genes;
    }


    @JsonIgnore
    public int getSpeciesId() {
        try {
            return mapper.writeValueAsString(genes).hashCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to calulate speciesId for creature.", e);
        }
    }


    public final Gene genes;

}
