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
 * from the gene pool and put on the processing stack.  The creature's
 * genes can then be operated on by other genes.
 *
 * @author Derk Norton
 */
public final class Get extends Gene {

    public Get() {
        super("Get");
    }


    @Override
    public void accept(GeneVisitor visitor) {
        visitor.visit(this);
    }


    public Gene template;

}
