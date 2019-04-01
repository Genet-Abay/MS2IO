package com.compomics.ms2io;

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

     /**
      * close the random access buffer
      * @throws IOException 
      */
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

        } else if (this.file.getName().endsWith("msp") || this.file.getName().endsWith("sptxt")) {
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
                while (line != null ) {
                    if("".equals(line)){
                         line = braf.getNextLine();
                         continue;
                    }
                        
                    else if(line.startsWith("END") || Character.isDigit(line.charAt(0)))
                        break;
                    // if empty line -> continue
                    // if starts zith end -> break
                    
                    if (line.startsWith("BEGIN")) {
                       // p = braf.getFilePointer();
                        indexObj.setPos(p);
                        header = true;
                    }
                    if (line.startsWith("TITLE")) {
                        String[] temp = line.split(" ");
                        int tempLen = temp.length;
                        for (int a = 0; a < tempLen; a++) {
                            if (temp[a].startsWith("scan")) {
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

    /**
     * saves generated index to given file
     * @param file 
     */
    public void saveIndex2File(File file)
    {
        FileOutputStream fout = null;
        ObjectOutputStream oos=null;
        try {
            String indxfilename = file.getName().substring(0, file.getName().lastIndexOf("."));           
            fout = new FileOutputStream(file.getParent()+ "\\"+ indxfilename + ".idx");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(index);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
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
     */
    public List<IndexKey> readFromFile(File indxfile) {
       FileInputStream fis=null;
       ObjectInputStream ois=null;
        try {            
            fis = new FileInputStream(indxfile); 
            ois  = new ObjectInputStream(fis);
            index=(List<IndexKey>)ois.readObject();                
            
        } catch (EOFException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally {
            try {
                ois.close();
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return index;
    }
}
