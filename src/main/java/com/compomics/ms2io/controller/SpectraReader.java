/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io.controller;

import com.compomics.ms2io.model.Spectrum;
import com.compomics.ms2io.model.IndexKey;
import com.compomics.ms2io.model.Modification;
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
     *
     * @param specFile file to be read
     */
    protected SpectraReader(File specFile) {
        this.spectraFile = specFile;
    }

    /**
     * abstract class to read all spectrum
     *
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
        int low = 0;
        int high = indKey.size() - 1;
        int startPos = 0;
        while (low < high) {

            startPos = (low + high) / 2;
            if (indKey.get(startPos).getPM() > lowerBound && high != startPos) {
                high = startPos;

            } else if (indKey.get(startPos).getPM() < lowerBound && low != startPos) {
                low = startPos;

            } else {
                break;
            }

        }

        for (int a = startPos; a < indKey.size(); a++) {
            double mass = indKey.get(a).getPM();

            if (mass <= upperBound) {
                pos.add(indKey.get(a).getPos());

            } //index file recoreded in pm increasing order, so we can stop checking out of bounds
            else {
                break;
            }
        }

        return pos;
    }

    /**
     * parse modifications, if present, for the given string
     *
     * @param comment string containing modification information
     * @return
     */
    protected List<Modification> getModifications(String comment, String sequence) {

        List<Modification> modifications = new ArrayList<>();
        Modification my_mod=new Modification();

        String[] coms = comment.split(" ");
        for (String s : coms) {
            if (s.contains("Mods")) {
                String mod = s.substring(s.indexOf("=") + 1);
                if (!"0".equals(mod)) {

                    int mod_site;
                    String aa;
                    String mod_name;
                    double mass_shift;

                    String[] strAr = mod.split("[/()]");
                    int num_mods = strAr.length - 1; //first string represents number of tmpMod

                    //List l = new ArrayList<String>();
                    for (int p = 1; p < num_mods + 1; p++) {
                        mod_site = 0;

                        if (!"".equals(strAr[p])) {
                            strAr[p] = strAr[p].replaceAll("\\s", ""); //remove all white space
                            String[] m = strAr[p].split(",");
                            int mLen = m.length;

                            if (mLen < 3 || mod_site >= sequence.length()) {
                                break;
                            }

                            mod_site = Integer.parseInt(m[0]);
                            aa = Character.toString(sequence.charAt(mod_site));
                            if (mLen == 3) {
                                if (!isNumeric(m[2])) {
                                    my_mod = new Modification(mod_site,aa, m[2]);
                                } else {
                                    my_mod = new Modification(mod_site, aa, Double.parseDouble(m[2]));
                                }
                            }

                            modifications.add(my_mod);
                        }
                    }
                    //}
                }
                break;
            }
        }

        return modifications;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
