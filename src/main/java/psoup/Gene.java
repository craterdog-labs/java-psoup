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
import psoup.util.*;


/**
 * This abstract class specifies that all genes must support being visited by
 * a gene visitor.  This allows them to be scanned and operated on in
 * various ways so that they can take on the role of data and program.
 *
 * @author Derk Norton
 */
public abstract class Gene extends SmartObject<Gene> {

    static private final SmartObjectMapper mapper = new SmartObjectMapper();

    static {
        mapper.addMixIn(craterdog.primitives.Probability.class, UseNullAsValueMixIn.class);
    }

    public Gene(String geneType) {
        this.geneType = geneType;
    }

    public abstract void accept(GeneVisitor visitor);

    @JsonIgnore
    public int getSpeciesId() {
        try {
            return mapper.writeValueAsString(this).hashCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to calulate speciesId for genes.", e);
        }
    }


    public final String geneType;

}
