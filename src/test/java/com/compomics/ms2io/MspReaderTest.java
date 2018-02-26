
package com.compomics.ms2io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Genet
 */
public class MspReaderTest extends TestCase {
    
    public MspReaderTest(String testName) {
        super(testName);
    }

    /**
     * Test of readAll method, of class MspReader.
     */
    public void testReadAll() {
        System.out.println("readAll");
        SpectrumReader instance = new MspReader(new File("C:/pandyDS/testfile.msp"));
        ArrayList<Spectrum> expResult = new ArrayList<>();
        
        //Spectrum 1
        Spectrum sp1=new Spectrum();
        sp1.setTitle("YDDMAAAMK/2");
        sp1.setCharge("2");
        sp1.setPCMass(498.5731);  
        sp1.setMW(997.1463);
        sp1.setNumPeaks(3);
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(164.0706, 4.4700);
        pk.add(p);
        p=new Peak(279.0975, 473.5073);
        
        pk.add(p);        
        p=new Peak(394.1245, 305.4938);
        pk.add(p);
        
        sp1.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setTitle("YDDMAAAMK/2");
        k.setPM(498.5731);
        k.setPos(0L);
        sp1.setIndex(k);
        
        expResult.add(sp1);
        
        //Spectrum 2
       
        Spectrum sp2=new Spectrum();
        sp2.setCharge("3");
        sp2.setTitle("YDDMAAAMK/3");
        sp2.setPCMass(332.3821);        
        sp2.setMW(997.1463);
        sp2.setNumPeaks(2);
        
        pk=new ArrayList<>();
        p=new Peak(164.0706, 1.1742);
        pk.add(p);
        p=new Peak(82.5389, 0.3678);
        
        pk.add(p);   
        
        sp2.setPeakList(pk);
        
        k=new IndexKey();
        k.setTitle("YDDMAAAMK/3");
        k.setPM(332.3821);
        k.setPos(139L);
        sp2.setIndex(k);
        expResult.add(sp2);
         
        
        ArrayList<Spectrum> result = instance.readAll();
            
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expResult.get(0).getFileName(), result.get(0).getFileName());
        assertEquals(expResult.get(0).getPCMass(), result.get(0).getPCMass());
        assertEquals(expResult.get(0).getCharge(), result.get(0).getCharge());
        
        assertEquals(expResult.get(0).getPeakList().size(), result.get(0).getPeakList().size());
        assertEquals(expResult.get(1).getPeakList().size(), result.get(1).getPeakList().size());
    }

    /**
     * Test of readPart method, of class MspReader.
     * @throws java.io.IOException
     */
    public void testReadPart_double_double() throws IOException {
        System.out.println("readPart");
        double precMass = 498.5731;
        double error = 0.0;
        
        Indexer giExp = new Indexer(new File("C:/pandyDS/testfile.msp"));
        List<IndexKey> indxList = giExp.generate();        
        SpectrumReader instance = new MspReader(new File("C:/pandyDS/testfile.msp"), indxList);
        ArrayList<Spectrum> expResult = new ArrayList<>();
        
        //Spectrum 1
        Spectrum sp1=new Spectrum();
        sp1.setTitle("YDDMAAAMK/2");
        sp1.setCharge("2");
        sp1.setPCMass(498.5731);  
        sp1.setMW(997.1463);
        sp1.setNumPeaks(3);
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(164.0706, 4.4700);
        pk.add(p);
        p=new Peak(279.0975, 473.5073);
        
        pk.add(p);        
        p=new Peak(394.1245, 305.4938);
        pk.add(p);
        
        sp1.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setTitle("YDDMAAAMK/2");
        k.setPM(498.5731);
        k.setPos(0L);
        sp1.setIndex(k);
        
        expResult.add(sp1);
        
        ArrayList<Spectrum> result = instance.readPart(precMass, error);
        
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expResult.get(0).getFileName(), result.get(0).getFileName());
        assertEquals(expResult.get(0).getPCMass(), result.get(0).getPCMass());
        assertEquals(expResult.get(0).getCharge(), result.get(0).getCharge());        
        assertEquals(expResult.get(0).getPeakList().size(), result.get(0).getPeakList().size());
    }

    /**
     * Test of readPart method, of class MspReader.
     * @throws java.io.IOException
     */
    public void testReadPart_String() throws IOException {
        System.out.println("readPart");
        String title = "YDDMAAAMK/2";
        Indexer giExp = new Indexer(new File("C:/pandyDS/testfile.msp"));
        List<IndexKey> indxList = giExp.generate();
        
        SpectrumReader instance = new MspReader(new File("C:/pandyDS/testfile.msp"), indxList);
        
        ArrayList<Spectrum> expResult = new ArrayList<>();
        
        //Spectrum 1
        Spectrum sp1=new Spectrum();
        sp1.setTitle("YDDMAAAMK/2");
        sp1.setCharge("2");
        sp1.setPCMass(498.5731);  
        sp1.setMW(997.1463);
        sp1.setNumPeaks(3);
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(164.0706, 4.4700);
        pk.add(p);
        p=new Peak(279.0975, 473.5073);
        
        pk.add(p);        
        p=new Peak(394.1245, 305.4938);
        pk.add(p);
        
        sp1.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setTitle("YDDMAAAMK/2");
        k.setPM(498.5731);
        k.setPos(0L);
        sp1.setIndex(k);
        
        expResult.add(sp1);
        
        ArrayList<Spectrum> result = instance.readPart(title);
        
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expResult.get(0).getFileName(), result.get(0).getFileName());
        assertEquals(expResult.get(0).getPCMass(), result.get(0).getPCMass());
        assertEquals(expResult.get(0).getCharge(), result.get(0).getCharge());        
        assertEquals(expResult.get(0).getPeakList().size(), result.get(0).getPeakList().size());
    }

    /**
     * Test of readAt method, of class MspReader.
     * @throws java.io.IOException
     */
    public void testReadAt() throws IOException {
        System.out.println("readAt");
        Long position = 0L;
        Indexer giExp = new Indexer(new File("C:/pandyDS/testfile.msp"));
        List<IndexKey> indxList = giExp.generate();
        
        SpectrumReader instance = new MspReader(new File("C:/pandyDS/testfile.msp"), indxList);
     
        Spectrum expResult=new Spectrum();
        expResult.setTitle("YDDMAAAMK/2");
        expResult.setCharge("2");
        expResult.setPCMass(498.5731);  
        expResult.setMW(997.1463);
        expResult.setNumPeaks(3);
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(164.0706, 4.4700);
        pk.add(p);
        p=new Peak(279.0975, 473.5073);
        
        pk.add(p);        
        p=new Peak(394.1245, 305.4938);
        pk.add(p);
        
        expResult.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setTitle("YDDMAAAMK/2");
        k.setPM(498.5731);
        k.setPos(0L);
        expResult.setIndex(k);
        
        Spectrum result = instance.readAt(position);
        
        
        assertEquals(expResult.getTitle(), result.getTitle());
        assertEquals(expResult.getFileName(), result.getFileName());
        assertEquals(expResult.getPCMass(), result.getPCMass());
        assertEquals(expResult.getCharge(), result.getCharge());        
        assertEquals(expResult.getPeakList().size(), result.getPeakList().size());
    }
    
}
