/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Genet
 */
public abstract class SpectraReader {

    /**
     * spectra file to be read
     */
    protected final File spectraFile;

    /**
     * class constructor
     * @param specFile file to be read
     */
    protected SpectraReader(File specFile) {
        this.spectraFile = specFile;
    }

    /**
     * abstract class to read all spectrum
     * @return spectra
     */
    public abstract ArrayList<Spectrum> readAll();

    /**
     * Reads part of spectra based on given criteria: precursor mass and mass
     * error
     *
     * @param precMass precursor mass of spectra to be read
     * @param error mass error
     * @return array list of spectra
     */
    public abstract ArrayList<Spectrum> readPart(double precMass, double error);
    
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
       int low=0;
       int high=indKey.size()-1;
       int startPos=0;
       while(low<high){
           
           startPos=(low+high)/2;
           if(indKey.get(startPos).getPM() > lowerBound && high!=startPos){
               high=startPos;
               
           }
           
           else if(indKey.get(startPos).getPM() < lowerBound  && low!=startPos){
               low=startPos;
               
           }
            
            else{
                break;
            }
           
       }

       
       
       for (int a=startPos;a<indKey.size();a++) {
            double mass= indKey.get(a).getPM();
           
            if (mass <= upperBound) {
                pos.add(indKey.get(a).getPos());
                
            }
            //index file recoreded in pm increasing order, so we can stop checking out of bounds
            else{
                break;
            }
        }
       


        return pos;
    }

 


}
