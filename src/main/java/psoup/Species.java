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
import craterdog.smart.SmartObject;
import java.util.*;


/**
 * This class manages all creatures that belong to the same species.
 * Creatures belong to the same species have the same genetic structure
 * but may have different probability values within matching genes.
 *
 * @author Derk Norton
 */
public final class Species extends SmartObject<Species> {

    public Species() {
        this.members = new ArrayList<>();
    }


    public Gene selectMember() {
        int index = Math.round(((float) Math.random()) * (members.size() - 1));
        Gene result = members.remove(index);
        return result;
    }


    public boolean isExtinct() {
        return members.isEmpty();
    }


    @JsonIgnore
    public Iterator<Gene> getIterator() {
        return members.iterator();
    }


    public final List<Gene> members;

}
