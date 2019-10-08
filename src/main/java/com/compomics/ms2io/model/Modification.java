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
/**
 *
 * @author Genet
 */

public class Modification {
    
    private final int position;
    private final String mode_name;
    private final String aaSymbol;
    private final double massShift;
    
    public Modification(int pos, String name, String aaSymbol){
        this.aaSymbol = aaSymbol;
        this.mode_name=name;
        this.position=pos;
        
        this.massShift= Modification_Mass.getMassShift(name);
        
    }
    
    public Modification(int pos, double massShift, String aaSymbol){
        this.aaSymbol = aaSymbol;
        this.position=pos;
        this.massShift=massShift;
        this.mode_name="";        
    }
    
    /**
     * returns the modification site
     * @return 
     */
    public int getModificationPosition(){
        return position;
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
     * get modification as string
     */
    public String getModification(){
        StringBuilder mod=new StringBuilder();
        mod.append(Integer.toString(this.position));
        mod.append(",");
        mod.append(this.aaSymbol);
        mod.append(",");
        mod.append(this.mode_name);
    
        
        return mod.toString();
    }
    
}
