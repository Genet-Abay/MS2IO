/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class MgfWriter extends SpectraWriter {

    /**
     * class constructor
     *
     * @param f file to be write to
     * @param s spectrum to be write on the file
     */
    public MgfWriter(File f, List<Spectrum> s) {
        super(f, s);
    }

    /**
     * class constructor
     *
     * @param f file to be write to
     */
    public MgfWriter(File f) {
        super(f);
    }

    /**
     * writes given list of spectrum to the file
     */
    @Override
    public void write() {

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.file, true));
            for (Spectrum spectrum : this.spectra) {

                ArrayList<Peak> peaks;
                //header part
                bw.write("BEGINE");
                bw.newLine();
                bw.write("TITLE=" + spectrum.getTitle() + " " + "FILE:" + spectrum.getFileName() + " " + "scan=" + spectrum.getScanNumber());
                bw.newLine();
                bw.write("PEPMASS=" + Double.toString(spectrum.getPCMass()) + "" + Double.toString(spectrum.getPCIntensity()));
                bw.newLine();
                bw.write("CHARGE=" + spectrum.getCharge());
                bw.newLine();

                //writing peaks
                peaks = spectrum.getPeakList();

                for (Peak peak : peaks) {
                    bw.write(Double.toString(peak.getMz()) + " " + Double.toString(peak.getIntensity()));
                    bw.newLine();
                }
                bw.write("END IONS");
                bw.newLine();

            }
        } catch (IOException ex) {
            Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * appends spectrum to the file
     */
    @Override
    public void write(Spectrum spectrum, BufferedWriter bw) {

        try {

            ArrayList<Peak> peaks;
            //header part
            bw.write("BEGINE");
            bw.newLine();
            bw.write("TITLE=" + spectrum.getTitle() + " " + "FILE:" + spectrum.getFileName() + " " + "scan=" + spectrum.getScanNumber());
            bw.newLine();
            bw.write("PEPMASS=" + Double.toString(spectrum.getPCMass()) + "" + Double.toString(spectrum.getPCIntensity()));
            bw.newLine();
            bw.write("CHARGE=" + spectrum.getCharge());
            bw.newLine();

            //writing peaks
            peaks = spectrum.getPeakList();

            for (Peak peak : peaks) {
                bw.write(Double.toString(peak.getMz()) + " " + Double.toString(peak.getIntensity()));
                bw.newLine();
            }
            bw.write("END IONS");
            bw.newLine();

        } catch (IOException ex) {
            Logger.getLogger(MgfWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
