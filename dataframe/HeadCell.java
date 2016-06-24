/*
 * *********COPYRIGHT BY JOHN N. DEMOS 2015. ALL RIGHTS RESERVED.********
 */
package com.jewel.core.model;

import java.util.Objects;

/**
 * A head cell is a specific area of the brain (could be a brodmann number, 
 * a surface 1020 cell, etc). 
 * @author Patricio Carranza, Beatriz Carballo
 * @version 3.00.000
 * last update: 12/14/2015
 */
public class HeadCell {
    
    private final String cellName;
    private Double numberValue;
    private String dominantBand;
    
    public HeadCell(String cellName) {
        this.cellName = cellName;        
    }
    
    /**
     * When using this constructor, is expected that the dominant band
     * will not change. In such case, the dominant band is the same as the
     * cellName. That way we don't have to use different painters for
     * different use cases.
     * @param cellName
     * @param number 
     */
    public HeadCell(String cellName, Double number) {
        this.cellName = cellName;
        this.setNumberValue(number);        
    }
    
    public HeadCell(String cellName, Double number, String dominantBand) {
        this.cellName = cellName;
        this.setNumberValue(number);
        this.dominantBand = dominantBand;
    }
    
    /**
     * Should be used only when the dominant band will not change in time.
     * @param cellName
     * @param permanentDominantBand 
     */
    public HeadCell(String cellName, String permanentDominantBand) {
        this.cellName = cellName;
        this.dominantBand = permanentDominantBand;        
    }

    public Double getNumberValue() {
        if(this.numberValue != null && 
           (this.numberValue.isNaN() || this.numberValue.isInfinite()))
            this.numberValue = null;
        return numberValue;
    }

    public void setNumberValue(Double numberValue) {
        if(numberValue != null && 
           (numberValue.isNaN() || numberValue.isInfinite()))
            numberValue = null;
        this.numberValue = numberValue;        
    }

    public String getDominantBand() {
        return dominantBand;
    }

    public void setDominantBand(String dominantBand) {
        this.dominantBand = dominantBand;
    }

    public String getCellName() {
        return cellName;
    }       

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.cellName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) 
            return false;
        
        if (getClass() != obj.getClass()) 
            return false;
        
        final HeadCell other = (HeadCell) obj;
        return Objects.equals(this.cellName, other.cellName);
    }        
}
