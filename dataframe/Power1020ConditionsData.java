package dataframe;

import com.jewel.core.conditions.scriptsProcessors.ConditionsMapsProcessor;
import com.jewel.core.exceptions.*;
import com.jewel.core.gui.head1020.SurfaceEmptyHeadStrategy;
import com.jewel.core.model.*;
import com.jewel.core.model.HeadData.HeadType;
import com.jewel.core.r.RExecutor;
import java.util.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * Algorithm for loading the head cells number values of the symptoms for DzM
 * Surface Power Fast, Slow and Rewards heads. This data makes possible the
 * functioning of these training heads in symptoms mode.
 * All the symptoms names should have already been loaded by another
 * Script Processor (a MapsInitializer likely).
 * This processor supports User Symptom.
 * @author Patricio Carranza, Beatriz Carballo
 * @version 4.00.000
 * Last update: 04/26/2016
 */
public class Power1020ConditionsData implements ConditionsMapsProcessor {
    
    private static final String processor = "DzM Slow, Fast and Rewards Symptoms Data";
    
    private final HeadType headType;
        
             
    public Power1020ConditionsData(HeadType type) {
        this.headType = type;
    }    
     

    @Override
    public void loadData(Map<String, JewelMap> symptomsMap, 
                         Map<String, JewelMap> tripleNetworkMap, 
                         Map<String, JewelMap> otherNetworksMap,
                         Map<String, JewelMap> lobesMap)
            throws RserveException, RserveStartupException, 
                   RserveLibraryNotFoundException, REnvironmentNotFoundException, 
                   RScriptChangedException, Exception {
        
        RExecutor rexec = new RExecutor();        
        if(this.headType == HeadType.SLOW_WAVES) {
            this.dataframeProcessing(rexec.execFunction("swih.data.numbers"), 
                                     symptomsMap, 
                                     true); 
            this.dataframeProcessing(rexec.execFunction("swih.data.bands"), 
                                     symptomsMap, 
                                     false);            
            //BANDS: D, T1, T2, T, A1, A
        }
        else if(this.headType == HeadType.FAST_WAVES) {
            this.dataframeProcessing(rexec.execFunction("fwih.data.numbers"), 
                                     symptomsMap, true);
            this.dataframeProcessing(rexec.execFunction("fwih.data.bands"), 
                                     symptomsMap, false);            
            //BANDS: A2, B1, B2, B3, B4, B5, B6, B, G
        }
        else if(this.headType == HeadType.REWARDS) {
            this.dataframeProcessing(rexec.execFunction("rewardsh.data.numbers"), 
                                     symptomsMap, true);  
            this.dataframeProcessing(rexec.execFunction("rewardsh.data.bands"), 
                                     symptomsMap, false);            
            //Fp1, F3, C3, Fz = B3
            //O1, P4, O2, T6, Pz = A, A1
            //Fp2, F4, C4, F8, T4, Cz = B1, B2
            //P3, F7, T3, T5 = NOT CONSIDERED (except for 'weak delta' symptom) 
        }   
        
        this.insertUserSymptomData(symptomsMap,
                                   this.headType);
    }
    
    
    private void dataframeProcessing(REXP rexp, 
                                     Map<String, JewelMap> symptomsMap, 
                                     boolean processNumberValues) 
                throws RScriptChangedException {
        try {
            ArrayList<String> surfacePowerSites = new ArrayList();
            
            Collection columns = rexp.asList().values();
            Collection metadata = rexp._attr().asList().values();            
            REXPString rxpSites = (REXPString) metadata.toArray()[1];
            
            for(String site : rxpSites.asStrings()) {
                if( ! site.toLowerCase().contains("symptoms") && ! site.toLowerCase().contains("category")) 
                    surfacePowerSites.add(site);          
            }           

            //WE ONLY HAVE SYMPTOMS IN DzM SURFACE POWER!!!!!
            for(Object objRexpcol : columns) {
                REXPString singleColumn = (REXPString)objRexpcol;
                int rowIndex = 0;
                HeadData data = null;
                String symptomName = null;

                for(String cell : singleColumn.asStrings()) {
                    if (rowIndex == 0) {
                        symptomName = cell.toLowerCase();
                        //Badly written symptoms, we correct them.
                        /*if(symptomName.startsWith("depresson"))
                            symptomName = symptomName.replace("depresson", "depression");*/
                        if (symptomsMap.get(symptomName) != null) 
                            data = (HeadData) symptomsMap.get(symptomName); 
                        else {
                            throw new RScriptChangedException(processor, "Unexpected symptom " + symptomName);
                        }
                    }
                    //Last row says "Symptoms", so we skip it
                    else if(rowIndex == (singleColumn.length() -1))
                        continue;
                    else {
                        if(processNumberValues) {
                            Double numValue;
                            try {
                                numValue = Double.parseDouble(cell);
                            }
                            catch (Exception ex) {
                                numValue = null;
                            }
                            HeadCell headCell = data.getHeadCell((String)surfacePowerSites.get(rowIndex - 1));
                            headCell.setNumberValue(numValue);
                            data.setHeadCell(headCell.getCellName(), headCell);
                        }
                        else {
                            if(cell == null || cell.equalsIgnoreCase("NA") || cell.equalsIgnoreCase("inf") || cell.equalsIgnoreCase("-inf"))
                                cell = "";
                            HeadCell headCell = data.getHeadCell((String)surfacePowerSites.get(rowIndex - 1));
                            headCell.setDominantBand(cell);
                            data.setHeadCell(headCell.getCellName(), headCell);
                        }
                    }
                    rowIndex++;
                }                
                symptomsMap.put(symptomName, data);                 
            }
        }
        //Bizarre error, it should never happen
        catch(REXPMismatchException ex) { 
            throw new RScriptChangedException(processor, "General exception catch"); 
        }
        //Motherfucker won't throw REXPMismatchExpcetion nor any kind of exception if 
        //data comes with a different type than expected. So we must force it to 
        //throw an exception if that happens... You think this is garbage?? took
        //me 2 hours of debugging to know that this horseshit happens.
        catch(Exception ex) {
            throw ex;
        }
    } 
    
    /**
     * Inserts the User Symptom data into the map passed by parameter. The user 
     * symptom will have the highest valued sites according to the headtype that
     * must be shown (fast, slow or rewards).
     * @param symptomsMap
     * @param type 
     */
    private void insertUserSymptomData(Map<String, JewelMap> symptomsMap, 
                                       HeadType type)  {
        //Just in case it wasn't there previously
        symptomsMap.put("user symptom", new HeadData(new SurfaceEmptyHeadStrategy(), HeadType.TRAINING));
        HeadData userSymptomData = (HeadData) symptomsMap.get("user symptom");
        
        //symptomsMap contains a map of HeadData values (but with generalized JewelMap type reference)
        for(JewelMap headData : symptomsMap.values()) {
            //Each HeadData has a set of HeadCell, and we'll access them through the key because of the
            //complicated generalized reference of the values.
            for(String site : headData.getMap().keySet()) {
                HeadCell cellSite = (HeadCell) headData.getMap().get(site);
                if(cellSite.getNumberValue() != null) {
                    //We only care about positive valued sites on fast and slow waves
                    if(type == HeadType.FAST_WAVES || type == HeadType.SLOW_WAVES) {
                        if(userSymptomData.getHeadCell(site).getNumberValue() == null ||
                           cellSite.getNumberValue() > userSymptomData.getHeadCell(site).getNumberValue()) 
                              userSymptomData.setHeadCell(site, cellSite);
                    }
                    //We only care about negative valued sites on Rewards
                    else if(type == HeadType.REWARDS) {
                        //On Rewards, we don't care about P3, F7, T3 and T5
                        if(site.equalsIgnoreCase("P3") ||
                           site.equalsIgnoreCase("F7") ||
                           site.equalsIgnoreCase("T3") ||
                           site.equalsIgnoreCase("T5"))
                                continue;
                        //Delta sites are not considered for rewards (they are only 
                        //considered for "weak delta" symptom.
                        if( ! cellSite.getDominantBand().equalsIgnoreCase("D") &&
                           (userSymptomData.getHeadCell(site).getNumberValue() == null ||                       
                           cellSite.getNumberValue() < userSymptomData.getHeadCell(site).getNumberValue()))
                            userSymptomData.setHeadCell(site, cellSite);
                    }
                }                
            }
        }        
    }
}