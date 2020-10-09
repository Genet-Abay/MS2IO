/*
 * Copyright 2019 Genet.
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
package com.compomics.ms2io.model;
import com.compomics.ms2io.util.Modification_Mass;
import java.io.Serializable;
/**
 *
 * @author Genet
 */

public class Modification implements Serializable{
    
    private int position;
    private final String mode_name;
    private final String aaSymbol;
    private final double massShift;
    private final String UNIMODE_id;
    
    public Modification()
    {
        this.aaSymbol = "";
        this.mode_name="";
        this.position=0;        
        this.massShift=0;
        this.UNIMODE_id = "";
    }
    
    public Modification(int pos, String aa, String name){
        this.aaSymbol = aa;
        this.mode_name=name;
        this.position=pos;        
        this.massShift= Modification_Mass.getMassShift(name);
        this.UNIMODE_id="";
        
    }
    
    public Modification(int pos, String aa, double massShift){
        this.aaSymbol = aa;
        this.position=pos;
        this.massShift=massShift;
        this.mode_name="";     
        this.UNIMODE_id="";
    }
    
    public Modification(int pos, String aa, double massShift, String unimodeId){
        this.aaSymbol = aa;
        this.position=pos;
        this.massShift=massShift;
        this.mode_name="";     
        this.UNIMODE_id=unimodeId;
    }
    
       public Modification(int pos, String aa, String name, String unimodeId){
        this.aaSymbol = aa;
        this.position=pos;
        this.massShift=0;
        this.mode_name="";     
        this.UNIMODE_id=unimodeId;
    }
    
    /**
     * returns the modification site
     * @return 
     */
    public int getModificationPosition(){
        return position;
    }
    
       /**
     * sets the modification site
     * @return 
     */
    public void setModificationPosition(int newPos){
        this.position = newPos;
    }
    
    /**
     * returns type of this modification
     * @return 
     */
    public String getModificationName(){
        return mode_name;
    }
    
     /**
     * returns type of this modification
     * @return 
     */
    public double getModificationMassShift(){
        return massShift;
    }
    
     /**
     * returns symbol of modified aa
     * @return 
     */
    public String getModifiedAA(){
        return aaSymbol;
    }
    
    /**
     * returns the UNIMODE identifier for this specific modification
     * @return 
     */
    public String getUnimodeId(){
        return this.UNIMODE_id;
    }
    

    
       /**
     * get modification as string
     */
    public String getModification(){
        StringBuilder mod=new StringBuilder();
        mod.append(Integer.toString(this.position));
        mod.append(",");
        mod.append(this.aaSymbol);
        mod.append(",");
        if(this.mode_name!="")
            mod.append(this.mode_name);
        else if(this.massShift>0)
            mod.append(Double.toString(this.massShift));
            
        return mod.toString();
    }
    
}
