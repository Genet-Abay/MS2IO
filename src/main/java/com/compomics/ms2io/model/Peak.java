package com.compomics.ms2io.model;

import java.io.Serializable;

/**
 *
 * @author Genet
 */
public class Peak implements Comparable<Peak>, Serializable {
    
    
    /**
     * m/z value of the peak
     */
    private double mz;
   
    /**
     * intensity of the peak.
     */
    private double intensity;
    
    /**
     * annotation of the peak
     */
    private String annotation = "";

    
     /**
     * class constructor
     *
     * @param mz the m/z value of the peak
     * @param intensity the intensity of the peak
     * @param annotation peak annotation
     */
    public Peak(double mz, double intensity, String annotation) {
        this.mz = mz;
        this.intensity = intensity;
        this.annotation=annotation;
    }

    /**
     * Returns true if the peak has the same m/z and intensity.
     *
     * @param p the peal to compare this peak to
     * @return true if the peak has the same m/z and intensity
     */
    public boolean isEqual(Peak p) {
      
        return mz == p.mz && intensity == p.intensity;
    }



    /**
     * Returns the mz.
     *
     * @return the mz
     */
    public double getMz() {
        return mz;
    }

    /**
     * Set the mz.
     *
     * @param mz the value to set
     */
    public void setMz(double mz) {
        this.mz = mz;
    }

    /**
     * Returns the intensity.
     *
     * @return the intensity
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Set the intensity.
     *
     * @param intensity the intensity to set
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    
    
      /**
     * Returns the annotation.
     *
     * @return the annotation of this peak
     */
    public String getPeakAnnotation() {
        return annotation;
    }

    /**
     * Set the annotation.
     *
     * @param annotation to set
     */
    public void setPeakAnnotation(String annotation) {
        this.annotation = annotation;
    }
    
    /**
     * compares given peak to current peak
     * @param t given peak
     * @return int greater than 0 if current peak greater than given peak,
     * 0 if they are equal and int less than 0 if it is less than the given peak
     * 
     */
    @Override
    public int compareTo(Peak t) {
        
        return Double.compare(this.mz, t.mz);
    }

 

}
