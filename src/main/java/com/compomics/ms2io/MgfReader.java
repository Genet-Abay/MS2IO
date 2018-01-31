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
public class MgfReader extends SpectrumReader {

    private List<IndexKey> IKey=null;
    private File indexFile = null;
    private double pm = 0.0;
    private double massErro = 0.0;

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
     * @param pm precursor mass of the spectrum to be read
     */
    public MgfReader(File specFile, File indxFile, double pm) {
        super(specFile);
        this.indexFile = indxFile;
        this.pm = pm;

    }

    /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indxFile index file that contains spectrum positions of the
     * spectrum file
     * @param pm
     * @param error
     */
    public MgfReader(File specFile, File indxFile, double pm, double error) {
        super(specFile);
        this.indexFile = indxFile;
        this.pm = pm;
        this.massErro = error;

    }
    
     /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indK index list of the spectra to be read
     * @param pm
     * @param error
     */
    public MgfReader(File specFile, List<IndexKey> indK, double pm, double error) {
        super(specFile);
        this.IKey = indK;
        this.pm = pm;
        this.massErro = error;

    }
    
         /**
     * Constructor to create object for spectrum reader
     *
     * @param specFile the spectrum file to be read
     * @param indK index list of the spectra to be read
     * @param pm
     */
    public MgfReader(File specFile, List<IndexKey> indK, double pm) {
        super(specFile);
        this.IKey = indK;
        this.pm = pm;

    }
    
    /**
     * Prepares for spectra reading for 3 options
     * 1 from file(whole spectra), 2 from given index file used to pick spectra
     * from spectra file based on positions, and 
     * the last option 3 from spectra file given spectra index to select spectra
     * from spectra file.
     * @return 
     */

    @Override
    public List<Spectrum> Read() {
        
        

        List<Spectrum> spectra = new ArrayList<>();
        if (this.indexFile == null) {
            //Call to read the whole spectra from the given file
           if(IKey==null){
               try {
                   spectra = readWholeSpec();
               } catch (IOException ex) {
                   Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           
           else{//Call selected spectra given the list of spectrum index key
               try {
                   List<Long> pos = positionsToberead(this.IKey, this.pm, this.massErro);
                   spectra=readSelectedSpec(pos);
               } catch (IOException ex) {
                   Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
               }
               
           }
            
        } else {//Call selected spectra read for given indes file
            try {
                List<Long> pos = positionsToberead(this.indexFile, this.pm, this.massErro);
                spectra = readSelectedSpec(pos);
            } catch (IOException ex) {
                Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return spectra;

    }

    /**
     * Reads the whole spectrum from given spectrum file
     * @return list of spectra
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private List<Spectrum> readWholeSpec() throws FileNotFoundException, IOException {

        List<Spectrum> spectra = new ArrayList<>();
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
        return spectra;

    }

    /**
     * Returns list of selected spectrum based on precursor mass with specified
     * mass error
     *
     * @return list of spectrum
     * @throws IOException
     */
    private List<Spectrum> readSelectedSpec(List<Long> pos) throws IOException {

        List<Spectrum> selSpectra = new ArrayList<>();        
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
            selSpectra.add(spec);

        }
        return selSpectra;

    }

    
}
