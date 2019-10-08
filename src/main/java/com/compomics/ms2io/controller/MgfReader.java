package com.compomics.ms2io.controller;

import com.compomics.ms2io.model.Spectrum;
import com.compomics.ms2io.model.Peak;
import com.compomics.ms2io.model.IndexKey;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class MgfReader extends SpectraReader {

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
        ArrayList<Spectrum> spectra = new ArrayList<>(100000);
        try {
            //BufferedReader br = new BufferedReader(new FileReader(this.spectraFile));

            BufferedRAF braf = new BufferedRAF(this.spectraFile, "r");
            IndexKey k=new IndexKey();
            Peak pk;
            ArrayList<Peak> pkList = new ArrayList<>();
            Spectrum spec = new Spectrum();

            
            String line = braf.readLine();
            while (line != null) {

                if (line.endsWith("\r")) {
                    line = line.replace("\r", "");
                }
                if (line.equals("")) {
                    //ignore the extra line in between the header and the data and between peaks

                } else if (Character.isDigit(line.charAt(0))) {
                    String fline = line.replaceAll("\\s+", " ");
                    String[] p = fline.split(" ");
                    //String[] p = line.split(" ");
                    double pcm = Double.parseDouble(p[0]);
                    double pci = Double.parseDouble(p[1]);

                    String annotation = "";
                    if(p.length > 2){
                        annotation = p[2];
                    }
                    pk=new Peak(pcm, pci, annotation);  
                    pkList.add(pk);

                } else if (line.startsWith("BEGIN")) {
                    pkList = new ArrayList<>();
                    spec = new Spectrum();
                    k.setPos(braf.getFilePointer());
                } else if (line.startsWith("TITLE")) {
                   // spec.setTitle(line.substring(line.indexOf("=") + 1));
                    spec.setSequence("");
                    spec.setProtein("");
                    spec.setTitle(line);
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
//                        if(temp[b].startsWith("TITLE")){
//                            String title=temp[b].substring(temp[b].indexOf("=") +1);
//                            if(line.contains("Decoy") || line.contains("decoy")|| line.contains("DECOY")){
//                                title+= "_decoy";
//                            }
//                            spec.setTitle(title);
//                        }
//                        else if (temp[b].startsWith("File")) {
//                            String name = temp[b].substring(temp[b].indexOf(":") + 2, temp[b].indexOf(",") -1);
//                            spec.setFileName(name);
//                        } else 
                         if (temp[b].startsWith("scan")) {
                            String ss = temp[b].replaceAll("[^0-9?!\\.]", "");
                            spec.setScanNumber(ss);
                        }
                    }

                } else if (line.startsWith("PEPMASS")) {
                    String[] temp = line.split(" ");
                    spec.setPCMass(Double.parseDouble(temp[0].substring(temp[0].indexOf("=") + 1)));
                    if(temp.length > 1){
                        spec.setPCIntesity(Double.parseDouble(temp[1]));
                    }
                  

                } else if (line.startsWith("RTINSECONDS")) {
                    String temp = line.substring(line.indexOf("=") + 1);
                    spec.setRtTime(Double.parseDouble(temp));

                } else if (line.startsWith("CHARGE")) {

                    spec.setCharge(line.substring(line.indexOf("=") + 1));

                } else if (line.endsWith("END IONS")) {    
                    spec.setNumPeaks(pkList.size());                   
                    k.setPM(spec.getPCMass());                  
                    k.setScanNum(spec.getScanNumber());                  
                    spec.setIndex(k);
                    spec.setPeakList(pkList);
                    spectra.add(spec);
                    
                   
                    

                }

                line = braf.readLine();

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
     *
     */
    @Override
    public ArrayList<Spectrum> readPart(double precMass, double error) {

        ArrayList<Spectrum> selectedSpectra = new ArrayList<>(100000);
        Indexer indxer = new Indexer();
        
        try {
            if (this.IKey == null) {
                if (this.indexFile != null) {
                    this.IKey = indxer.readFromFile(indexFile);
                } else {
                    //Index key not found, it should be read from file or should be provided
                }
            }
            List<Long>  pos = positionsToberead(this.IKey, precMass, error);

            int len = pos.size();
            for (int a = 0; a < len; a++) {
                selectedSpectra.add(readAt(pos.get(a)));
            }

        } catch (IOException ex) { 
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selectedSpectra;

    }
 
    /**
     * Reads spectrum at the specified position
     * @param position position to be read
     * @return spectrum at location position
     */

    @Override
    public Spectrum readAt(Long position) {
        
        Spectrum spec = new Spectrum();
        String msg="";
        try {
            
            ArrayList<Peak> pkList;
            String line;
            Peak pk;
            
            
            BufferedRAF braf = new BufferedRAF(this.spectraFile, "r");
            IndexKey k=new IndexKey();
            
            braf.seek(position);
            line = braf.readLine();
            
            pkList = new ArrayList<>();
            
            while (line != null) {
                
                if(line.equals("")){
                    //do nothing
                }
                else if (Character.isDigit(line.charAt(0))) {
                    String fline = line.replaceAll("\\s+", " ");
                    String[] p = fline.split(" ");
                    double pcm = Double.parseDouble(p[0]);
                    double pci = Double.parseDouble(p[1]);
                    
                    String annotation = "";
                    if(p.length > 2){
                        annotation = p[2];
                    }
                    pk=new Peak(pcm, pci, annotation);  
                    pkList.add(pk);
                    
                } else if (line.startsWith("BEGIN")) {
                    k=new IndexKey();
                    k.setPos(braf.getFilePointer());
                } else if (line.startsWith("TITLE")) {
                    spec.setTitle(line);
                    spec.setSequence("");
                    spec.setProtein("");
                    String[] temp = line.split(" ");
                    int tempLen = temp.length;
                    for (int b = 0; b < tempLen; b++) {
//                        if(temp[b].startsWith("TITLE")){
//                            String title=temp[b].substring(temp[b].indexOf("=") +1);
//                            if(line.contains("decoy")){
//                                title+= "_decoy";
//                            }
//                            spec.setTitle(title);
//                        }
//                        if (temp[b].startsWith("File")) {
//                            String name = temp[b].substring(temp[b].indexOf(":") + 2, temp[b].indexOf(",") - 1);
//                            spec.setFileName(name);
//                        } 
                        //else 
                        if (temp[b].startsWith("scan")) {
                            String ss = temp[b].replaceAll("[^0-9?!\\.]", "");
                            spec.setScanNumber(ss);
                        }
                    }
                    
                } else if (line.startsWith("PEPMASS")) {
                    String[] temp = line.split(" ");
                    spec.setPCMass(Double.parseDouble(temp[0].substring(temp[0].indexOf("=") + 1)));
                    if(temp.length > 1){
                        spec.setPCIntesity(Double.parseDouble(temp[1]));
                    }
                    
                } else if (line.startsWith("CHARGE")) {
                    
                    spec.setCharge(line.substring(line.indexOf("=") + 1));
                    
                } else if (line.endsWith("END IONS")) {
                    spec.setNumPeaks(pkList.size());  
                    k.setPM(spec.getPCMass());
                    k.setScanNum(spec.getScanNumber());
                    spec.setIndex(k);
                    spec.setPeakList(pkList);
                    break;
                }
                
                line = braf.readLine();
                
            }
        } catch (IOException | NumberFormatException ex ) {
            Logger.getLogger(MgfReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return spec;
    }

}
