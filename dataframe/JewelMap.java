package dataframe;

import java.util.Map;


/**
 * A lot of Jewel data is organized in maps. This interface is to be used in
 * such collections of data to help us with polymorphism. 
 * @author Patricio Carranza, Beatriz Carballo
 * @version 1.00.000
 * Last update: 05/02/2015
 */
public interface JewelMap {
    
    /**
     * Implementers of this interface must always hold a map that can be
     * delivered through this method. The generics ? should be an object 
     * existing in this package (such as HeadCell or RegionOfInterest).
     * @return 
     */
     Map<String, ?> getMap();
} 
