package com.compomics.ms2io;

import java.util.ArrayList;

/**
 *
 * @author Genet
 */
public class Spectrum {
    
     /**
     * Spectrum title.
     */
    private String title;
    
    /**
     * Molecular weight of the peptide. Only msp file format
     */
    private double mw;
    
    /**
     * Spectrum title.
     */
    private String charge;
    
    /**
     * precursor mass.
     */
    private double pcMass;
    
    /**
     * precursor intensity.
     */
    private double pcIntensity;
    
    /**
     * Spectrum file name.
     */
    private String fileName;
 
    /**
     * peaks of the spectrum sorted in ascending order of mz value
     */    
    private ArrayList<Peak> peakList;
    
    /**
     * Scan number or range.
     */
    private String scanNumber="0";
    
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
     * Returns the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
     * set peak list
     * @param peaklist
     */
    public void setPeakList(ArrayList peaklist) {
        this.peakList=peaklist;
    }
    /**
     * Returns the peak list.
     *
     * @return the peak list
     */
    public ArrayList<Peak> getPeakList() {
        return peakList;
    }
    
     /**
     * Returns the peak list as double.The first represents mz and the second for intensity
     *
     * @return the peak list as double
     */
    public double[][] getPeakListDouble() {
        
        int len=this.peakList.size();
        int c=0;
        double[][] peakDouble=new double[len][len];
        for(Peak p : this.peakList){
            peakDouble[0][c]=p.getMz();
            peakDouble[1][c]=p.getIntensity();
            c++;
        }
        return peakDouble;
    }
    
    /**
     * returns minimum Intensity
     * @return 
     */
    public double getMinIntensity(){
        double[][] p=this.getPeakListDouble();
        int len=this.peakList.size();
        double min=Double.MAX_VALUE;
        for(int a=0;a<len;a++){
            if(min>p[1][a]){
                min=p[1][a];
            }
        }
        
        return min;
    }
    
    /**
     * returns the maximum Intensity
     * @return 
     */
     public double getMaxIntensity(){
        double[][] p=this.getPeakListDouble();
        int len=this.peakList.size();
        double max=Double.MIN_VALUE;
        for(int a=0;a<len;a++){
            if(max < p[1][a]){
                max=p[1][a];
            }
        }
        
        return max;
    }
    
    
    
    /**
     * returns minimum mass to charge ratio
     * @return 
     */
    public double getMinMZ(){
        double[][] p=this.getPeakListDouble();
        int len=this.peakList.size();
        double min=Double.MAX_VALUE;
        for(int a=0;a<len;a++){
            if(min>p[0][a]){
                min=p[0][a];
            }
        }
        
        return min;
    }
    
    /**
     * returns the maximum mass over charge ratio
     * @return 
     */
     public double getMaxMZ(){
        double[][] p=this.getPeakListDouble();
        int len=this.peakList.size();
        double max=Double.MIN_VALUE;
        for(int a=0;a<len;a++){
            if(max < p[0][a]){
                max=p[0][a];
            }
        }
        
        return max;
    }
    
     public void setMW(double mw){
        this.mw=mw;
    }
    
    public double getMW(){
        return this.mw;
    }
    
      public void setNumPeaks(int numPeaks){
        this.numPeaks=numPeaks;
    }
    
    public int getNumPeaks(){
        if(Double.compare(this.numPeaks, 0.0) <= 0){
            this.numPeaks=this.peakList.size();
        }
        return this.numPeaks;
    }
    
    public void setPCMass(double pcm){
        this.pcMass=pcm;
    }
    
    public double getPCMass(){
        return this.pcMass;
    }
    
    public void setPCIntesity(double pcI){
        this.pcIntensity=pcI;
        
    }
    
    public double getPCIntensity(){
        return this.pcIntensity;
    }
    
    public void setCharge(String ch){
        this.charge=ch;
        
    }
    
    public String getCharge(){
        return this.charge;
    }
     
     

   
}
