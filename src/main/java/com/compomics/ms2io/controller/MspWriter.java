package com.compomics.ms2io.controller;

import com.compomics.ms2io.model.Spectrum;
import com.compomics.ms2io.model.Peak;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class MspWriter extends SpectraWriter {
    
    BufferedWriter bfr;

    /**
     * class constructor
     *
     * @param f file to be write to
     * @param s spectrum to be written to given file
     */
    public MspWriter(File f, List<Spectrum> s) throws IOException {
        super(f, s);
        bfr = new BufferedWriter(new FileWriter(this.file, true));
    }
    


    /**
     * class constructor
     *
     * @param f file to be write to
     */
    public MspWriter(File f) throws IOException {
        super(f);
       
        bfr=new BufferedWriter(new FileWriter(f.getAbsoluteFile(), true));
        
    }

    /**
     * writes msp files
     */
    @Override
    public void write() {    
        try {
            
            for (Spectrum spectrum : this.spectra) {

                ArrayList<Peak> peaks;
                //header part
                bfr.write("Name: " + spectrum.getTitle());
                bfr.newLine();
                bfr.write("MW: " + spectrum.getMW());
                bfr.newLine();
                bfr.write(spectrum.getComment());
                bfr.newLine();
                bfr.write("Num peaks: " + spectrum.getNumPeaks());
                bfr.newLine();

                //writing peaks
                peaks = spectrum.getPeakList();

                for (Peak peak : peaks) {
                   // bfr.write(Double.toString(peak.getMz()) + " " + Double.toString(peak.getIntensity()));
                    bfr.write(Double.toString(peak.getMz()) + "\t" + Double.toString(peak.getIntensity()) + "\t" + peak.getPeakAnnotation());
                    bfr.newLine();
                }
                bfr.newLine();

            }
           
        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           
        }
    }

    /**
     * appends msp files
     * @param spectrum
     */
    @Override
    public void write(Spectrum spectrum) {

        try {

            ArrayList<Peak> peaks;
            //header part
            bfr.write("Name: " + spectrum.getTitle());
            bfr.newLine();
            bfr.write("MW: " + spectrum.getMW());
            bfr.newLine();
            bfr.write(spectrum.getComment());
            bfr.newLine();
            bfr.write("Num peaks: " + spectrum.getNumPeaks());
            bfr.newLine();

            //writing peaks
            peaks = spectrum.getPeakList();

            for (Peak peak : peaks) {
                bfr.write(Double.toString(peak.getMz()) + "\t" + Double.toString(peak.getIntensity()) + "\t" + peak.getPeakAnnotation());
                bfr.newLine();
            }
            bfr.newLine();

        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    @Override
    public void closeWriter() {
        try {
            bfr.flush();
            bfr.close();
        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
