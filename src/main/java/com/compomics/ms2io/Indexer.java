package com.compomics.ms2io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Genet
 */
public class Indexer implements Closeable, AutoCloseable {

    private BufferedRAF braf;
    private final File file;

    /**
     * constructor to generate the index
     *
     * @param file
     * @throws java.io.IOException
     */
    public Indexer(File file) throws IOException {
        this.braf = new BufferedRAF(file, "r");
        this.file = file;
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

        List<IndexKey> index = new ArrayList<>();
        if (this.file.getName().endsWith("mgf")) {
            index = mgfIndexer();

        } else if (this.file.getName().endsWith("msp")) {
           index = mspIndexer();
        } else {
            //report file type error
        }

        String indxfilename = file.getName().substring(0, file.getName().lastIndexOf("."));
        File indxFile = new File(file.getParent(), indxfilename + ".idx");

        BufferedWriter bw = new BufferedWriter(new FileWriter(indxFile));
        for (IndexKey inx : index) {
            String temp = inx.getCombinedIndex();
            bw.write(temp);
            bw.newLine();

        }
        bw.close();
        
        return index;

    }

    /**
     * Generate index for .mgf file
     *
     * @throws IOException
     */
    private List<IndexKey> mgfIndexer() throws IOException {

        List<IndexKey> index=new ArrayList<>();
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

            Collections.sort(index);
        } finally {
            if (braf != null) {
                braf.close();
            }
        }

        return index;
    }

    /**
     * Generate index for .msp spectrum file format
     *
     * @throws IOException
     */
    private List<IndexKey> mspIndexer() throws IOException {

        List<IndexKey> index=new ArrayList<>();
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

            Collections.sort(index);
        } finally {
            if (braf != null) {
                braf.close();
            }
        }

        return index;
    }

}
