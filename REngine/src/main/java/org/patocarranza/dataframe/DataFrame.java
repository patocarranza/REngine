package org.patocarranza.dataframe;

import java.util.Collection;
import java.util.LinkedHashMap;
import org.rosuda.rengine.REXP;
import org.rosuda.rengine.REXPMismatchException;
import org.rosuda.rengine.REXPString;

/**
 * DataFrame java object, maps to a DataFrame from R. 
 * BEWARE: DataFrame is similar to a matrix plus more attributes, but
 * the notation is reversed: columnsXrows is the notation used in this
 * object to determine its size. Therefore, a DataFrame 5x2 has 5 columns
 * and 2 rows. It was chosen this way because in an R DataFrame all values
 * from a column must be of the same type, while rows have no condition. 
 * Also, REngine's REXP objects have facilities to traverse its references
 * by columns easily, but very cumbersome to traverse by rows.
 * @author paulcurcean (http://github.com/paulcurcean) 
 *         patocarranza (http://github.com/patocarranza)
 */
public class DataFrame {

    private Object[][] objects;
    private String[] rowNames;
    private String[] colNames;
    
    public DataFrame(REXP rexpRef) 
            throws REXPMismatchException {
        //Check that this is an R DataFrame
        String varType = ((REXPString) rexpRef._attr().asList().values().toArray()[2]).asString();
        if( ! varType.toLowerCase().contains("data.frame"))
            throw new REXPMismatchException(rexpRef, "data.frame");
                
        this.colNames = ((REXPString) rexpRef._attr().asList().values().toArray()[0]).asStrings();
        this.rowNames = ((REXPString) rexpRef._attr().asList().values().toArray()[1]).asStrings();
        
        this.objects = new Object[this.colNames.length][this.rowNames.length];
        
        Collection data = rexpRef.asList().values();        
        int colCounter = 0;
        int rowCounter = 0;
        //Support for string cell values so far only.
        for(Object objRexpcol : data) {
            REXPString singleColumn = (REXPString)objRexpcol;;
            for(String singleValue : singleColumn.asStrings()) {
                this.objects[colCounter][rowCounter] = singleValue;
                rowCounter++;
            }
            colCounter++;
            rowCounter = 0;
        }
    }

    public DataFrame(Object[][] objects, String[] colNames, String[] rowNames) {
        if (objects.length != colNames.length) {
            throw  new IllegalArgumentException("The number of columns of the data frame is not equal with the number of column names!");
        }
        if (objects[0].length != rowNames.length) {
            throw  new IllegalArgumentException("The number of rows of the data frame is not equal with the number of row names!");
        }
        this.objects = objects;
        this.rowNames = rowNames;
        this.colNames = colNames;
    }
    
    public DataFrame(Object defaultObject, String[] colNames, String[] rowNames) {
        this.objects = createObjectsMatrix(colNames.length, rowNames.length, defaultObject);
        this.rowNames = rowNames;
        this.colNames = colNames;
    }

    /**
     * create an empty DataFrame with the dimensions of n*m
     * @param columns is the number of columns
     * @param rows is the number of rows
     * @return an empty data frame of the dimension n x m
     */
    public static DataFrame create(int columns, int rows) {
        return new DataFrame(createEmptyObjectsMatrix(columns,rows), 
                             createDefaultNamesArray(columns),
                             createDefaultNamesArray(rows));
    }    
    
    public Object[] getColumn(int colIndex) {
        if (colIndex >= getNumberOfColumns() || colIndex < 0) {
            throw new IllegalArgumentException("Requested index is greater than the number of columns - 1!");
        }        
        return objects[colIndex];
    }

    public Object[] getRow(int rowIndex) {
        if (rowIndex >= getNumberOfRows() || rowIndex < 0) {
            throw  new IllegalArgumentException("Requested index is greater than the number of rows - 1!");
        }

        Object[] row = new Object[objects.length];

        for (int j = 0; j < objects.length; j++) {
            row[j] = objects[j][rowIndex];
        }

        return row;
    }

    public Object[][] getObjects() {
        return objects;
    }

    public String[] getRowNames() {
        return rowNames;
    }

    public int getNumberOfColumns() {
        return objects.length;
    }

    public int getNumberOfRows() {
        return objects[0].length;
    }

    public Object getObject(int colIndex, int rowIndex) {
        if (colIndex >= getNumberOfColumns() || colIndex < 0 && rowIndex > getNumberOfRows() || rowIndex < 0) {
            throw new IllegalArgumentException("Given indexes are greater than the dimensions of the data frame!");
        } else if (colIndex >= getNumberOfColumns() || colIndex < 0) {
            throw  new IllegalArgumentException("Requested index is greater than the number of columns - 1!");
        } else if (rowIndex >= getNumberOfRows() || rowIndex < 0) {
            throw new IllegalArgumentException("Requested index is greater than the number of rows - 1!");
        }

        return objects[colIndex][rowIndex];
    }
    
    private static Object[][] createEmptyObjectsMatrix(int cols, int rows) {
        return new Object[cols][rows];
    }

    private static Object[][] createObjectsMatrix(int cols, int rows, Object o) {
        Object[][] objects = new Object[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++)
                objects[i][j] = o;            
        }

        return objects;
    }

    public static String[] createDefaultNamesArray(int n) {
        String[] names = new String[n];

        for (int i = 0; i < n; i++) 
            names[i] = "var" + i;       

        return names;
    }
    
    public LinkedHashMap<String, LinkedHashMap<String, Object>> getDataFrameAsHashMap() {
        LinkedHashMap<String, LinkedHashMap<String, Object>> map = 
                new LinkedHashMap<String, LinkedHashMap<String, Object>>();
        for(int i = 0; i > this.colNames.length; i++) {
            LinkedHashMap<String, Object> colMap = new LinkedHashMap<String, Object>();
            for(int k = 0; k > this.rowNames.length; k++) {
                colMap.put(this.rowNames[k], this.getObject(i, k));
            }
            map.put(this.colNames[i], colMap);
        }
        return map;
    }
}