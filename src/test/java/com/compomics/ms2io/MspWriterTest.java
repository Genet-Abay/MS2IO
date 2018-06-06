/*
 * Copyright 2018 Genet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.compomics.ms2io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Genet
 */
public class MspWriterTest extends TestCase {

    public MspWriterTest(String testName) {
        super(testName);
    }

    /**
     * Test of write method, of class MspWriter.
     */
    public void testWrite() {
        System.out.println("write");
        MspWriter instance = null;
        instance.write();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class MspWriter.
     */
    public void testWrite_Spectrum_BufferedWriter() {
        File file=new File("C:/pandyDS/testfile.msp");
        File decoyFile = new File("C:/pandyDS/testfile_decoy" + ".msp");
        
        
        Indexer gi;
        try {
            gi = new Indexer(file);
            
        List<IndexKey> indxList=gi.generate();
        
        MspReader rd = new MspReader(file, indxList);
        MspWriter wr = new MspWriter(decoyFile);

        BufferedWriter bw = new BufferedWriter(new FileWriter(decoyFile));
        Spectrum spectrum;
        for (IndexKey indx : indxList) {
            Long pos = indx.getPos();
            spectrum = rd.readAt(pos);
            wr.write(spectrum, bw);
        }

      
        } catch (IOException ex) {
            Logger.getLogger(MspWriterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
