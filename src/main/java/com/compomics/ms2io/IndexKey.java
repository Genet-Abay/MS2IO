package com.compomics.ms2io;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Genet
 */
public class IndexKey implements Comparable<IndexKey>, Serializable {
    
   // private static final long SERIALVERSIONUID = 129348938L;
    

    /**
     * memory position
     */
    private long pos;

    /**
     * secondary index: precursor mass
     */
    
    private double precMass;
    
    /**
     * last index: scan number
     */
    
    private String scanNum;

    /**
     * return position representing the spectrum to be indexed
     * @return position
     */
    public Long getPos() {
        return this.pos;
    }

    
    /**
     * Return scan number of the spectrum to be indexed
     * @return scan number
     */
    public String getScanNum(){
        return this.scanNum;
    }

    /**
     * Returns the precursor mass of the spectrum to be indexed
     * @return precursor mass
     */
    public double getPM() {
        return this.precMass;
    }

    
    /**
     * Set position of the spectrum
     * @param pos
     */
    public void setPos(Long pos) {
        this.pos = pos;
    }
    
    /**
     * Set the scan number
     * @param scan 
     */
    public void setScanNum(String scan){
        this.scanNum=scan;
    }

    /**
     * set the precursor mass
     * @param pm 
     */
    public void setPM(double pm) {
        this.precMass = pm;
    }
    
    /**
     * Return combined index
     * @return 
     */
    public String getCombinedIndex(){
        
        return (Long.toString(this.pos)+ ";" + this.scanNum + ";" + Double.toString(this.precMass));
    }

    /**
     * Compares the given index with the current index.
     * It returns 0 if they are equal, 1 if given index is less than the present index
     * and -1 if the given index greater than the current index
     * @param t the index to compare with the current Index key
     * @return 
     */
    @Override
    public int compareTo(IndexKey t) {
        BigDecimal bd1=BigDecimal.valueOf(this.precMass);
        BigDecimal bd2=BigDecimal.valueOf(t.precMass);       
        
        return bd1.compareTo(bd2);

    }
}
