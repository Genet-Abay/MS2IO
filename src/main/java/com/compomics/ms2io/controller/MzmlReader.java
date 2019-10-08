package com.compomics.ms2io.controller;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.zip.DataFormatException;
//import java.util.zip.Inflater;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.*;
//import uk.ac.ebi.pride.tools.jmzreader.JMzReader;
//import uk.ac.ebi.pride.tools.jmzreader.model.Spectrum;
//import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
//import uk.ac.ebi.pride.tools.mzml_wrapper.MzMlWrapper;

/**
 *
 * @author Genet
 */
public class MzmlReader {

//    private final File file;
//    private ArrayList<Spectrum> spectra;
//    
//
//    public MzmlReader(File file) {
//        this.file = file;
//        spectra = new ArrayList<>();
//    }
//
//    public ArrayList<Spectrum> readAll() throws XMLStreamException, FileNotFoundException, JMzReaderException {
//
//
//        JMzReader jmzreader = new MzMlWrapper(new File("/path/to/55merge.mgf"));
//        jmzreader.acceptsFile();
//        Iterator<Spectrum> itr=jmzreader.getSpectrumIterator();
//        ArrayList<Spectrum> specList=new ArrayList<>();
//        
//        //converting pride spectrum to compomics spectrum
//        Spectrum specCp=new Spectrum();
//        for(itr.hasNext()){
//            Spectrum spec=itr.next();
//            spec.getAdditional().
//            
//            
//        }
//        
//       
//        
//        parse();
//        return spectra;
//       
//    }
//
//    private void parse() throws XMLStreamException, FileNotFoundException {
//
//        XMLInputFactory factory = XMLInputFactory.newInstance();
//        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
//        //xml file handler to access the tags
//        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(this.file));
//
//        boolean run, spectrumList, spectrum, binaryDataArrayList, binaryDataArray, binary;
//        boolean pcM, pcInt, msLevel;
//        boolean mz, intensity;
//        mz = false;
//        intensity = false;
//        pcM = pcInt = msLevel = false;
//        run = spectrumList = spectrum = binaryDataArrayList = binaryDataArray = binary = false;
//
//        Spectrum spec = new Spectrum();
//
//        float[] mzData = null;
//        float[] intData = null;
//
//        while (eventReader.hasNext()) {
//
//            XMLEvent event = eventReader.nextEvent();
//
//            //check for start tag
//            if (event.isStartElement()) {
//                StartElement element = (StartElement) event;
//
//                // Checking for tag's name
//                if (element.getName().toString().equalsIgnoreCase("cvParam")) {
//
//                    if (binaryDataArray) {
//
//                        Iterator<Attribute> iterator = element.getAttributes();
//                        while (iterator.hasNext()) {
//                            Attribute attribute = iterator.next();
//                            QName name = attribute.getName();
//                            if (name.toString().equals("name")) {
//                                String value = attribute.getValue();
//                                if (value.equals("m/z array")) {
//                                    mz = true;
//                                    intensity = false;
//                                } else if (value.equals("intensity array")) {
//                                    mz = false;
//                                    intensity = true;
//                                }
//
//                            }
//                        }
//
//                    } else if (spectrum && !binaryDataArrayList) {
//                        //Iterating attributes of cvParam for spectrum header
//                        Iterator<Attribute> iterator = element.getAttributes();
//                        while (iterator.hasNext()) {
//                            Attribute attribute = iterator.next();
//                            QName name = attribute.getName();
//                            if (name.toString().equals("name")) {
//                                String value = attribute.getValue();
//                                switch (value) {
//                                    case "base peak m/z":
//                                        pcM = true;
//                                        break;
//                                    case "base peak intensity":
//                                        pcInt = true;
//                                        break;
//                                    case "ms level":
//                                        msLevel = true;
//                                        break;
//                                    default:
//                                        break;
//                                }
//
//                            } else if (name.toString().equals("value")) {
//                                if (pcM) {
//                                    String value = attribute.getValue();
//                                    //System.out.println("reading prec mass");
//                                    spec.setPCMass(Double.parseDouble(value));
//                                    pcM = false;
//                                } else if (pcInt) {
//                                    String value = attribute.getValue();
//                                    //System.out.println("reading prec intensity");
//                                    spec.setPCIntesity(Double.parseDouble(value));
//                                    pcInt = false;
//                                } else if (msLevel) {
//                                    String value = attribute.getValue();
//                                    int level = Integer.parseInt(value);
//                                    //System.out.println("reading ms-level");
//                                    if (level != 2) {
//                                        System.out.println("only ms-level 2 supported");
//                                        spectra = null;
//                                        break;
//                                    }
//                                    msLevel = false;
//                                }
//
//                            }
//
//                        }
//                    }
//
//                } else if (element.getName().toString().equalsIgnoreCase("binaryDataArray")) {
//                    if (binaryDataArray) {
//                        System.out.println("File is corrupted! check the file, end tag missing for binaryDataArray");
//                        spectra = null;
//                        break;
//                    }
//
//                    binaryDataArray = true;
//                } else if (element.getName().toString().equalsIgnoreCase("binary")) {
//                    if (binary) {
//                        System.out.println("File is corrupted! check the file, end tag missing for binary");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("inside binary");
//                    binary = true;
//
//                } else if (element.getName().toString().equalsIgnoreCase("binaryDataArrayList")) {
//                    if (binaryDataArrayList) {
//                        System.out.println("File is corrupted! check the file, end tag missing for binaryDataArrayList");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("inside binarydataArraylist");
//                    binaryDataArrayList = true;
//
//                } else if (element.getName().toString().equalsIgnoreCase("spectrum")) {
//                    if (spectrum) {
//                        System.out.println("File is corrupted! check the file, end tag missing for spectrum");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("inside spectrum");
//                    Iterator<Attribute> iterator = element.getAttributes();
//                    while (iterator.hasNext()) {
//                        Attribute attribute = iterator.next();
//                        QName name = attribute.getName();
//                        if (name.toString().equals("id")) {
//                            String value = attribute.getValue();
//                            spec.setScanNumber(value);
//                        }
//                    }
//                    spec = new Spectrum();
//                    spectrum = true;
//                }
//                if (element.getName().toString().equalsIgnoreCase("spectrumList")) {
//                    if (spectrumList) {
//                        System.out.println("File is corrupted! check the file, end tag missing for spectrumList");
//                        spectra = null;
//                        break;
//                    }
//
//                    //System.out.println("inside spectrumList");
//                    spectrumList = true;
//                }
//                if (element.getName().toString().equalsIgnoreCase("run")) {
//                    if (run) {
//                        System.out.println("File is corrupted! check the file, end tag missing for run");
//                        spectra = null;
//                        break;
//                    }
//                    System.out.println("inside run");
//                    run = true;
//                }
//            }
//
//            // This will be triggered when the tag is of type </...>
//            if (event.isEndElement()) {
//                EndElement element = (EndElement) event;
//
//                // Checking which tag needs to be closed after reading.
//                // If the tag matches then the boolean of that tag is
//                // set to be false.
//                if (element.getName().toString().equalsIgnoreCase("binaryDataArrayList")) {
//                    if (!binaryDataArrayList) {
//                        System.out.println("File is corrupted! check the file, start tag missing for binaryDataArrayList");
//                        spectra = null;
//                        break;
//                    }
//                    binaryDataArrayList = false;
//                    if (mzData != null && intData != null) {
//                        if (mzData.length != intData.length) {
//                            System.out.println("Error, unequal size of mz and intensity value.");
//                            break;
//                        }
//
//                        int peaksLen = mzData.length;
//                        ArrayList<Peak> peaks = new ArrayList<>();
//                        for (int a = 0; a < peaksLen; a++) {
//
//                            Peak pk = new Peak(mzData[a], intData[a]);
//                            peaks.add(pk);
//                        }
//
//                        spec.setNumPeaks(peaksLen);
//                        spec.setPeakList(peaks);
//                    }
//
//                } else if (element.getName().toString().equalsIgnoreCase("binary")) {
//                    if (!binary) {
//                        System.out.println("File is corrupted! check the file, end tag missing for binary");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("closing binary");
//                    binary = false;
//
//                }
//                if (element.getName().toString().equalsIgnoreCase("binaryDataArray")) {
//                    if (!binaryDataArray) {
//                        System.out.println("File is corrupted! check the file, start tag missing for binaryDataArray");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("closing binarydataArray");
//                    binaryDataArray = false;
//                    //add intensity or m/x to peak
//                }
//                if (element.getName().toString().equalsIgnoreCase("spectrum")) {
//                    if (!spectrum) {
//                        System.out.println("File is corrupted! check the file, start tag missing for spectrum");
//                        spectra = null;
//                        break;
//                    }
//                    //System.out.println("closing spectrum");
//                    spectrum = false;
//                    spectra.add(spec);
//                }
//                if (element.getName().toString().equalsIgnoreCase("spectrumList")) {
//                    if (!spectrumList) {
//                        System.out.println("File is corrupted! check the file, start tag missing for spectrumList");
//                        spectra = null;
//                        break;
//                    }
//                    spectrumList = false;
//                    System.out.println("closing spectrumList, reading spectrum completed");
//                    //can break the iterator safly as the spectrum reading completed here
//                    //break;
//                }
//                if (element.getName().toString().equalsIgnoreCase("run")) {
//                    if (!run) {
//                        System.out.println("File is corrupted! check the file, start tag missing for run");
//                        spectra = null;
//                        break;
//                    }
//                    System.out.println("closing run, completed spectrum part of the file");
//                    run = false;
//                    break;
//                }
//            }
//
//            // Triggered when there is data after the tag which is
//            // currently opened.
//            if (event.isCharacters()) {
//                // Depending upon the tag opened the data is retrieved .
//                Characters element = (Characters) event;
//                if (binary && binaryDataArray && spectrum && spectrumList && run) {
//
//                    String temp = element.getData();
//                    float[] theData = null;
//                    try {
//                        theData = getMzValues(temp);
//
//                    } catch (Exception ex) {
//                        Logger.getLogger(MzmlReader.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    if (mz && theData != null) {
//                        mzData = theData;
//                    } else if (intensity && theData != null) {
//                        intData = theData;
//                    }
//
//                }
//
//            }
//        }
//    }
//
//    private float[] getMzValues(String input) throws Exception {
//
//         //Decode bytes from base-64. These are compressed.  converting to 8 bit signed        
//        byte[] binArray = Base64.decode(input);//new byte[]{120,218,251,240,220,193,121,245,167,102,103,0,23,115,4,190}
//
//        //convert to little endian order
//        ByteBuffer bbuf = ByteBuffer.allocate(binArray.length);
//        bbuf.put(binArray);
//        binArray = bbuf.order(ByteOrder.LITTLE_ENDIAN).array();
//
//        /**
//         * Decompress from zlib.
//         */
//        byte[] decompressedData = null;
//        {
//            Inflater decompressor = new Inflater();
//            decompressor.setInput(binArray);
//
//            // Create an expandable byte array to hold the decompressed data
//            ByteArrayOutputStream bos = null;
//
//            try {
//
//                bos = new ByteArrayOutputStream(binArray.length);
//
//                // Decompress the data
//                byte[] buf = new byte[1024];
//                while (!decompressor.finished()) {
//                    int count = decompressor.inflate(buf);
//                    bos.write(buf, 0, count);
//                }
//
//            } finally {
//                try {
//                    bos.close();
//                } catch (Exception nope) {
//                    /* This exception doesn't matter */ }
//            }
//
//            decompressedData = bos.toByteArray();
//        }
//
//        /**
//         * STEP 4: Read floats from IEEE 754 floating-point "single format"
//         * representations
//         */
//        final int totalFloats = decompressedData.length / 4;
//        float[] floatValues = new float[totalFloats];
//
//        // Iterate until parse each float
//        int floatIndex = 0;
//        for (int nextFloatPosition = 0; nextFloatPosition < decompressedData.length; nextFloatPosition += 4) {
//            // Read in the bytes
//            char c1 = (char) decompressedData[nextFloatPosition + 0];
//            char c2 = (char) decompressedData[nextFloatPosition + 1];
//            char c3 = (char) decompressedData[nextFloatPosition + 2];
//            char c4 = (char) decompressedData[nextFloatPosition + 3];
//
//            // Bitwise AND to make sure only first 2 bytes are included
//            int b1 = (int) (c1 & 0xFF);
//            int b2 = (int) (c2 & 0xFF);
//            int b3 = (int) (c3 & 0xFF);
//            int b4 = (int) (c4 & 0xFF);
//
//            // Build the four-byte floating-point "single  format" representation
//            int intBits = (b4 << 0) | (b3 << 8) | (b2 << 16) | (b1 << 24);
//
//            floatValues[floatIndex] = Float.intBitsToFloat(intBits);
//
//            // Increment counter used to populate array
//            floatIndex++;
//        }
//
//        return floatValues;
//    }
//
}
