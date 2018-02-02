package com.compomics.ms2io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class MspReader extends SpectrumReader {

    private File indexFile = null;
    List<IndexKey> IKey = null;

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFfile the spectrum file to be read
     */
    public MspReader(File specFfile) {
        super(specFfile);

    }

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indxFile index file that contains spectrum positions of the
     * spectrum file
     */
    public MspReader(File specFile, File indxFile) {
        super(specFile);
        this.indexFile = indxFile;

    }

    
    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indK index list of the spectra to be read
     *
     */
    public MspReader(File specFile, List<IndexKey> indK) {
        super(specFile);
        this.IKey = indK;

    }


    /**
     * Read the whole spectrum from the given spectrum file
     *
     * @return List of spectrum
     */
    @Override
    public ArrayList<Spectrum> readAll() {

        ArrayList<Spectrum> spectra = new ArrayList<>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(this.spectraFile));

            Peak pk;
            ArrayList<Peak> pkList = new ArrayList<>();
            Spectrum spec = new Spectrum();

            String line = br.readLine();
            while (line != null) {

                if (line.endsWith("\r")) {
                    line = line.replace("\r", "");
                }

                if (Character.isDigit(line.charAt(0))) {
                    String[] p = line.split(" ");
                    double pcm = Double.parseDouble(p[0]);
                    double pci = Double.parseDouble(p[1]);

                    pk = new Peak(pcm, pci);
                    pkList.add(pk);

                } else if (line.startsWith("Name")) {
                    pkList = new ArrayList<>();
                    spec = new Spectrum();
                    spec.setTitle(line.substring(line.indexOf(":") + 2));

                    String ch = line.substring(line.indexOf('/') + 1);
                    spec.setCharge(ch);

                } else if (line.startsWith("MW:")) {
                    try {
                        double molecularWeight = Double.parseDouble(line.substring(line.indexOf(':') + 2));
                        spec.setMW(molecularWeight);
                    } catch (Exception e) {
                        System.out.println("An exception was thrown when trying to decode the msp Molecular Weight");

                    }
                } else if (line.startsWith("Comment")) {
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
                        if (temp[b].startsWith("Parent")) {
                            double precmass = Double.parseDouble(temp[b].substring(temp[b].indexOf("=") + 1));
                            spec.setPCMass(precmass);
                        } else if (temp[b].startsWith("Scan")) {
                            String scan = temp[b].substring(temp[b].indexOf("=") + 1);
                            spec.setScanNumber(scan);
                        }
                    }

                } else if (line.startsWith("Num peaks")) {
                    String temp = line.substring(line.indexOf(":") + 2);
                    spec.setNumPeaks(Integer.parseInt(temp));

                } else if (line.equals("") || line == null) {
                    spec.setPeakList(pkList);
                    spectra.add(spec);

                }

                line = br.readLine();

            }

        } catch (IOException ex) {
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return spectra;

    }

   
    @Override
    public ArrayList<Spectrum> readPart(double precMass, double error) {
        
        ArrayList<Spectrum> selectedSpectra = new ArrayList<>();
        List<Long> pos;

        try {
            if (this.IKey != null) {
                pos = positionsToberead(this.IKey, precMass, error);
            } else if (this.indexFile != null) {
                pos = positionsToberead(this.indexFile, precMass, error);
            } else {
                //Log that there is no spectrum index
                return null;
            }

            filter(pos, selectedSpectra);
        } catch (IOException ex) {
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selectedSpectra;
    }

    @Override
    public ArrayList<Spectrum> readPart(String title) {
        
        ArrayList<Spectrum> selectedSpectra = new ArrayList<>();
        List<Long> pos;

        try {
            if (this.IKey != null) {

                pos = positionsToberead(this.IKey, title);

            } else if (this.indexFile != null) {
                pos = positionsToberead(this.indexFile, title);
            } else {
                //Log that there is no spectrum index
                return null;
            }

            filter(pos, selectedSpectra);
        } catch (IOException ex) {
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selectedSpectra;
    }

    
    
     /**
     * select spectra at the given positions 
     *
     * @throws IOException
     */
    private void filter(List<Long> pos, ArrayList<Spectrum> selectedSpectra) throws IOException {

        Spectrum spec;
        int len = pos.size();
        String line;
        Peak pk;
        ArrayList<Peak> pkList;

        BufferedRAF braf = new BufferedRAF(this.spectraFile, "r");
        for (int a = 0; a < len; a++) {
            spec = new Spectrum();
            braf.seek(pos.get(a));
            line = braf.readLine();

            pkList = new ArrayList<>();

            while (line != null) {

                if (line.equals("")) {
                    spec.setPeakList(pkList);
                    break;
                } else if (Character.isDigit(line.charAt(0))) {
                    String fline = line.replaceAll("\\s+", " ");
                    String[] p = fline.split(" ");
                    double pcm = Double.parseDouble(p[0]);
                    double pci = Double.parseDouble(p[1]);

                    pk = new Peak(pcm, pci);
                    pkList.add(pk);

                } else if (line.startsWith("Name")) {
                    spec.setTitle(line.substring(line.indexOf(":") + 2));

                } else if (line.startsWith("MW")) {
                    spec.setMW(Double.parseDouble(line.substring(line.indexOf(":") + 2)));

                } else if (line.startsWith("Comment")) {
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
                        if (temp[b].startsWith("Parent")) {
                            double precmass = Double.parseDouble(temp[b].substring(temp[b].indexOf("=") + 1));
                            spec.setPCMass(precmass);
                            spec.setScanNumber("No Info.");
                            break;
                        }
                    }

                } else if (line.startsWith("Num")) {
                    spec.setNumPeaks(Integer.parseInt(line.substring(line.indexOf(":") + 2)));

                }
                line = braf.readLine();
            }
            selectedSpectra.add(spec);
        }
       

    }

}
