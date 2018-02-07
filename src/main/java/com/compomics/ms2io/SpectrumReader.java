/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Genet
 */
public abstract class SpectrumReader {

    protected final File spectraFile;

    protected SpectrumReader(File specFile) {
        this.spectraFile = specFile;
    }

    public abstract ArrayList<Spectrum> readAll();

    /**
     * Reads part of spectra based on given criteria: precursor mass and mass
     * error
     *
     * @param precMass
     * @param error
     * @return
     */
    public abstract ArrayList<Spectrum> readPart(double precMass, double error);

    /**
     * Reads part of spectra based on criteria: spectrum title
     *
     * @param title
     * @return
     */
    public abstract ArrayList<Spectrum> readPart(String title);
    
    /**
     * Reads spectrum at the specified positions
     * 
     * 
     * @param position positions of the spectrum to be read
     * @return return spectrum at the specified position
     */
    public abstract Spectrum readAt(Long position);
        
   


    /**
     * positions of the spectra to be read from given list of index
     *
     * @param indKey list of the index of the spectrum which contains positions
     * of spectrum on the actual file, precursor mass and scan number
     * @param pm precursor mass to be used for spectrum selection
     * @param error mass error to include
     * @return list of selected spectra positions
     * @throws IOException
     */
    protected List<Long> positionsToberead(List<IndexKey> indKey, double pm, double error) throws IOException {

        List<Long> pos = new ArrayList<>();
        double lowerBound = pm - error;
        double upperBound = pm + error;

        for (IndexKey k : indKey) {
            double mass=k.getPM();
           
            if (lowerBound <= mass && mass <= upperBound) {
                pos.add(k.getPos());

                //index file recoreded in pm increasing order, so we can stop checking out of bounds
                if (mass > upperBound) {
                    break;
                }
            }
        }

        return pos;
    }

 

    /**
     * positions of the spectra to be read from given list of index
     *
     * @param indKey list of the index of the spectrum which contains positions
     * of spectrum on the actual file, precursor mass and scan number
     * @param title spectrum title to search for reading
     * @return list of selected spectra positions
     * @throws IOException
     */
    protected List<Long> positionsToberead(List<IndexKey> indKey, String title) throws IOException {

        List<Long> pos = new ArrayList<>();

        indKey.stream().filter((k) -> (k.getName().equals(title))).forEach((k) -> {
            pos.add(k.getPos());
        });

        return pos;
    }

}
