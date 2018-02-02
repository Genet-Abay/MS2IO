package com.compomics.ms2io;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        File specfile = new File("C:/pandyDS/human_hcd_selected.msp");
        File opfile = new File("C:/pandyDS/human_hcd_selected_selected.msp");

        String indxfilename = specfile.getName().substring(0, specfile.getName().lastIndexOf("."));
        File indxfile = new File(specfile.getParent(), indxfilename + ".idx");

        double pcm = 379.6062;
        double error = 0.05;

        List<Spectrum> spectra;

        if (indxfile.exists()) {

            SpectrumReader rd = new MspReader(specfile, indxfile);
            spectra = rd.readPart(pcm, error);

        } else {

            Indexer gi = new Indexer(specfile);
            List<IndexKey> indxList = gi.generate();
            SpectrumReader rd = new MspReader(specfile, indxList);
            spectra = rd.readPart(pcm, error);

        }

        SpectrumWriter wr = new MspWriter(opfile, spectra);
        wr.write();
    }
}
