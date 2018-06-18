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
import java.nio.CharBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class AppendLibrary {

    final File f1;
    final File f2;

    public AppendLibrary(File originalFile, File filetobeAppended) {

        this.f1 = originalFile;
        this.f2 = filetobeAppended;

    }

    public void Append() {
         BufferedWriter bw=null;
        try {
            Indexer gi;
            gi = new Indexer(f2);
            List<IndexKey> indxList = gi.generate();

            SpectraReader rd=null;           
            SpectraWriter wr=null;

            bw = new BufferedWriter(new FileWriter(f1, true));
            if (this.f1.getName().endsWith("msp")) {
                rd = new MspReader(this.f2, indxList);
                wr = new MspWriter(f1);
            } else if (this.f1.getName().endsWith("mgf")) {
                rd = new MgfReader(this.f2, indxList);
                wr = new MgfWriter(f1);
            }
            Spectrum spectrum;
            for (IndexKey key : indxList) {

                Long pos = key.getPos();
                spectrum = rd.readAt(pos);
                wr.write(spectrum, bw);

            }

        } catch (IOException ex) {
            Logger.getLogger(AppendLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(MspWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
