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
public class MgfWriter extends SpectraWriter {

    BufferedWriter bfr;

    /**
     * class constructor
     *
     * @param f file to be write to
     * @param s spectrum to be write on the file
     */
    public MgfWriter(File f, List<Spectrum> s) {
        super(f, s);
        try {
            bfr = new BufferedWriter(new FileWriter(f.getAbsoluteFile(), true));
        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * class constructor
     *
     * @param f file to be write to
     */
    public MgfWriter(File f) {
        super(f);
        try {
            bfr = new BufferedWriter(new FileWriter(f.getAbsoluteFile(), true));
        } catch (IOException ex) {
            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * writes given list of spectrum to the file
     */
    @Override
    public void write() {
        try {
            for (Spectrum spectrum : this.spectra) {

                ArrayList<Peak> peaks;
                //header part
                bfr.write("BEGIN IONS");
                bfr.newLine();
                bfr.write("TITLE=" + spectrum.getTitle());
                bfr.newLine();
                bfr.write("PROTEIN=" + spectrum.getProtein());
                bfr.newLine();
                bfr.write("SEQUENCE=" + spectrum.getSequence());
                bfr.newLine();
                bfr.write("MODS=" + spectrum.getModifications_asStr());
                bfr.newLine();
                bfr.write("SCAN=" + spectrum.getScanNumber());
                bfr.newLine();
                bfr.write("RTINSECONDS=" + spectrum.getRtTime());
                bfr.newLine();
                bfr.write("PEPMASS=" + Double.toString(spectrum.getPCMass()));
                bfr.newLine();
                bfr.write("CHARGE=" + spectrum.getCharge());
                bfr.newLine();

                //writing peaks
                peaks = spectrum.getPeakList();
                for (Peak peak : peaks) {
                    bfr.write(Double.toString(peak.getMz()) + " " + Double.toString(peak.getIntensity()));
                    bfr.newLine();
                }
                bfr.write("END IONS");
                bfr.newLine();

            }
        } catch (Exception ex) {
            Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bfr.close();
            } catch (IOException ex) {
                Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * appends spectrum to the file
     */
    @Override
    public void write(Spectrum spectrum) {

        try {

            ArrayList<Peak> peaks;
            //header part
            bfr.write("BEGIN IONS");
            bfr.newLine();
            bfr.write("TITLE=" + spectrum.getTitle());
            bfr.newLine();
            bfr.write("PROTEIN=" + spectrum.getProtein());
            bfr.newLine();
            bfr.write("SEQUENCE=" + spectrum.getSequence());
            bfr.newLine();
            bfr.write("MODS=" + spectrum.getModifications_juststr());
            bfr.newLine();
            bfr.write("SCAN=" + spectrum.getScanNumber());
            bfr.newLine();
            bfr.write("RTINSECONDS=" + spectrum.getRtTime());
            bfr.newLine();
            bfr.write("PEPMASS=" + Double.toString(spectrum.getPCMass()));
            bfr.newLine();
            bfr.write("CHARGE=" + spectrum.getCharge_asStr());
            bfr.newLine();

            //writing peaks
            peaks = spectrum.getPeakList();

            for (Peak peak : peaks) {
                bfr.write(Double.toString(peak.getMz()) + "\t" + Double.toString(peak.getIntensity()));
                bfr.newLine();
            }
            bfr.write("END IONS");
            bfr.newLine();

        } catch (IOException ex) {
            Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
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
