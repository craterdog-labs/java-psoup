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

import psoup.GeneVisitor;
import craterdog.primitives.Probability;
import psoup.*;


/**
 * This class defines a type of gene that may have a left and/or right
 * child gene.  The branch gene also has a probability that is used to
 * decide which branch to traverse during execution.
 *
 * @author Derk Norton
 */
public final class Branch extends Gene {

    public Branch() {
        super("Branch");
        probability = new Probability(0.5);
    }


    @Override
    public void accept(GeneVisitor visitor) {
        visitor.visit(this);
    }


    public Probability probability;
    public Gene leftBranch;
    public Gene rightBranch;

}
