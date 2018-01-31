package com.compomics.ms2io;

/**
 *
 * @author Genet
 */
public class IndexKey implements Comparable<IndexKey> {

    /**
     * memory position
     */
    private Long pos;

    /**
     * primary index : name of the spectrum
     */
    private String name;
    /**
     * secondary index: precursor mass
     */
    
    private double pm;
    
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
     * returns name of the spectrum to be indexed
     * @return name of the spectrum
     */
    public String getName() {
        return this.name;
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
        return this.pm;
    }

    
    /**
     * Set position of the spectrum
     * @param pos
     */
    public void setPos(Long pos) {
        this.pos = pos;
    }

    /**
     * Set the name of the spectrum
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
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
        this.pm = pm;
    }
    
    /**
     * Return combined index
     * @return 
     */
    public String getCombinedIndex(){
        
        return (Long.toString(this.pos)+";" + this.name+ ";" + this.scanNum + ";" + Double.toString(this.pm));
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

        return Double.compare(this.pm, t.pm);

    }
}
