/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.gui;

import craterdog.primitives.Probability;
import java.io.*;
import java.awt.*;
import javax.swing.*;

import psoup.*;
import psoup.engine.*;
import psoup.pool.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.*;
import org.jfree.data.time.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


public final class Console extends javax.swing.JFrame {

    static XLogger logger = XLoggerFactory.getXLogger(Console.class);

    /** Creates new form Console */
    public Console() {
        initPool();
        initEngine();
        initGraph();
        initSnapshot();
        initComponents();
        integrateGraph();
    }


    private void initPool() {
        pool = new GenePool();
        pool.initialize(0, new Probability(0.5), 10, new Probability(1.0));
    }


    private void initEngine() {
        engine = new EvolutionEngine(pool);

    }


    private void initGraph() {

        // initialize the time axis
        DateAxis timeDomain = new DateAxis("Time");
        timeDomain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        timeDomain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        timeDomain.setLowerMargin(0.0);
        timeDomain.setUpperMargin(0.0);

        // initialize the creature axis
        NumberAxis creatureRange = new NumberAxis("Number of Creatures");
        creatureRange.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        creatureRange.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        creatureRange.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        creatureRange.setLowerMargin(0.05);
        creatureRange.setUpperMargin(0.05);

        // initialize the species axis
        NumberAxis speciesRange = new NumberAxis("Number of Species");
        speciesRange.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        speciesRange.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        speciesRange.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        speciesRange.setLowerMargin(0.2);
        speciesRange.setUpperMargin(0.2);

        // initialize the temperature axis
        NumberAxis temperatureRange = new NumberAxis("Temperature");
        temperatureRange.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        temperatureRange.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        temperatureRange.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        temperatureRange.setRange(new Range(0.0, 100.0));

        // initialize the creature data stream
        creatureData = new TimeSeries("Number of Creatures");
        creatureData.setMaximumItemCount(5 * 60 * 24); // discard data over 5 days old
        TimeSeriesCollection creatureSeries = new TimeSeriesCollection();
        creatureSeries.addSeries(creatureData);
        XYItemRenderer creatureRenderer = new StandardXYItemRenderer();
        creatureRenderer.setSeriesPaint(0, Color.cyan);

        // initialize the species data stream
        speciesData = new TimeSeries("Number of Species");
        speciesData.setMaximumItemCount(5 * 60 * 24);  // discard data over 5 days old
        TimeSeriesCollection speciesSeries = new TimeSeriesCollection();
        speciesSeries.addSeries(speciesData);
        XYItemRenderer speciesRenderer = new StandardXYItemRenderer();
        speciesRenderer.setSeriesPaint(0, Color.green);

        // initialize the temperature data stream
        temperatureData = new TimeSeries("Temperature");
        temperatureData.setMaximumItemCount(5 * 60 * 24);  // discard data over 5 days old
        TimeSeriesCollection temperatureSeries = new TimeSeriesCollection();
        temperatureSeries.addSeries(temperatureData);
        XYItemRenderer temperatureRenderer = new StandardXYItemRenderer();
        temperatureRenderer.setSeriesPaint(0, Color.magenta);

        // initialize the plot
        XYPlot plot = new XYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainAxis(timeDomain);
        plot.setDataset(0, creatureSeries);
        plot.setRangeAxis(0, creatureRange);
        plot.mapDatasetToRangeAxis(0, 0);
        plot.setRenderer(0, creatureRenderer);
        plot.setDataset(1, speciesSeries);
        plot.setRangeAxis(1, speciesRange);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, speciesRenderer);
        plot.setDataset(2, temperatureSeries);
        plot.setRangeAxis(2, temperatureRange);
        plot.mapDatasetToRangeAxis(2, 2);
        plot.setRenderer(2, temperatureRenderer);

        // initialize the chart
        JFreeChart chart = new JFreeChart(
            "Pool Statistics",
            new Font("SansSerif", Font.BOLD, 24),
            plot,
            true
        );
        chart.setBackgroundPaint(Color.lightGray);

        // initialize the chart panel (JPanel)
        chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 4, 4, 4),
            BorderFactory.createLineBorder(Color.black))
        );
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(new Font("SansSerif", Font.PLAIN, 12));
    }


    private void initSnapshot() {
        snapshotDirectory = new File("./");
    }


    private void integrateGraph() {
        graphTab.add(chartPanel, java.awt.BorderLayout.CENTER);
    }


    private void startCollectingStatistics() {
        statisticsThread = new Thread(() -> {
            int iterations = 0;
            try {
                while (!Thread.interrupted()) {
                    int numberOfActiveThreads = engine.getNumberOfActiveThreads();
                    int numberOfGenerations = engine.getNumberOfGenerations();
                    double temperature = pool.getTemperature().toDouble() * 100;
                    numberOfThreadsField.setText(Integer.toString(numberOfActiveThreads));
                    numberOfGenerationsField.setText(Integer.toString(numberOfGenerations));
                    if (iterations > 0) {
                        generationsPerSecondField.setText(Integer.toString(numberOfGenerations / (iterations * 5 /*seconds/iteration*/ )));
                    } else {
                        generationsPerSecondField.setText("0");
                    }
                    currentNumberOfGenesField.setText(Integer.toString(pool.getCurrentNumberOfGenes()));
                    currentNumberOfSpeciesField.setText(Integer.toString(pool.getCurrentNumberOfSpecies()));
                    currentNumberOfCreaturesField.setText(Integer.toString(pool.getCurrentNumberOfCreatures()));
                    lowNumberOfGenesField.setText(Integer.toString(pool.getLowestNumberOfGenes()));
                    lowNumberOfSpeciesField.setText(Integer.toString(pool.getLowestNumberOfSpecies()));
                    lowNumberOfCreaturesField.setText(Integer.toString(pool.getLowestNumberOfCreatures()));
                    highNumberOfGenesField.setText(Integer.toString(pool.getHighestNumberOfGenes()));
                    highNumberOfSpeciesField.setText(Integer.toString(pool.getHighestNumberOfSpecies()));
                    highNumberOfCreaturesField.setText(Integer.toString(pool.getHighestNumberOfCreatures()));
                    currentTemperatureSlider.setValue((int) Math.round(temperature));
                    creatureData.addOrUpdate(new Minute(), pool.getCurrentNumberOfCreatures());
                    speciesData.addOrUpdate(new Minute(), pool.getCurrentNumberOfSpecies());
                    temperatureData.addOrUpdate(new Minute(), temperature);
                    iterations++;
                    Thread.sleep(5 * SECONDS);
                }
            } catch (InterruptedException e) {
                // do nothing, just wait to exit
            } catch (Exception e) {
                logger.error("Console update thread caught the following exception: {}", e);
            }
        });
        statisticsThread.start();
    }


    private void stopCollectingStatistics() {
        statisticsThread.interrupt();
        try {
            statisticsThread.join();
        } catch (InterruptedException e) {
            // do nothing
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPanel = new javax.swing.JTabbedPane();
        initializationTab = new javax.swing.JPanel();
        initializationValuesPanel = new javax.swing.JPanel();
        initialNumberOfCreaturesPanel = new javax.swing.JPanel();
        initialNumberOfCreaturesLabel = new javax.swing.JLabel();
        initialNumberOfCreaturesField = new javax.swing.JTextField();
        initialTemperaturePanel = new javax.swing.JPanel();
        initialTemperatureLabel = new javax.swing.JLabel();
        initialTemperatureField = new javax.swing.JTextField();
        relativeComplexityPanel = new javax.swing.JPanel();
        relativeComplexityLabel = new javax.swing.JLabel();
        relativeComplexityField = new javax.swing.JTextField();
        maximumDepthPanel = new javax.swing.JPanel();
        maximumDepthLabel = new javax.swing.JLabel();
        maximumDepthField = new javax.swing.JTextField();
        initialNumberOfSpeciesPanel = new javax.swing.JPanel();
        initialNumberOfSpeciesLabel = new javax.swing.JLabel();
        initialNumberOfSpeciesField = new javax.swing.JTextField();
        initialNumberOfGenesPanel = new javax.swing.JPanel();
        initialNumberOfGenesLabel = new javax.swing.JLabel();
        initialNumberOfGenesField = new javax.swing.JTextField();
        initialAverageSizePanel = new javax.swing.JPanel();
        initialAverageSizeLabel = new javax.swing.JLabel();
        initialAverageSizeField = new javax.swing.JTextField();
        initialMaximumSizePanel = new javax.swing.JPanel();
        initialMaximumSizeLabel = new javax.swing.JLabel();
        initialMaximumSizeField = new javax.swing.JTextField();
        initializationButtonPanel = new javax.swing.JPanel();
        initializeButton = new javax.swing.JButton();
        executionTab = new javax.swing.JPanel();
        executionDataPanel = new javax.swing.JPanel();
        numberOfThreadsPanel = new javax.swing.JPanel();
        numberOfThreadsLabel = new javax.swing.JLabel();
        numberOfThreadsField = new javax.swing.JTextField();
        numberOfGenerationsPanel = new javax.swing.JPanel();
        numberOfGenerationsLabel = new javax.swing.JLabel();
        numberOfGenerationsField = new javax.swing.JTextField();
        generationsPerSecondPanel = new javax.swing.JPanel();
        generationsPerSecondLabel = new javax.swing.JLabel();
        generationsPerSecondField = new javax.swing.JTextField();
        executionButtonPanel = new javax.swing.JPanel();
        evolutionButton = new javax.swing.JButton();
        statisticsTab = new javax.swing.JPanel();
        statisticsDataPanel = new javax.swing.JPanel();
        rowHeadingPanel = new javax.swing.JPanel();
        rowHeadingLabel = new javax.swing.JLabel();
        lowMarkTitlePanel = new javax.swing.JPanel();
        lowMarkTitleLabel = new javax.swing.JLabel();
        currentTitlePanel = new javax.swing.JPanel();
        currentTitleLabel = new javax.swing.JLabel();
        highMarkTitlePanel = new javax.swing.JPanel();
        highMarkTitleLabel = new javax.swing.JLabel();
        numberOfSpeciesHeadingPanel = new javax.swing.JPanel();
        numberOfSpeciesHeadingLabel = new javax.swing.JLabel();
        lowNumberOfSpeciesPanel = new javax.swing.JPanel();
        lowNumberOfSpeciesField = new javax.swing.JTextField();
        currentNumberOfSpeciesPanel = new javax.swing.JPanel();
        currentNumberOfSpeciesField = new javax.swing.JTextField();
        highNumberOfSpeciesPanel = new javax.swing.JPanel();
        highNumberOfSpeciesField = new javax.swing.JTextField();
        numberOfCreaturesHeadingPanel = new javax.swing.JPanel();
        numberOfCreaturesHeadingLabel = new javax.swing.JLabel();
        lowNumberOfCreaturesPanel = new javax.swing.JPanel();
        lowNumberOfCreaturesField = new javax.swing.JTextField();
        currentNumberOfCreaturesPanel = new javax.swing.JPanel();
        currentNumberOfCreaturesField = new javax.swing.JTextField();
        highNumberOfCreaturesPanel = new javax.swing.JPanel();
        highNumberOfCreaturesField = new javax.swing.JTextField();
        numberOfGenesHeadingPanel = new javax.swing.JPanel();
        numberOfGenesHeadingLabel = new javax.swing.JLabel();
        lowNumberOfGenesPanel = new javax.swing.JPanel();
        lowNumberOfGenesField = new javax.swing.JTextField();
        currentNumberOfGenesPanel = new javax.swing.JPanel();
        currentNumberOfGenesField = new javax.swing.JTextField();
        highNumberOfGenesPanel = new javax.swing.JPanel();
        highNumberOfGenesField = new javax.swing.JTextField();
        temperaturePanel = new javax.swing.JPanel();
        currentTemperatureLabel = new javax.swing.JLabel();
        currentTemperatureSlider = new javax.swing.JSlider();
        statisticsButtonPanel = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        graphTab = new javax.swing.JPanel();
        snapshotTab = new javax.swing.JPanel();
        filenamePanel = new javax.swing.JPanel();
        prefixLabel = new javax.swing.JLabel();
        prefixField = new javax.swing.JTextField();
        suffixCheckbox = new javax.swing.JCheckBox();
        snapshotButtonPanel = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        storeButton = new javax.swing.JButton();
        directoryPanel = new javax.swing.JPanel();
        directoryLabel = new javax.swing.JLabel();
        directoryField = new javax.swing.JTextField();
        chooserButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setTitle("PSoup Evolution Engine");
        setName("mainWindow"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        initializationTab.setLayout(new java.awt.BorderLayout());

        initializationValuesPanel.setLayout(new java.awt.GridLayout(5, 2));

        initialNumberOfCreaturesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialNumberOfCreaturesLabel.setText("Initial Number of Creatures:");
        initialNumberOfCreaturesPanel.add(initialNumberOfCreaturesLabel);

        initialNumberOfCreaturesField.setColumns(6);
        initialNumberOfCreaturesField.setText("100000");
        initialNumberOfCreaturesPanel.add(initialNumberOfCreaturesField);

        initializationValuesPanel.add(initialNumberOfCreaturesPanel);

        initialTemperaturePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialTemperatureLabel.setText("Initial Temperature (0 - 100):");
        initialTemperaturePanel.add(initialTemperatureLabel);

        initialTemperatureField.setColumns(3);
        initialTemperatureField.setText("50");
        initialTemperaturePanel.add(initialTemperatureField);

        initializationValuesPanel.add(initialTemperaturePanel);

        relativeComplexityPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        relativeComplexityLabel.setText("Relative Complexity (%):");
        relativeComplexityPanel.add(relativeComplexityLabel);

        relativeComplexityField.setColumns(3);
        relativeComplexityField.setText("50");
        relativeComplexityPanel.add(relativeComplexityField);

        initializationValuesPanel.add(relativeComplexityPanel);

        maximumDepthPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        maximumDepthLabel.setText("Maximum Depth:");
        maximumDepthPanel.add(maximumDepthLabel);

        maximumDepthField.setColumns(3);
        maximumDepthField.setText("5");
        maximumDepthPanel.add(maximumDepthField);

        initializationValuesPanel.add(maximumDepthPanel);

        initialNumberOfSpeciesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialNumberOfSpeciesLabel.setText("Initial Number of Species:");
        initialNumberOfSpeciesPanel.add(initialNumberOfSpeciesLabel);

        initialNumberOfSpeciesField.setColumns(5);
        initialNumberOfSpeciesField.setEditable(false);
        initialNumberOfSpeciesField.setText("0");
        initialNumberOfSpeciesPanel.add(initialNumberOfSpeciesField);

        initializationValuesPanel.add(initialNumberOfSpeciesPanel);

        initialNumberOfGenesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialNumberOfGenesLabel.setText("Initial Number of Genes:");
        initialNumberOfGenesPanel.add(initialNumberOfGenesLabel);

        initialNumberOfGenesField.setColumns(8);
        initialNumberOfGenesField.setEditable(false);
        initialNumberOfGenesField.setText("0");
        initialNumberOfGenesPanel.add(initialNumberOfGenesField);

        initializationValuesPanel.add(initialNumberOfGenesPanel);

        initialAverageSizePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialAverageSizeLabel.setText("Initial Average Size:");
        initialAverageSizePanel.add(initialAverageSizeLabel);

        initialAverageSizeField.setColumns(5);
        initialAverageSizeField.setEditable(false);
        initialAverageSizeField.setText("0");
        initialAverageSizePanel.add(initialAverageSizeField);

        initializationValuesPanel.add(initialAverageSizePanel);

        initialMaximumSizePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initialMaximumSizeLabel.setText("Initial Maximum Size:");
        initialMaximumSizePanel.add(initialMaximumSizeLabel);

        initialMaximumSizeField.setColumns(5);
        initialMaximumSizeField.setEditable(false);
        initialMaximumSizeField.setText("0");
        initialMaximumSizePanel.add(initialMaximumSizeField);

        initializationValuesPanel.add(initialMaximumSizePanel);

        initializationTab.add(initializationValuesPanel, java.awt.BorderLayout.CENTER);

        initializeButton.setText("Initialize");
        initializeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initializeButtonActionPerformed(evt);
            }
        });
        initializationButtonPanel.add(initializeButton);

        initializationTab.add(initializationButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPanel.addTab("Initialization", initializationTab);

        executionTab.setEnabled(false);
        executionTab.setLayout(new java.awt.BorderLayout());

        executionDataPanel.setLayout(new java.awt.GridLayout(6, 2));

        numberOfThreadsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        numberOfThreadsLabel.setText("Number of Processing Threads:");
        numberOfThreadsPanel.add(numberOfThreadsLabel);

        numberOfThreadsField.setColumns(3);
        numberOfThreadsField.setEditable(false);
        numberOfThreadsField.setText("0");
        numberOfThreadsPanel.add(numberOfThreadsField);

        executionDataPanel.add(numberOfThreadsPanel);

        numberOfGenerationsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        numberOfGenerationsLabel.setText("Number of Generations:");
        numberOfGenerationsPanel.add(numberOfGenerationsLabel);

        numberOfGenerationsField.setColumns(8);
        numberOfGenerationsField.setEditable(false);
        numberOfGenerationsField.setText("0");
        numberOfGenerationsPanel.add(numberOfGenerationsField);

        executionDataPanel.add(numberOfGenerationsPanel);

        generationsPerSecondPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        generationsPerSecondLabel.setText("Generations per Second:");
        generationsPerSecondPanel.add(generationsPerSecondLabel);

        generationsPerSecondField.setColumns(8);
        generationsPerSecondField.setEditable(false);
        generationsPerSecondField.setText("0");
        generationsPerSecondPanel.add(generationsPerSecondField);

        executionDataPanel.add(generationsPerSecondPanel);

        executionTab.add(executionDataPanel, java.awt.BorderLayout.CENTER);

        evolutionButton.setText("Start Evolving");
        evolutionButton.setEnabled(false);
        evolutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evolutionButtonActionPerformed(evt);
            }
        });
        executionButtonPanel.add(evolutionButton);

        executionTab.add(executionButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPanel.addTab("Execution", executionTab);

        statisticsTab.setLayout(new java.awt.BorderLayout());

        statisticsDataPanel.setLayout(new java.awt.GridLayout(5, 4));

        rowHeadingPanel.add(rowHeadingLabel);

        statisticsDataPanel.add(rowHeadingPanel);

        lowMarkTitleLabel.setText("Low");
        lowMarkTitleLabel.setFocusable(false);
        lowMarkTitlePanel.add(lowMarkTitleLabel);

        statisticsDataPanel.add(lowMarkTitlePanel);

        currentTitleLabel.setText("Current");
        currentTitlePanel.add(currentTitleLabel);

        statisticsDataPanel.add(currentTitlePanel);

        highMarkTitleLabel.setText("High");
        highMarkTitlePanel.add(highMarkTitleLabel);

        statisticsDataPanel.add(highMarkTitlePanel);

        numberOfSpeciesHeadingPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        numberOfSpeciesHeadingLabel.setText("Species");
        numberOfSpeciesHeadingPanel.add(numberOfSpeciesHeadingLabel);

        statisticsDataPanel.add(numberOfSpeciesHeadingPanel);

        lowNumberOfSpeciesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lowNumberOfSpeciesField.setColumns(8);
        lowNumberOfSpeciesField.setEditable(false);
        lowNumberOfSpeciesField.setText("0");
        lowNumberOfSpeciesPanel.add(lowNumberOfSpeciesField);

        statisticsDataPanel.add(lowNumberOfSpeciesPanel);

        currentNumberOfSpeciesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        currentNumberOfSpeciesField.setColumns(8);
        currentNumberOfSpeciesField.setEditable(false);
        currentNumberOfSpeciesField.setText("0");
        currentNumberOfSpeciesPanel.add(currentNumberOfSpeciesField);

        statisticsDataPanel.add(currentNumberOfSpeciesPanel);

        highNumberOfSpeciesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        highNumberOfSpeciesField.setColumns(8);
        highNumberOfSpeciesField.setEditable(false);
        highNumberOfSpeciesField.setText("0");
        highNumberOfSpeciesPanel.add(highNumberOfSpeciesField);

        statisticsDataPanel.add(highNumberOfSpeciesPanel);

        numberOfCreaturesHeadingPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        numberOfCreaturesHeadingLabel.setText("Creatures");
        numberOfCreaturesHeadingPanel.add(numberOfCreaturesHeadingLabel);

        statisticsDataPanel.add(numberOfCreaturesHeadingPanel);

        lowNumberOfCreaturesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lowNumberOfCreaturesField.setColumns(8);
        lowNumberOfCreaturesField.setEditable(false);
        lowNumberOfCreaturesField.setText("0");
        lowNumberOfCreaturesPanel.add(lowNumberOfCreaturesField);

        statisticsDataPanel.add(lowNumberOfCreaturesPanel);

        currentNumberOfCreaturesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        currentNumberOfCreaturesField.setColumns(8);
        currentNumberOfCreaturesField.setEditable(false);
        currentNumberOfCreaturesField.setText("0");
        currentNumberOfCreaturesPanel.add(currentNumberOfCreaturesField);

        statisticsDataPanel.add(currentNumberOfCreaturesPanel);

        highNumberOfCreaturesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        highNumberOfCreaturesField.setColumns(8);
        highNumberOfCreaturesField.setEditable(false);
        highNumberOfCreaturesField.setText("0");
        highNumberOfCreaturesPanel.add(highNumberOfCreaturesField);

        statisticsDataPanel.add(highNumberOfCreaturesPanel);

        numberOfGenesHeadingPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        numberOfGenesHeadingLabel.setText("Genes");
        numberOfGenesHeadingPanel.add(numberOfGenesHeadingLabel);

        statisticsDataPanel.add(numberOfGenesHeadingPanel);

        lowNumberOfGenesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lowNumberOfGenesField.setColumns(8);
        lowNumberOfGenesField.setEditable(false);
        lowNumberOfGenesField.setText("0");
        lowNumberOfGenesPanel.add(lowNumberOfGenesField);

        statisticsDataPanel.add(lowNumberOfGenesPanel);

        currentNumberOfGenesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        currentNumberOfGenesField.setColumns(8);
        currentNumberOfGenesField.setEditable(false);
        currentNumberOfGenesField.setText("0");
        currentNumberOfGenesPanel.add(currentNumberOfGenesField);

        statisticsDataPanel.add(currentNumberOfGenesPanel);

        highNumberOfGenesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        highNumberOfGenesField.setColumns(8);
        highNumberOfGenesField.setEditable(false);
        highNumberOfGenesField.setText("0");
        highNumberOfGenesPanel.add(highNumberOfGenesField);

        statisticsDataPanel.add(highNumberOfGenesPanel);

        statisticsTab.add(statisticsDataPanel, java.awt.BorderLayout.CENTER);

        temperaturePanel.setLayout(new java.awt.BorderLayout());

        currentTemperatureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTemperatureLabel.setText("Temperature");
        currentTemperatureLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        temperaturePanel.add(currentTemperatureLabel, java.awt.BorderLayout.SOUTH);

        currentTemperatureSlider.setMajorTickSpacing(10);
        currentTemperatureSlider.setMinorTickSpacing(5);
        currentTemperatureSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        currentTemperatureSlider.setPaintLabels(true);
        currentTemperatureSlider.setPaintTicks(true);
        currentTemperatureSlider.setSnapToTicks(true);
        currentTemperatureSlider.setValue(0);
        currentTemperatureSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                temperatureStateChangePerformed(evt);
            }
        });
        temperaturePanel.add(currentTemperatureSlider, java.awt.BorderLayout.CENTER);

        statisticsTab.add(temperaturePanel, java.awt.BorderLayout.EAST);

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        statisticsButtonPanel.add(resetButton);

        statisticsTab.add(statisticsButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPanel.addTab("Statistics", statisticsTab);

        graphTab.setLayout(new java.awt.BorderLayout());
        tabbedPanel.addTab("Graph", graphTab);

        snapshotTab.setLayout(new java.awt.BorderLayout());

        prefixLabel.setText("Filename Prefix:");
        filenamePanel.add(prefixLabel);

        prefixField.setColumns(18);
        prefixField.setText("snapshot");
        filenamePanel.add(prefixField);

        suffixCheckbox.setSelected(true);
        suffixCheckbox.setText("Auto Suffix");
        filenamePanel.add(suffixCheckbox);

        snapshotTab.add(filenamePanel, java.awt.BorderLayout.CENTER);

        loadButton.setText("Load");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        snapshotButtonPanel.add(loadButton);

        storeButton.setText("Store");
        storeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeButtonActionPerformed(evt);
            }
        });
        snapshotButtonPanel.add(storeButton);

        snapshotTab.add(snapshotButtonPanel, java.awt.BorderLayout.SOUTH);

        directoryLabel.setText("Directory:");
        directoryPanel.add(directoryLabel);

        directoryField.setColumns(22);
        directoryField.setText("./snapshots/");
        directoryPanel.add(directoryField);

        chooserButton.setText("Choose");
        chooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooserButtonActionPerformed(evt);
            }
        });
        directoryPanel.add(chooserButton);

        snapshotTab.add(directoryPanel, java.awt.BorderLayout.NORTH);

        tabbedPanel.addTab("Snap Shots", snapshotTab);

        getContentPane().add(tabbedPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help");

        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void temperatureStateChangePerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_temperatureStateChangePerformed
        pool.setTemperature(new Probability(currentTemperatureSlider.getValue() / 100.0d));
    }//GEN-LAST:event_temperatureStateChangePerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        pool.resetWaterMarks();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void evolutionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evolutionButtonActionPerformed
        if (engine.isEvolving()) {
            engine.stopEvolving();
            stopCollectingStatistics();
            evolutionButton.setText("Start Evolving");
            initialNumberOfCreaturesField.setEditable(true);
            relativeComplexityField.setEditable(true);
            maximumDepthField.setEditable(true);
            initialTemperatureField.setEditable(true);
            initializeButton.setEnabled(true);
            numberOfThreadsField.setEditable(true);
            numberOfThreadsField.setText("10");
        } else {
            try {
                numberOfThreadsField.setEditable(false);
                int numberOfThreads = Integer.parseInt(numberOfThreadsField.getText());
                initialNumberOfCreaturesField.setEditable(false);
                relativeComplexityField.setEditable(false);
                maximumDepthField.setEditable(false);
                initialTemperatureField.setEditable(false);
                initializeButton.setEnabled(false);
                evolutionButton.setText("Stop Evolving");
                engine.startEvolving(numberOfThreads);
                startCollectingStatistics();
            } catch (NumberFormatException e) {
                logger.error("Bad initial number of threads entered.");
            }
        }
    }//GEN-LAST:event_evolutionButtonActionPerformed

    private void initializeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initializeButtonActionPerformed
        if (!engine.isEvolving()) {
            try {
                int numberOfCreatures = Integer.parseInt(initialNumberOfCreaturesField.getText());
                Probability relativeComplexity = new Probability(Float.parseFloat(relativeComplexityField.getText()) / 100.0);
                int maximumDepth = Integer.parseInt(maximumDepthField.getText());
                Probability temperature = new Probability(Double.parseDouble(initialTemperatureField.getText()) / 100.0);
                pool.initialize(numberOfCreatures, relativeComplexity, maximumDepth, temperature);
                int numberOfSpecies = pool.getCurrentNumberOfSpecies();
                int numberOfGenes = pool.getCurrentNumberOfGenes();
                double averageSize = (double) numberOfGenes / (double) numberOfCreatures;
                initialNumberOfSpeciesField.setText(Integer.toString(numberOfSpecies));
                initialNumberOfGenesField.setText(Integer.toString(numberOfGenes));
                initialAverageSizeField.setText(Long.toString(Math.round(averageSize)));
                numberOfThreadsField.setEditable(true);
                numberOfThreadsField.setText("10");
                creatureData.clear();
                speciesData.clear();
                temperatureData.clear();
                evolutionButton.setEnabled(true);
            } catch (NumberFormatException e) {
                logger.error("Bad initialization parameters entered.");
            }
        }
    }//GEN-LAST:event_initializeButtonActionPerformed

    private void chooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooserButtonActionPerformed
        JFileChooser chooser = new JFileChooser(snapshotDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            snapshotDirectory = chooser.getSelectedFile();
            directoryField.setText(snapshotDirectory.toString());
        }
    }//GEN-LAST:event_chooserButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_loadButtonActionPerformed

    private void storeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_storeButtonActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Console().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton chooserButton;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JTextField currentNumberOfCreaturesField;
    private javax.swing.JPanel currentNumberOfCreaturesPanel;
    private javax.swing.JTextField currentNumberOfGenesField;
    private javax.swing.JPanel currentNumberOfGenesPanel;
    private javax.swing.JTextField currentNumberOfSpeciesField;
    private javax.swing.JPanel currentNumberOfSpeciesPanel;
    private javax.swing.JLabel currentTemperatureLabel;
    private javax.swing.JSlider currentTemperatureSlider;
    private javax.swing.JLabel currentTitleLabel;
    private javax.swing.JPanel currentTitlePanel;
    private javax.swing.JTextField directoryField;
    private javax.swing.JLabel directoryLabel;
    private javax.swing.JPanel directoryPanel;
    private javax.swing.JButton evolutionButton;
    private javax.swing.JPanel executionButtonPanel;
    private javax.swing.JPanel executionDataPanel;
    private javax.swing.JPanel executionTab;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel filenamePanel;
    private javax.swing.JTextField generationsPerSecondField;
    private javax.swing.JLabel generationsPerSecondLabel;
    private javax.swing.JPanel generationsPerSecondPanel;
    private javax.swing.JPanel graphTab;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel highMarkTitleLabel;
    private javax.swing.JPanel highMarkTitlePanel;
    private javax.swing.JTextField highNumberOfCreaturesField;
    private javax.swing.JPanel highNumberOfCreaturesPanel;
    private javax.swing.JTextField highNumberOfGenesField;
    private javax.swing.JPanel highNumberOfGenesPanel;
    private javax.swing.JTextField highNumberOfSpeciesField;
    private javax.swing.JPanel highNumberOfSpeciesPanel;
    private javax.swing.JTextField initialAverageSizeField;
    private javax.swing.JLabel initialAverageSizeLabel;
    private javax.swing.JPanel initialAverageSizePanel;
    private javax.swing.JTextField initialMaximumSizeField;
    private javax.swing.JLabel initialMaximumSizeLabel;
    private javax.swing.JPanel initialMaximumSizePanel;
    private javax.swing.JTextField initialNumberOfCreaturesField;
    private javax.swing.JLabel initialNumberOfCreaturesLabel;
    private javax.swing.JPanel initialNumberOfCreaturesPanel;
    private javax.swing.JTextField initialNumberOfGenesField;
    private javax.swing.JLabel initialNumberOfGenesLabel;
    private javax.swing.JPanel initialNumberOfGenesPanel;
    private javax.swing.JTextField initialNumberOfSpeciesField;
    private javax.swing.JLabel initialNumberOfSpeciesLabel;
    private javax.swing.JPanel initialNumberOfSpeciesPanel;
    private javax.swing.JTextField initialTemperatureField;
    private javax.swing.JLabel initialTemperatureLabel;
    private javax.swing.JPanel initialTemperaturePanel;
    private javax.swing.JPanel initializationButtonPanel;
    private javax.swing.JPanel initializationTab;
    private javax.swing.JPanel initializationValuesPanel;
    private javax.swing.JButton initializeButton;
    private javax.swing.JButton loadButton;
    private javax.swing.JLabel lowMarkTitleLabel;
    private javax.swing.JPanel lowMarkTitlePanel;
    private javax.swing.JTextField lowNumberOfCreaturesField;
    private javax.swing.JPanel lowNumberOfCreaturesPanel;
    private javax.swing.JTextField lowNumberOfGenesField;
    private javax.swing.JPanel lowNumberOfGenesPanel;
    private javax.swing.JTextField lowNumberOfSpeciesField;
    private javax.swing.JPanel lowNumberOfSpeciesPanel;
    private javax.swing.JTextField maximumDepthField;
    private javax.swing.JLabel maximumDepthLabel;
    private javax.swing.JPanel maximumDepthPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel numberOfCreaturesHeadingLabel;
    private javax.swing.JPanel numberOfCreaturesHeadingPanel;
    private javax.swing.JTextField numberOfGenerationsField;
    private javax.swing.JLabel numberOfGenerationsLabel;
    private javax.swing.JPanel numberOfGenerationsPanel;
    private javax.swing.JLabel numberOfGenesHeadingLabel;
    private javax.swing.JPanel numberOfGenesHeadingPanel;
    private javax.swing.JLabel numberOfSpeciesHeadingLabel;
    private javax.swing.JPanel numberOfSpeciesHeadingPanel;
    private javax.swing.JTextField numberOfThreadsField;
    private javax.swing.JLabel numberOfThreadsLabel;
    private javax.swing.JPanel numberOfThreadsPanel;
    private javax.swing.JTextField prefixField;
    private javax.swing.JLabel prefixLabel;
    private javax.swing.JTextField relativeComplexityField;
    private javax.swing.JLabel relativeComplexityLabel;
    private javax.swing.JPanel relativeComplexityPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel rowHeadingLabel;
    private javax.swing.JPanel rowHeadingPanel;
    private javax.swing.JPanel snapshotButtonPanel;
    private javax.swing.JPanel snapshotTab;
    private javax.swing.JPanel statisticsButtonPanel;
    private javax.swing.JPanel statisticsDataPanel;
    private javax.swing.JPanel statisticsTab;
    private javax.swing.JButton storeButton;
    private javax.swing.JCheckBox suffixCheckbox;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JPanel temperaturePanel;
    // End of variables declaration//GEN-END:variables

    private Pool pool;
    private Evolver engine;
    private TimeSeries creatureData;
    private TimeSeries speciesData;
    private TimeSeries temperatureData;
    private ChartPanel chartPanel;
    private Thread statisticsThread;
    private File snapshotDirectory;

    static private final int SECONDS = 1000;

}
