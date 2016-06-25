package org.patocarranza.tests;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.patocarranza.dataframe.DataFrame;

/**
 * 
 * @author paulcurcean (http://github.com/paulcurcean) 
 *         patocarranza (http://github.com/patocarranza)
 */
public class DataFrameTest {

    @Test
    public void createDefaultDataFrameTest() {
        DataFrame dataFrame = DataFrame.create(4, 3);
        
        assertEquals(dataFrame.getNumberOfColumns(), 4);
        assertEquals(dataFrame.getNumberOfRows(), 3);
        assertEquals(dataFrame.getRowNames().length, 4);
        assertEquals(dataFrame.getRowNames()[0], "var0");
        assertEquals(dataFrame.getRowNames()[1], "var1");
        assertEquals(dataFrame.getRowNames()[2], "var2");
        assertEquals(dataFrame.getRowNames()[3], "var3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDataFrameTest_shouldFail() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] rowNames = new String[] {"numbers"};
        String[] colNames = new String[] {"numbers"};

        DataFrame data = new DataFrame(objects, colNames, rowNames);
    }

    @Test
    public void createDataFrameTest_shouldSucceed() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(Arrays.equals(dataFrame.getRow(0), new Object[]{1, "a"}));
        assertTrue(Arrays.equals(dataFrame.getColumn(1), new Object[]{"a", "b", "c"}));
        assertTrue(dataFrame.getObject(0,2).equals(3));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRowTest_shouldFail_1() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(Arrays.equals(dataFrame.getRow(3), new Object[]{1, "a"}));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRowTest_shouldFail_2() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(Arrays.equals(dataFrame.getRow(-3), new Object[]{1, "a"}));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getColumnTest_shouldFail_1() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(Arrays.equals(dataFrame.getColumn(7), new Object[]{"a", "b", "c"}));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getColumnTest_shouldFail_2() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(Arrays.equals(dataFrame.getColumn(-7), new Object[]{"a", "b", "c"}));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTest_shouldFail_1() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(dataFrame.getObject(-1,2).equals(3));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTest_shouldFail_2() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(dataFrame.getObject(0,-2).equals(3));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTest_shouldFail_3() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(dataFrame.getObject(-0,5).equals(3));*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTest_shouldFail_4() {
        Object[][] objects = new Object[][]{{1,2,3}, {"a", "b", "c"}};
        String[] names = new String[] {"numbers", "letters"};

        /*DataFrame dataFrame = DataFrame.create(objects, names);

        assertTrue(dataFrame.getObject(8,2).equals(3));*/
    }

}