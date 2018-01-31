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
public abstract class SpectrumWriter {
    
    protected final File file;
    protected final List<Spectrum> spectra;
    
    protected SpectrumWriter(File f, List<Spectrum> s){
        this.file=f;
        this.spectra=s;
        
    }
    
    public abstract void write();
    
    
}
