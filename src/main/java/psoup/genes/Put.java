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

import psoup.*;
import psoup.util.*;


/**
 * This class defines a type of gene that causes a creature to be pulled
 * from the processing stack and put back into the gene pool.  The creature
 * is then free to act on other creatures.
 *
 * @author Derk Norton
 */
public final class Put extends Gene {

    public Put() {
        super("Put");
    }


    @Override
    public void accept(GeneVisitor visitor) {
        visitor.visit(this);
    }

}
