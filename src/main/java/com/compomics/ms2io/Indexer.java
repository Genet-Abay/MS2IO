package com.compomics.ms2io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class Indexer implements Closeable, AutoCloseable {

    private BufferedRAF braf;
    private final File file;
    List<IndexKey> index;
    
    /**
     * constructor
     */
    public Indexer(){
        index=new ArrayList<>();
        this.file=null;
    }

    /**
     * constructor to generate the index
     *
     * @param file
     * @throws java.io.IOException
     */
    public Indexer(File file) throws IOException {
        this.braf = new BufferedRAF(file, "r");
        this.file = file;
        index=new ArrayList<>();
    }

     
    @Override
    public void close() throws IOException {
        braf.close();      
    }


    /**
     * Generate index file based on file type
     *
     * @return list of index keys of the spectrum
     * @throws IOException
     */
    public List<IndexKey>  generate() throws IOException {

        
        if (this.file.getName().endsWith("mgf")) {
            mgfIndexer();

        } else if (this.file.getName().endsWith("msp")) {
           mspIndexer();
        } else {
            //report file type error
        }

            
        return index;

    }

    /**
     * Generate index for .mgf file
     *
     * @throws IOException
     */
    private void mgfIndexer() throws IOException {
        
        index = new ArrayList<>();
        try {
            
            braf = new BufferedRAF(file, "r");
            braf.seek(0);
            Long end = this.file.length();
            String line;
            IndexKey indexObj;
            Long p;
            boolean header = false;

            while ((p = braf.getFilePointer()) < end) {
                header = false;
                line = braf.getNextLine();
                indexObj = new IndexKey();
                while (line != null &&  !line.startsWith("END") && !Character.isDigit(line.charAt(0))) {

                    if (line.startsWith("BEGIN")) {
                        indexObj.setPos(p);
                        header = true;
                    }
                    if (line.startsWith("TITLE")) {
                        String[] temp = line.split(" ");
                        int tempLen = temp.length;
                        for (int a = 0; a < tempLen; a++) {
                            if (temp[a].startsWith("TITLE")) {
                                String name = temp[a].substring(temp[a].indexOf("=") + 1);
                                indexObj.setName(name);
                            } else if (temp[a].startsWith("scan")) {
                                String ss = temp[a].replaceAll("[^0-9?!\\.]", "");
                                indexObj.setScanNum(ss);
                            }
                        }

                    } else if (line.startsWith("PEPMASS")) {
                        String[] temp = line.split(" ");
                        indexObj.setPM(Double.parseDouble(temp[0].substring(temp[0].indexOf("=") + 1)));

                    } else {
                        //error on mgf format
                    }
                    
                    line = braf.getNextLine();
                }
                if (header) {
                    index.add(indexObj);
                }

            }

           // Collections.sort(index);
        } finally {
            if (braf != null) {
                braf.close();
            }
        }

       
    }

    /**
     * Generate index for .msp spectrum file format
     *
     * @throws IOException
     */
    private void mspIndexer() throws IOException {

        index = new ArrayList<>();
        try {
            braf = new BufferedRAF(file, "r");
            braf.seek(0);
            Long end = this.file.length();
            String line;
            IndexKey indexObj;
            Long p;
            boolean header;

            while ((p = braf.getFilePointer()) < end) {
                header = false;
                line = braf.getNextLine();
                indexObj = new IndexKey();
                while (line != null && !line.equals("")&& !Character.isDigit(line.charAt(0))) {

                    if (line.startsWith("Name")) {
                        indexObj.setPos(p);
                        indexObj.setName(line.substring(line.indexOf(":") + 2));
                        header = true;
                    }
                    else if (line.startsWith("Comment")) {
                        String[] temp = line.split(" ");
                        int tempLen = temp.length;
                        for (int a = 0; a < tempLen; a++) {
                            if (temp[a].startsWith("Parent")) {
                                double precmass = Double.parseDouble(temp[a].substring(temp[a].indexOf("=") + 1));

                                indexObj.setPM(precmass);
                                indexObj.setScanNum("no info.");
                                break;
                                
                            } 
                        }

                    } 
                    
                    line = braf.getNextLine();
                }
                if (header) {
                    index.add(indexObj);
                }

            }

           // Collections.sort(index);
        } finally {
            if (braf != null) {
                braf.close();
            }
        }

    }

    public void saveIndex2File(File file)
    {
        FileOutputStream fout = null;
        try {
            String indxfilename = file.getName().substring(0, file.getName().lastIndexOf("."));
            File indxFile = new File(file.getParent(), indxfilename + ".idx");
            fout = new FileOutputStream(indxFile);
            BufferedOutputStream bos=new BufferedOutputStream(fout);
            ObjectOutputStream opStream = new ObjectOutputStream(bos);
            // BufferedWriter bw = new BufferedWriter(new FileWriter(indxFile));
            for (IndexKey inx : index) {
                //String temp = inx.getCombinedIndex();
                opStream.writeObject(inx);
                //opStream.write('\n');

            }   opStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns list of spectrum index keys read from given file
     *
     * @param indxfile file contains the index of spectra which contains
     * positions of spectrum on the actual file precursor mass and scan number
     * @return array of indexes
     * @throws IOException
     * @throws java.lang.ClassNotFoundException
     */
    public List<IndexKey> readFromFile(File indxfile) throws IOException, ClassNotFoundException {
        List<IndexKey> indxKey = new ArrayList<>();
        try {
            FileInputStream fin = new FileInputStream(indxfile);
            BufferedInputStream bis = new BufferedInputStream(fin);
            ObjectInputStream ois = new ObjectInputStream(bis);
            IndexKey indObject = (IndexKey) ois.readObject();
            while (true) {
                indxKey.add(indObject);
                indObject = (IndexKey) ois.readObject();
            }
        } catch (EOFException e) {
            return indxKey;
        }
    }
}
