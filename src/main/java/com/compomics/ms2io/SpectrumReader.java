/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Genet
 */
public abstract class SpectrumReader {

    protected final File spectraFile;

    protected SpectrumReader(File specFile) {
        this.spectraFile = specFile;
    }

    public abstract List<Spectrum> Read();

    /**
     * positions of the spectra to be read
     *
     * @param indxfile file contains the index of spectra which contains
     * positions of spectrum on the actual file precursor mass and scan number
     * @param pm precursor mass to be used for spectrum selection
     * @param error mass error to include
     * @return list of selected spectra positions
     * @throws IOException
     */
    protected List<Long> positionsToberead(File indxfile, double pm, double error) throws IOException {

        List<Long> pos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(indxfile));
        double lowerBound = pm - error;
        double upperBound = pm + error;

        String line = br.readLine();
        while (line != null) {
            String[] temp = line.split(";");
            int tempLen = temp.length;
            if (tempLen == 4) {
                double pmass = Double.parseDouble(temp[3]);
                if (lowerBound <= pmass && pmass <= upperBound) {
                    Long p = Long.parseLong(temp[0]);
                    pos.add(p);
                }
                //index file recoreded in pm increasing order, so we can stop checking out of bounds
                if (pm > upperBound) {
                    break;
                }
            } else {
                //report error on iindex file
            }

            line = br.readLine();

        }

        return pos;
    }

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
            if (lowerBound <= k.getPM()&& k.getPM() <= upperBound) {
                pos.add(k.getPos());

                //index file recoreded in pm increasing order, so we can stop checking out of bounds
                if (k.getPM() > upperBound) {
                    break;
                }
            } 
        }

        return pos;
    }

}
