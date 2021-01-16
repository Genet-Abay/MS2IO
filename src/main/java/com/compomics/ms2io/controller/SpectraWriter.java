/**
 * 
 */
package com.compomics.ms2io.controller;

import com.compomics.ms2io.model.Spectrum;
import java.io.File;

import java.util.List;


/**
 *
 * @author Genet
 */
public abstract class SpectraWriter {
    
    /**
     * file to be write to
     */
    protected final File file;
    /**
     * list of spectra to be written on the file
     */
    protected List<Spectrum> spectra;
    
    /**
     *constructor initializing the object with file and list of spectrum
     * @param f file to be written to
     * @param s spectrum to be written on file
     */
    protected SpectraWriter(File f, List<Spectrum> s){
        this.file=f;
        this.spectra=s;
        
    }
    
    
      /**
     * constructor initializing with file
     * @param f file to be written to
     */
    protected SpectraWriter(File f){
        this.file=f;
        
    }
    
    /**
     * writes the spectra on the file
     */
    public abstract void write();
    
    /**
     * appends spectrum to the file
     * @param spec 
     * @param bw buffered writer 
     */
    
    public abstract void write(Spectrum spec);
    
    public abstract void closeWriter();
    

}
