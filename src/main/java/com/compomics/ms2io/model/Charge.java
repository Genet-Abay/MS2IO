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

/**
 *
 * @author Genet
 */
public class Charge {
    
    /**
     * value of the charge
     */
    private final int charge_val;
    
    /**
     * sign of the charge
     */
    private final String sign;
    
    
    public Charge(int value, String sign){
        this.charge_val=value;
        this.sign=sign;
    }
    
    /**
     * returns the value of this charge
     * @return 
     */
    public int getChargeValue(){
        return this.charge_val;
    }
    
    /**
     * return sign of this charge
     * @return 
     */
    public String getSign(){
        return this.sign;
    }
    
    /**
     * get charge as string
     */
    public String getCharge(){
        StringBuilder chr=new StringBuilder();
        chr.append(Integer.toString(this.charge_val));
        chr.append(this.sign);
        
        return chr.toString();
    }
    
    
}
