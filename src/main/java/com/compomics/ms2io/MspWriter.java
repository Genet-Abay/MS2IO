package com.compomics.ms2io;

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
public class MspWriter extends SpectrumWriter {

    public MspWriter(File f, List<Spectrum> s) {
        super(f, s);
    }

    @Override
    public void write() {
          
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.file));
            for (Spectrum spectrum : this.spectra) {
                
                ArrayList<Peak> peaks;
                //header part
                bw.write("Name: " + spectrum.getTitle());
                bw.newLine();
                bw.write("MW: " + spectrum.getMW());
                bw.newLine();
                bw.write("Comment: Single " + "Parent=" + spectrum.getPCMass() + " Scan=" + spectrum.getScanNumber());
                bw.newLine();
                bw.write("Num Peaks: " + spectrum.getNumPeaks());
                bw.newLine();
                
                //writing peaks
                peaks = spectrum.getPeakList();
                
                for (Peak peak : peaks) {
                    bw.write(Double.toString(peak.getMz()) + " " + Double.toString(peak.getIntensity()));
                    bw.newLine();
                }
                bw.newLine();
               
                
            }   bw.close();
        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
