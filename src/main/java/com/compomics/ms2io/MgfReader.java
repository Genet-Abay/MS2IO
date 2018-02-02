package com.compomics.ms2io;

import java.io.BufferedReader;
import java.io.File;
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
public class MgfReader extends SpectrumReader {

    private List<IndexKey> IKey = null;
    private File indexFile = null;

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFfile the spectrum file to be read
     */
    public MgfReader(File specFfile) {
        super(specFfile);

    }

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indxFile index file that contains spectrum positions of the
     * spectrum file
     */
    public MgfReader(File specFile, File indxFile) {
        super(specFile);
        this.indexFile = indxFile;

    }

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indK index list of the spectra to be read
     */
    public MgfReader(File specFile, List<IndexKey> indK) {
        super(specFile);
        this.IKey = indK;

    }

    /**
     * Reads all spectra in specified file
     *
     * @return list of spectra
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

                } else if (line.startsWith("BEGIN")) {
                    pkList = new ArrayList<>();
                    spec = new Spectrum();
                } else if (line.startsWith("TITLE")) {
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
                        if (temp[b].startsWith("TITLE")) {
                            String title = temp[b].substring(temp[b].indexOf("=") + 1);
                            spec.setTitle(title);
                        } else if (temp[b].startsWith("File")) {
                            String name = temp[b].substring(temp[b].indexOf("=") + 1);
                            spec.setTitle(name);
                        } else if (temp[b].startsWith("scan")) {
                            String ss = temp[b].replaceAll("[^0-9?!\\.]", "");
                            spec.setScanNumber(ss);
                        }
                    }

                } else if (line.startsWith("PEPMASS")) {
                    String[] temp = line.split(" ");
                    spec.setPCMass(Double.parseDouble(temp[0].substring(temp[0].indexOf("=") + 1)));
                    spec.setPCIntesity(Double.parseDouble(temp[1]));

                } else if (line.startsWith("CHARGE")) {

                    spec.setCharge(line.substring(line.indexOf("=") + 1));

                } else if (line.endsWith("END IONS")) {
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

    
    /**
     * Returns list of selected spectrum based on precursor mass with specified
     * mass error
     *
     * @return list of spectrum
     
     */
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

    /**
     * Returns list of selected spectrum based on spectrum title
     *
     * @return list of spectrum
     
     */
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

                //if line is starts with digit it assumes as a peak
                if (Character.isDigit(line.charAt(0))) {
                    String[] p = line.split(" ");
                    double pcm = Double.parseDouble(p[0]);
                    double pci = Double.parseDouble(p[1]);

                    pk = new Peak(pcm, pci);
                    pkList.add(pk);

                } else if (line.startsWith("BEGIN")) {
                } else if (line.startsWith("TITLE")) {
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
                        if (temp[b].startsWith("TITLE")) {
                            String title = temp[b].substring(temp[b].indexOf("=") + 1);
                            spec.setTitle(title);
                        } else if (temp[b].startsWith("File")) {
                            String name = temp[b].substring(temp[b].indexOf("=") + 1);
                            spec.setTitle(name);
                        } else if (temp[b].startsWith("scan")) {
                            String ss = temp[b].replaceAll("[^0-9?!\\.]", "");
                            spec.setScanNumber(ss);
                        }
                    }

                } else if (line.startsWith("PEPMASS")) {
                    String[] temp = line.split(" ");
                    spec.setPCMass(Double.parseDouble(temp[0].substring(temp[0].indexOf("=") + 1)));
                    spec.setPCIntesity(Double.parseDouble(temp[1]));

                } else if (line.startsWith("CHARGE")) {

                    spec.setCharge(line.substring(line.indexOf("=") + 1));

                } else if (line.endsWith("END IONS")) {
                    spec.setPeakList(pkList);
                    break;
                }

                line = braf.readLine();

            }
            selectedSpectra.add(spec);

        }

    }

}
