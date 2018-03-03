package com.compomics.ms2io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    //main method to test the library
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File specfile = new File("C:/pandyDS/testfile.mgf");
        //File opfile = new File("C:/pandyDS/human_hcd_selected_selected.msp");

        String indxfilename = specfile.getName().substring(0, specfile.getName().lastIndexOf("."));
        File indxfile = new File(specfile.getParent(), indxfilename + ".idx");

        double pcm = 379.6062;
        double error = 0.05;

        List<Spectrum> spectra;
        List<IndexKey> indxList;

        if (indxfile.exists()) {

            Indexer indxer = new Indexer();
            indxList = indxer.readFromFile(indxfile);

        } else {

            Indexer gi = new Indexer(specfile);
            indxList = gi.generate();
            Collections.sort(indxList);

        }
        
        

        if (specfile.getName().endsWith("mgf")) {
            SpectraReader rd = new MgfReader(specfile, indxList);
            spectra = rd.readAll();

        } else if (specfile.getName().endsWith("msp")) {
            SpectraReader rd = new MspReader(specfile, indxList);
            spectra = rd.readAll();

        }

        //SpectrumWriter wr = new MspWriter(opfile, spectra);
        // wr.write();
    }
}
