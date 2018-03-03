/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io;

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
    protected final List<Spectrum> spectra;
    
    /**
     * this method writes list of spectra on the file
     * @param f file to be written to
     * @param s spectrum to be written on file
     */
    protected SpectraWriter(File f, List<Spectrum> s){
        this.file=f;
        this.spectra=s;
        
    }
    
    /**
     * writes the spectra on the file
     */
    public abstract void write();
    
    
}
