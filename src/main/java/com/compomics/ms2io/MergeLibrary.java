/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.ms2io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genet
 */
public class MergeLibrary {

    private final File libraryFile1;
    private final File libraryFile2;
    private final File mergedLibrary;

    public MergeLibrary(File f1, File f2, File f3) {
        this.libraryFile1 = f1;
        this.libraryFile2 = f2;
        this.mergedLibrary = f3;
    }

    public void Start() {

        BufferedWriter buffwr = null;
        SpectraWriter wr;
        try {

            buffwr = new BufferedWriter(new FileWriter(this.mergedLibrary, true));
            wr = new MspWriter(this.mergedLibrary);

            Merge mrg1 = new Merge(wr, buffwr, this.libraryFile1);
            Merge mrg2 = new Merge(wr, buffwr, this.libraryFile2);

            ExecutorService exec = Executors.newFixedThreadPool(2);
            Future future11 = exec.submit(mrg1);
            Future future12 = exec.submit(mrg2);
            future11.get();
            future12.get();
            exec.shutdown();

        } catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(MergeLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                buffwr.close();
            } catch (IOException ex) {
                Logger.getLogger(MergeLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class Merge implements Runnable {

        final SpectraWriter wr;
        final BufferedWriter bffWriter;
        final File file;
        final Object lock = new Object();

        Merge(SpectraWriter wr, BufferedWriter buffWr, File file) {
            this.wr = wr;
            this.file = file;
            this.bffWriter = buffWr;
        }

        @Override
        public void run() {

            try {
                Indexer gi;
                gi = new Indexer(file);
                List<IndexKey> indxList = gi.generate();

                SpectraReader rd = null;

                if (this.file.getName().endsWith("msp")) {
                    rd = new MspReader(this.file, indxList);

                } else if (this.file.getName().endsWith("mgf")) {
                    rd = new MgfReader(this.file, indxList);
                }
                Spectrum spectrum;

                for (IndexKey key : indxList) {

                    synchronized (lock) {
                        Long pos = key.getPos();
                        spectrum = rd.readAt(pos);
                        this.wr.write(spectrum, this.bffWriter);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }

        }

    }

}
