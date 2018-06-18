package com.compomics.ms2io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    //main method to test the library
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //Test library merge and append
        File file1 = new File("C:/pandyDS/testfile.msp");
        File file2 = new File("C:/pandyDS/testfile2.msp");
        File file3 = new File("C:/pandyDS/testfile3.msp");
       // MergeLibrary mrg = new MergeLibrary(file1, file2, file3);
       // mrg.Start();

       AppendLibrary appnd=new AppendLibrary(file1, file2);
       appnd.Append();
//        File file=new File("C:/pandyDS/testfile.mgf");
//        File decoyFile = new File("C:/pandyDS/testfile_decoy" + ".mgf");
//        BufferedWriter bw=null;
//        
//        Indexer gi;
//        try {
//            gi = new Indexer(file);
//            
//        List<IndexKey> indxList=gi.generate();
//        
//        
//        SpectraReader rd = new MgfReader(file, indxList);
//        SpectraWriter wr = new MgfWriter(decoyFile);
//
//         bw = new BufferedWriter(new FileWriter(decoyFile));
//        Spectrum spectrum;
//        for (IndexKey indx : indxList) {
//            Long pos = indx.getPos();
//            spectrum = rd.readAt(pos);
//            wr.write(spectrum, bw);
//        }
//
//      
//        } catch (IOException ex) {
//            Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
//        }finally {
//            try {
//                bw.close();
//            } catch (IOException ex) {
//                Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        
//        
//        
//        File specfile = new File("C:/pandyDS/MSMSpos20_6.mzML");// human_hcd_selected.msp//  MSMSpos20_6.mzML//AdLungCD4_Medium.mgf
//        File opfile = new File("C:/pandyDS/AdLungCD4_Medium.mgf");
//
////        if (specfile.getName().endsWith("mzML")) {
////
////            MzmlReader reader = new MzmlReader(specfile);
////            ArrayList<Spectrum> spec = reader.readAll();
////            System.out.println("reading finished" + spec.toString());
////
////        }
//
//        String indxfilename = specfile.getName().substring(0, specfile.getName().lastIndexOf("."));
//        File indxfile = new File(specfile.getParent(), indxfilename + ".idx");
//
//        double pcm = 1298.5546875;//584.8876; //;
//        double error = 0.03;
//
//        List<Spectrum> spectra = null;
//        List<IndexKey> indxList;
//
//        if (indxfile.exists()) {
//
//            Indexer indxer = new Indexer();
//            indxList = indxer.readFromFile(indxfile);
//
//        } else {
//
//            Indexer gi = new Indexer(specfile);
//            indxList = gi.generate();
//            Collections.sort(indxList);
//
//        }
//
//        SpectraReader rd = null;
//        SpectraWriter wr = null;
//        if (specfile.getName().endsWith("mgf")) {
//            rd = new MgfReader(specfile, indxList);
//            wr = new MspWriter(opfile, spectra);
//
//        } else if (specfile.getName().endsWith("msp")) {
//            rd = new MspReader(specfile, indxList);
//            wr = new MspWriter(opfile, spectra);
//
//        }
//
//        if (rd != null) {
//            spectra = rd.readPart(pcm, error);
//            wr.write();
//        }
    }
}
