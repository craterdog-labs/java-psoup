/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.genes;

import java.util.*;
import psoup.*;
import psoup.util.*;


/**
 * This class defines a type of gene that may have a sequence of children
 * genes.  The children genes are traversed in order during execution.
 *
 * @author Derk Norton
 */
public final class Sequence extends Gene {

    public Sequence() {
        super("Sequence");
        genes = new ArrayList<>();
    }


    @Override
    public void accept(GeneVisitor visitor) {
        visitor.visit(this);
    }


    public List<Gene> genes;

}
