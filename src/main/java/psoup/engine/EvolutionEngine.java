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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import psoup.*;
import psoup.util.*;


/**
 * This class acts on a gene pool as an "evolution agent".  It provides
 * the physical processes that allow one creature to manipulate another
 * creature's genes.
 *
 * @author Derk Norton
 */
public final class EvolutionEngine implements Evolver {

    static XLogger logger = XLoggerFactory.getXLogger(EvolutionEngine.class);


    public EvolutionEngine(Pool pool) {
        this.pool = pool;
        this.evolving = false;
        this.generationCounter = new SharedCounter();
    }


    @Override
    public synchronized void startEvolving(int threadCount) {
        if (!evolving) {
            logger.info("Starting evolution...");
            evolving = true;

            // reset any existing state
            generationCounter.resetCounter();
            if (processors != null) {
                processors.destroy();
            }

            // create a new set of low priority processors (running threads)
            processors = new ThreadGroup("Evolution Engine");
            processors.setMaxPriority(2);
            for (int i = 0; i < threadCount; i++) {
                Thread thread = new ProcessingThread(processors, "Processor " + i, pool, generationCounter);
                thread.start();
            }
        }
    }


    @Override
    public synchronized void stopEvolving() {
        if (evolving) {
            logger.info("Stopping evolution...");

            // interrupt the running processors
            processors.interrupt();

            // wait for each thread to finish
            int numberOfThreads = processors.activeCount();
            Thread[] threads = new Thread[numberOfThreads];
            processors.enumerate(threads);
            for (int i = 0; i < numberOfThreads; i++) {
                Thread thread = threads[i];
                if (thread != null) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            evolving = false;
        }
    }


    @Override
    public synchronized boolean isEvolving() {
        return evolving;
    }


    @Override
    public synchronized int getNumberOfGenerations() {
        return generationCounter.getCurrentValue();
    }


    @Override
    public synchronized int getNumberOfActiveThreads() {
        return processors.activeCount();
    }


    private final Pool pool;
    private boolean evolving;
    private final SharedCounter generationCounter;
    private ThreadGroup processors;


}
