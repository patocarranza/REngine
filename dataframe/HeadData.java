/*
 * *********COPYRIGHT BY JOHN N. DEMOS 2015. ALL RIGHTS RESERVED.********
 */
package com.jewel.core.model;

import com.jewel.core.exceptions.*;
import com.jewel.core.interfaces.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import org.rosuda.REngine.Rserve.RserveException;


/**
 * This class contains all the data of a single head (could be a brodmann head, 
 * power head, etc).
 * @author Patricio Carranza, Beatriz Carballo
 * @version 1.04.000
 * Last update: 12/14/2015
 */
public class HeadData implements JewelMap {
    
    //For any type of head (min, max, training, power1020, assymetry, coherence, etc)
    private final HeadDataStrategy strategy;
    private LinkedHashMap<String, HeadCell> headCellsMap;
    
    private HeadType headType;
    /**
    * The different HeadData possible
    */
    public enum HeadType {
        D1, D, T, T1, T2, A, A1, A2, LoB, B, B1, B2, B3, B4, B5, B6,
        HiB, G, PLUS, MINUS, TRAINING, SLOW_WAVES, FAST_WAVES, REWARDS, 
        DeltaBeta, ThetaBeta, AlphaBeta;
        
        @Override
        public String toString() {
            switch(this) {
                case D1: return "Delta 1";
                case D: return "Delta";
                case T: return "Theta";
                case T1: return "Theta 1";
                case T2: return "Theta 2";
                case A: return "Alpha";
                case A1: return "Alpha 1";
                case A2: return "Alpha 2";
                case LoB: return "Low Beta";
                case B: return "Beta";
                case B1: return "Beta 1";
                case B2: return "Beta 2";
                case B3: return "Beta 3";
                case B4: return "Beta 4";
                case B5: return "Beta 5";
                case B6: return "Beta 6";
                case HiB: return "High Beta";
                case G: return "Gamma";
                case PLUS: return "Plus Head";
                case MINUS: return "Minus Head";
                case TRAINING: return "Training Head";
                case SLOW_WAVES: return "Slow Waves";
                case FAST_WAVES: return "Fast Waves";
                case REWARDS: return "Rewards";
                case DeltaBeta: return "D/B";
                case ThetaBeta: return "T/B";
                case AlphaBeta: return "A/B";
                default: throw new IllegalArgumentException();
            }
        }
    }    
    
            
    /**
     * When using this constructor, don't EVER use the loadData() method!!!
     * IT WILL THROW AN NPE!!
     */
    public HeadData() {
        this.strategy = null;
        this.headCellsMap = new LinkedHashMap<>(80);
    }
    
    public HeadData(HeadDataStrategy strategy, HeadType headType) {         
        this.strategy = strategy;     
        this.headType = headType;
        
        //In case of an empty brodmann head, we load the data
        //right away because it has no sense to load it from outside 
        //when it doesn't depend of an R script.
        if(strategy instanceof EmptyHeadStrategy )
            try { this.loadData(); } catch(Exception ex) { /*will never fail*/}                    
    }
    
    public HeadType getHeadType() {
        return this.headType;
    }
    
    /**
     * Loads the data of each brodmann of this head. 
     * The data to be loaded depends on the HeadDataStrategy referenced. 
     * This HeadDataStrategy contains the algorithm to obtain all the data.
     * @throws RserveStartupException
     * @throws RserveLibraryNotFoundException
     * @throws IOException
     * @throws REnvironmentNotFoundException
     * @throws RserveException
     * @throws RScriptChangedException     
     * @throws Exception 
     */
    public void loadData() throws RserveStartupException, RserveLibraryNotFoundException,
                                   IOException, REnvironmentNotFoundException, RserveException,
                                   RScriptChangedException, Exception {
        
        this.headCellsMap = new LinkedHashMap<>(80);        
        //DO NOT CREATE THE MAP AGAIN IN THE STRATEGY!!!!!!!!
        strategy.loadData(this.headCellsMap);
    }   
    
    @Override
    public LinkedHashMap<String, HeadCell> getMap() {
        return this.headCellsMap;
    }
    
    
    public HeadCell getHeadCell(String headCellName) {
        return this.headCellsMap.get(headCellName);
    }                
    
    /**
     * This method should be used to overwrite an existing value.
     * @param key the name of the headCell (overwrites an existing one)
     * @param headCell the new value.
     */
    public void setHeadCell(String key, HeadCell headCell) {
        this.headCellsMap.put(key, headCell);        
    }
}
