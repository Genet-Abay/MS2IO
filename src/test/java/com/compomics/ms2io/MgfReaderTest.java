package com.compomics.ms2io;

import java.util.ArrayList;
import junit.framework.TestCase;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Genet
 */
public class MgfReaderTest extends TestCase {
    
    /**
     * class constructor
     * @param testName 
     */
    public MgfReaderTest(String testName) {
        super(testName);
    }

    /**
     * Test of readAll method, of class MgfReader.
     */
    public void testReadAll() {
        
        System.out.println("readAll");
        SpectraReader instance = new MgfReader(new File("C:/pandyDS/testfile.mgf"));
        ArrayList<Spectrum> expResult = new ArrayList<>();
        
        //Spectrum 1
        Spectrum sp1=new Spectrum();
        sp1.setCharge("3+");
        sp1.setFileName("Test_File1");   
        sp1.setPCIntesity(95551.1171875);
        sp1.setPCMass(355.913055419922);        
        sp1.setRtTime(1699.8885);
        sp1.setScanNumber("874");
        sp1.setTitle("Test_Title1");
        
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(85.39144897, 149.7417449951);
        pk.add(p);
        p=new Peak(92.209198, 143.806930542);
        
        pk.add(p);        
        p=new Peak(94.47766876, 142.0615997314);
        pk.add(p);
        
        sp1.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setPM(355.91305541992);
        k.setPos(0L);
        k.setScanNum("874");
        sp1.setIndex(k);
        
        expResult.add(sp1);
        
        //Spectrum 2
        Spectrum sp2=new Spectrum();
        sp2.setCharge("2+");
        sp2.setFileName("Test_File2");      
        sp2.setPCIntesity(64124.25390625);
        sp2.setPCMass(506.916564941406);        
        sp2.setRtTime(1700.4182);
        sp2.setScanNumber("875");
        sp2.setTitle("Test_Title2");
        
        pk=new ArrayList<>();
        p=new Peak(126.3209991, 159.5963592529);
        pk.add(p);
        p=new Peak(154.0863953, 290.3283996582);
        
        pk.add(p);   
        
        sp2.setPeakList(pk);
        
        k=new IndexKey();
        k.setPM(506.916564941406);
        k.setPos(327L);
        k.setScanNum("875");
        sp2.setIndex(k);
        expResult.add(sp2);
         
        
        ArrayList<Spectrum> result = instance.readAll();
        if(expResult.equals(result)){
            System.out.println("they are equel");
        }
        else{
            System.out.println("they are not equal");
        }
        
       
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expResult.get(0).getFileName(), result.get(0).getFileName());
        assertEquals(expResult.get(0).getPCMass(), result.get(0).getPCMass());
        assertEquals(expResult.get(0).getCharge(), result.get(0).getCharge());
        
        assertEquals(expResult.get(0).getPeakList().size(), result.get(0).getPeakList().size());
        assertEquals(expResult.get(1).getPeakList().size(), result.get(1).getPeakList().size());
        
    }

    /**
     * Test of readPart method, of class MgfReader.
     * @throws java.io.IOException
     */
    public void testReadPart_double_double() throws IOException {
        System.out.println("readPart");
        double precMass = 355.913055419922;
        double error = 0.0;
        
        
        Indexer giExp = new Indexer(new File("C:/pandyDS/testfile.mgf"));
        List<IndexKey> indxList = giExp.generate();
        
        SpectraReader instance = new MgfReader(new File("C:/pandyDS/testfile.mgf"), indxList);
        ArrayList<Spectrum> expResult = new ArrayList<>();
        
        //Spectrum 1
        Spectrum sp1=new Spectrum();
        sp1.setCharge("3+");
        sp1.setFileName("Test_File1");   
        sp1.setPCIntesity(95551.1171875);
        sp1.setPCMass(355.913055419922);        
        sp1.setRtTime(1699.8885);
        sp1.setScanNumber("874");
        sp1.setTitle("Test_Title1");
        
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(85.39144897, 149.7417449951);
        pk.add(p);
        p=new Peak(92.209198, 143.806930542);
        
        pk.add(p);        
        p=new Peak(94.47766876, 142.0615997314);
        pk.add(p);
        
        sp1.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setPM(355.91305541992);
        k.setPos(0L);
        k.setScanNum("874");
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
     * Test of readAt method, of class MgfReader.
     * @throws java.io.IOException
     */
    public void testReadAt() throws IOException {
        System.out.println("readAt");
        Long position = 0L;
        Indexer giExp = new Indexer(new File("C:/pandyDS/testfile.mgf"));
        List<IndexKey> indxList = giExp.generate();
        
        SpectraReader instance = new MgfReader(new File("C:/pandyDS/testfile.mgf"), indxList);
     
        Spectrum expResult=new Spectrum();
        
        expResult.setCharge("3+");
        expResult.setFileName("Test_File1");   
        expResult.setPCIntesity(95551.1171875);
        expResult.setPCMass(355.913055419922);        
        expResult.setRtTime(1699.8885);
        expResult.setScanNumber("874");
        expResult.setTitle("Test_Title1");
        
        ArrayList<Peak> pk=new ArrayList<>();
        Peak p=new Peak(85.39144897, 149.7417449951);
        pk.add(p);
        p=new Peak(92.209198, 143.806930542);
        
        pk.add(p);        
        p=new Peak(94.47766876, 142.0615997314);
        pk.add(p);
        
        expResult.setPeakList(pk);
      
        
        IndexKey k=new IndexKey();
        k.setPM(355.91305541992);
        k.setPos(0L);
        k.setScanNum("874");
        expResult.setIndex(k);
       
        Spectrum result = instance.readAt(position);
        
        
        assertEquals(expResult.getTitle(), result.getTitle());
        assertEquals(expResult.getFileName(), result.getFileName());
        assertEquals(expResult.getPCMass(), result.getPCMass());
        assertEquals(expResult.getCharge(), result.getCharge());        
        assertEquals(expResult.getPeakList().size(), result.getPeakList().size());
    }
    
}
