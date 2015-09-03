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


/**
 * This interface defines the methods that must be supported by each
 * component that can cause evolution within the gene pool.
 *
 * @author Derk Norton
 */
public interface Evolver {

    public void startEvolving(int threadCount);
    public void stopEvolving();
    public boolean isEvolving();
    public int getNumberOfGenerations();
    public int getNumberOfActiveThreads();

}
