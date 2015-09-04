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

import psoup.genes.*;


/**
 * This interface defines the visitor pattern methods that must be
 * implemented by all gene visitors.  It basically allows a visitor
 * to visit each type of gene.
 *
 * @author Derk Norton
 */
public interface GeneVisitor {
    
    public void visit(Get gene);
    public void visit(Put gene);
    public void visit(Merge gene);
    public void visit(Chop gene);
    public void visit(Copy gene);
    public void visit(Mutate gene);
    public void visit(Branch gene);
    public void visit(Sequence gene);

}
