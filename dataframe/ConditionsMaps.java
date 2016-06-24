/*
 * *********COPYRIGHT BY JOHN N. DEMOS 2015. ALL RIGHTS RESERVED.********
 */

package com.jewel.core.model;

import com.jewel.core.conditions.scriptsProcessors.ConditionsMapsProcessor;
import com.jewel.core.exceptions.*;
import java.util.*;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * Contains all the data that indicates how each condition 
 * (symptom, network, lobe, etc) relates to a JewelMap. 
 * @author Patricio Carranza, Beatriz Carballo
 * @version 1.02.000
 * Last update: 12/09/2015
 */
public class ConditionsMaps {
    
    //String is the symptom name. Each symptom contains an entire list of brodmanns. For some of them there won't be
    //value, but there will be for others and we will use to compare against the ZScore 
    //in symptoms mode to see if that brodmann should light up or not.
    private Map<String, JewelMap> symptomsMap;
    private Map<String, JewelMap> tripleNetworkMap;
    private Map<String, JewelMap> otherNetworksMap;
    private Map<String, JewelMap> lobesMap;        
    
    //This initializer loads the list of symptoms, networks, lobes, etc, BUT NOT HOW THEY AFFECT EACH JEWELMAP    
    private final ConditionsMapsProcessor mapsInitializer;
         
    //Loads the content of each JewelMap. That content indicates which headCell/ROI should
    //turn on with each condition.
    private final ConditionsMapsProcessor mapsDataFiller;
    

    public ConditionsMaps(ConditionsMapsProcessor mapsInitializer, 
                          ConditionsMapsProcessor mapsDataFiller) {
        
        this.mapsInitializer = mapsInitializer;
        this.mapsDataFiller = mapsDataFiller;
    }
    
    public Collection<String> getSymptomsNames() {
        return symptomsMap.keySet();
    }
    
    public Collection<String> getTripleNetworkNames() {
        return tripleNetworkMap.keySet();
    }
    
    public Collection<String> getOtherNetworksNames() {
        return otherNetworksMap.keySet();
    }
    
    public Collection<String> getLobesNames() {
        return lobesMap.keySet();
    }
    
    public int getSymptomsCount() {
        return symptomsMap.size();
    }
    
    public int getTripleNetworkCount() {
        return tripleNetworkMap.size();
    }
    
    public int getOtherNetworksCount() {
        return otherNetworksMap.size();
    }
    
    public int getLobesCount() {
        return lobesMap.size();
    }
    
    public JewelMap getSymptomJewelMap(String symptomName) {
        return this.symptomsMap.get(symptomName);
    }
    
    public JewelMap getTripleNetworkJewelMap(String tripleNetworkName) {
        return this.tripleNetworkMap.get(tripleNetworkName);
    }
    
    public JewelMap getOtherNetworksJewelMap(String otherNetworkName) {
        return this.otherNetworksMap.get(otherNetworkName);
    }
    
    public JewelMap getLobesJewelMap(String symptomName) {
        return this.lobesMap.get(symptomName);
    }
    
    public Collection<String> getJewelMapKeys() {
        Collection col = null;
        //We take them from symptomsMap, but we could take them
        //from any of the maps
        for(String regstr : this.symptomsMap.keySet()) {
            JewelMap reg = this.symptomsMap.get(regstr);
            col = reg.getMap().keySet();
            break;
        }
        return col;
    }    
        

    /**
     * Loads all the data needed for symptoms, triple network, other network 
     * and lobes to know how they affect jewelmaps.     
     * @throws RserveStartupException
     * @throws RserveLibraryNotFoundException     
     * @throws REnvironmentNotFoundException
     * @throws RserveException
     * @throws RScriptChangedException 
     */
    public void loadMapsData() throws RserveStartupException, RserveLibraryNotFoundException,
                                      REnvironmentNotFoundException, RserveException,
                                      RScriptChangedException, Exception {                
        mapsDataFiller.loadData(symptomsMap, tripleNetworkMap, 
                                otherNetworksMap, lobesMap);           
    }       
    
    /**
     * Initializes the maps. Each map will have the name of each value 
     * (the name of each symptom, the name of each
     * one of the triple networks, etc), BUT WILL NOT HAVE ALL THE 
     * DATA NEEDED TO DISPLAY HOW THE SYMPTOMS, NETWORKS AND LOBES
     * AFFECT JEWELMAPS. You must call the loadMapsData() method for 
     * that data to be loaded.     
     * @throws RserveStartupException
     * @throws RserveLibraryNotFoundException     
     * @throws REnvironmentNotFoundException
     * @throws RserveException
     * @throws RScriptChangedException 
     */
    public void initializeMaps() throws RserveStartupException, RserveLibraryNotFoundException,
                                        REnvironmentNotFoundException, RserveException,
                                        RScriptChangedException, Exception {
        symptomsMap = new LinkedHashMap<>(100);
        tripleNetworkMap = new LinkedHashMap<>(3);
        otherNetworksMap = new LinkedHashMap<>(20);
        lobesMap = new LinkedHashMap<>(20);
                
        mapsInitializer.loadData(symptomsMap, tripleNetworkMap, 
                                         otherNetworksMap, lobesMap);  
    }
}
