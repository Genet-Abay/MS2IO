package com.compomics.ms2io.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Genet
 */
public class Spectrum implements Serializable {

    /**
     * Index of this spectrum
     */
    private IndexKey indx;

    public Spectrum() {

        this.indx = new IndexKey();
        this.protein = "";
        this.title = "";
        this.sequence = "";
        this.mw = 0;
        this.charge = new Charge(1, "+");
        this.pcMass = 0;
        this.pcIntensity = 0;
        this.comment = "";
        this.peakList = new ArrayList<>();
        this.numPeaks = 0;
        this.rtTime = 0;
        this.scanNumber = "";
        this.minMZ = 0;
        this.maxMZ = 0;
        this.minIntensity = 0;
        this.maxIntensity = 0;
        modifications = new ArrayList<>();

    }

    /**
     * Spectrum title.
     */
    private String title;

    /**
     * Sequence if it is given
     */
    private String sequence;

    /**
     * protein name of the spectrum if given
     */
    private String protein = "";

    /**
     * Molecular weight of the peptide. Only msp file format
     */
    private double mw;

    /**
     * Retention time
     */
    private double rtTime = 0.0;

    /**
     * Spectrum title.
     */
    private Charge charge;

    /**
     * precursor mass.
     */
    private double pcMass;

    /**
     * precursor intensity.
     */
    private double pcIntensity;

    /**
     * set only if the spectrum is msp format otherwise it is null
     */
    private String comment;

    /**
     * peaks of the spectrum sorted in ascending order of mz value
     */
    private ArrayList<Peak> peakList;

    /**
     * Scan number or range.
     */
    private String scanNumber = "";

    /**
     * number of peaks in the spectrum.
     */
    private int numPeaks;

    /**
     * minimum m/z value of the spectrum
     */
    private double minMZ;

    /**
     * maximum m/z value of the spectrum
     */
    private double maxMZ;
    /**
     * minimum intensity value of the spectrum
     */
    private double minIntensity;

    /**
     * maximum intensity value of the spectrum
     */
    private double maxIntensity;

    /**
     * number modification
     */
    private List<Modification> modifications;

    /**
     * Getter for the scan number.
     *
     * @return the spectrum scan number
     */
    public String getScanNumber() {
        return scanNumber;
    }

    /**
     * Setter for the scan number or range.
     *
     * @param scanNumber or range
     */
    public void setScanNumber(String scanNumber) {
        this.scanNumber = scanNumber;
    }

    /**
     *
     * @param m modification to add to list of modification for this spectrum
     */
    public void setModification(List<Modification> m) {
        this.modifications = m;
    }

    /**
     * return list of modification for this spectrum
     *
     * @return
     */
    public List<Modification> getModifications() {
        return this.modifications;
    }

    /**
     * return list of modification for this spectrum
     *
     * @return
     */
    public String getModifications_asStr() {
        StringBuilder strb = new StringBuilder();
        if (this.modifications != null) {            
            strb.append(Integer.toString(this.modifications.size()));
            for (Modification m : this.modifications) {
                strb.append("/");
                strb.append(m.getModification());
            }
        }
        return strb.toString();
    }

    /**
     * Returns Title
     *
     * @return Title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the spectrum title.
     *
     * @param title the file name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns comment
     *
     * @return comment, if it is mgf the value is ""
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Sets the comment field of the spectrum if it is msp file format.
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns sequence
     *
     * @return sequence
     */
    public String getSequence() {
        return this.sequence;
    }

    /**
     * Returns protein name
     *
     * @return protein
     */
    public String getProtein() {
        return this.protein;
    }

    /**
     * Sets the name of the protein
     *
     * @param protein
     */
    public void setProtein(String protein) {
        this.protein = protein;
    }

    /**
     * Sets the spectrum sequence.
     *
     * @param sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * set peak list
     *
     * @param peaklist
     */
    public void setPeakList(ArrayList peaklist) {
        this.peakList = peaklist;
    }

    /**
     * Returns the peak list.
     *
     * @return the peak list
     */
    public ArrayList<Peak> getPeakList() {
        return peakList;
    }

    public void setMW(double mw) {
        this.mw = mw;
    }

    public double getMW() {
        return this.mw;

    }

    public double getRtTime() {
        return this.rtTime;

    }

    public void setRtTime(double rt) {
        this.rtTime = rt;
    }

    public void setNumPeaks(int numPeaks) {
        this.numPeaks = numPeaks;
    }

    public int getNumPeaks() {
        if (Double.compare(this.numPeaks, 0.0) <= 0) {
            try {
                this.numPeaks = this.peakList.size();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return this.numPeaks;
    }

    public void setPCMass(double pcm) {
        this.pcMass = pcm;
    }

    public double getPCMass() {
        return this.pcMass;
    }

    public void setPCIntesity(double pcI) {
        this.pcIntensity = pcI;

    }

    public double getPCIntensity() {
        return this.pcIntensity;
    }

    public void setCharge(String ch) {
        String sign = "";
        int value = 1;
        if (!"".equals(ch)) {
            if (ch.contains("-")) {
                sign = "-";
            }else if (ch.contains("+")) {
                sign = "+";
            }
            ch = ch.replaceAll("[^\\d]", "");
            try {
                value = Integer.parseInt(ch);
            } catch (Exception ex) {

                System.out.println("conversion error - parse string to int");
            }

            this.charge = new Charge(value, sign);
        }

    }

    /**
     * get charge of this spectrum
     *
     * @return
     */
    public Charge getCharge() {
        return this.charge;
    }

    /**
     * get charge of this spectrum in string format
     *
     * @return
     */
    public String getCharge_asStr() {
        return (this.charge.getCharge());
    }

    /**
     * Returns the peak list as double.The first represents mz and the second
     * for intensity
     *
     * @return the peak list as double
     */
    public double[][] getPeakListDouble() {

        double[][] peakDouble = new double[0][0];
        try {
            int len = this.peakList.size();
            int c = 0;
            peakDouble = new double[len][2];

            for (Peak p : this.peakList) {
                peakDouble[c][0] = p.getMz();
                peakDouble[c][1] = p.getIntensity();
                c++;
            }

        } catch (Exception ex1) {

            System.out.println(ex1);
        }

        return peakDouble;
    }

    /**
     * Returns the MZ values as double array.
     *
     * @return m/z as double
     */
    public double[] getMZDouble() {
        double[] mzDouble = new double[0];
        try {
            int len = this.peakList.size();
            int c = 0;
            mzDouble = new double[len];
            for (Peak p : this.peakList) {
                mzDouble[c++] = p.getMz();

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return mzDouble;
    }

    /**
     * Returns the Intensity values as double array.
     *
     * @return intensity as double array
     */
    public double[] getIntensityDouble() {
        double[] intensityDouble = new double[0];
        try {
            int len = this.peakList.size();
            int c = 0;
            intensityDouble = new double[len];
            for (Peak p : this.peakList) {
                intensityDouble[c++] = p.getIntensity();

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return intensityDouble;
    }

    /**
     * returns minimum Intensity
     *
     * @return
     */
    public double getMinIntensity() {
        try {
            if (this.minIntensity != 0) {
                return this.minIntensity;
            }
            double[][] p = this.getPeakListDouble();
            int len = this.peakList.size();
            this.minIntensity = Double.MAX_VALUE;
            for (int a = 0; a < len; a++) {
                if (this.minIntensity > p[a][1]) {
                    this.minIntensity = p[a][1];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return this.minIntensity;
    }

    public void setMinIntensity(double minInt) {
        this.minIntensity = minInt;

    }

    /**
     * returns the maximum Intensity
     *
     * @return
     */
    public double getMaxIntensity() {
        try {
            if (this.maxIntensity != 0) {
                return this.maxIntensity;
            }
            double[][] p = this.getPeakListDouble();
            int len = this.peakList.size();
            this.maxIntensity = Double.MIN_VALUE;
            for (int a = 0; a < len; a++) {
                if (this.maxIntensity < p[a][1]) {
                    this.maxIntensity = p[a][1];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return this.maxIntensity;
    }

    public void setMaxIntensity(double maxInt) {
        this.maxIntensity = maxInt;

    }

    /**
     * returns minimum mass to charge ratio
     *
     * @return
     */
    public double getMinMZ() {
        try {
            double[][] p = this.getPeakListDouble();
            int len = this.peakList.size();
            this.minMZ = Double.MAX_VALUE;
            double currentMz;
            for (int a = 0; a < len; a++) {
                currentMz = p[a][0];
                if (this.minMZ > currentMz) {
                    this.minMZ = currentMz;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);

        }

        return this.minMZ;
    }

    public void setMaxMz(double maxMz) {
        this.maxMZ = maxMz;

    }

    /**
     * returns the maximum mass over charge ratio
     *
     * @return
     */
    public double getMaxMZ() {
        try {
            double[][] p = this.getPeakListDouble();
            int len = this.peakList.size();
            this.maxMZ = Double.MIN_VALUE;
            double currentMz;
            for (int a = 0; a < len; a++) {
                currentMz = p[a][0];
                if (this.maxMZ < currentMz) {
                    this.maxMZ = currentMz;
                }
            }
        } catch (Exception ex) {

            System.out.println(ex);
        }

        return this.maxMZ;
    }

    public void setMinMz(double minMz) {
        this.minMZ = minMz;

    }

    /**
     * set Index of the spectrum
     *
     * @param k the key to be assigned
     */
    public void setIndex(IndexKey k) {
        this.indx = k;
    }

    /**
     * returns index of the spectrum
     *
     * @return index key
     */
    public IndexKey getIndex() {
        return this.indx;
    }
}
