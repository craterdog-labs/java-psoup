/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.engine;

import psoup.*;
import psoup.util.*;


/**
 * This class provides a single thread of processing for the evolution
 * engine.  Each processing thread grabs one creature from the gene pool
 * and "executes" its genes to perform operations on other creatures
 * using gene visitors.  This process is repeated in a loop forever.
 *
 * @author Derk Norton
 */
final class ProcessingThread extends Thread {

    ProcessingThread(ThreadGroup group, String name, Pool pool, SharedCounter generationCounter) {
        super(group, name);
        this.pool = pool;
        this.generationCounter = generationCounter;
    }


    @Override
    public void run() {
        try {
            Processor processor = new Processor(pool);
            while (!Thread.interrupted()) {
                Gene creature = pool.getCreature(0);
                if (creature != null) {
                    creature.accept(processor);
                    pool.putCreature(creature);
                    generationCounter.increment();
                }
                processor.reset();
                Thread.yield();
            }
        } catch (RuntimeException e) {
            System.err.println("Thread exiting with exception:\n" + e.getMessage());
            e.printStackTrace();
        }
    }


    private final Pool pool;
    private final SharedCounter generationCounter;


}
