package dataframe;

public class DataFrame<C> {

    private Object[][] objects;
    private String[] names;

    private DataFrame(Object[][] objects, String[] names) {
        this.objects = objects;
        this.names = names;
    }

    /**
     * create an empty DataFrame with the dimensions of n*m
     * @param n is the number of columns
     * @param m is the number of rows
     * @return an empty data frame of the dimension n x m
     */
    public static DataFrame create(int n, int m) {
        return new DataFrame(createEmptyObjectsMatrix(n,m), createDefaultNamesArray(n));
    }

    public static DataFrame create(Object[][] objects, String[] names) {
        if (objects.length != names.length) {
            throw  new IllegalArgumentException("The number of columns of the data frame is not equal with the number of names!");
        }

        return new DataFrame(objects, names);
    }
    
    

    public Object[] getColumn(int i) {
        if (i >= getNumberOfColumns() || i < 0) {
            throw new IllegalArgumentException("Requested index is greater than the number of columns - 1!");
        }

        return objects[i];
    }

    public Object[] getRow(int i) {
        if (i >= getNumberOfRows() || i < 0) {
            throw  new IllegalArgumentException("Requested index is greater than the number of rows - 1!");
        }

        Object[] row = new Object[objects.length];

        for (int j = 0; j < objects.length; j++) {
            row[j] = objects[j][i];
        }

        return row;
    }

    public Object[][] getObjects() {
        return objects;
    }

    public String[] getNames() {
        return names;
    }

    public int getNumberOfColumns() {
        return objects.length;
    }

    public int getNumberOfRows() {
        return objects[0].length;
    }

    public Object getObject(int n, int m) {
        if (n >= getNumberOfColumns() || n < 0 && m > getNumberOfRows() || m < 0) {
            throw new IllegalArgumentException("Given indexes are greater than the dimensions of the data frame!");
        } else if (n >= getNumberOfColumns() || n < 0) {
            throw  new IllegalArgumentException("Requested index is greater than the number of columns - 1!");
        } else if (m >= getNumberOfRows() || m < 0) {
            throw new IllegalArgumentException("Requested index is greater than the number of rows - 1!");
        }

        return objects[n][m];
    }
    
    public static Object[][] createEmptyObjectsMatrix(int n, int m) {
        return new Object[n][m];
    }

    public static Object[][] createObjectsMatrix(int n, int m, Object o) {
        Object[][] objects = new Object[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                objects[i][j] = o;
            }
        }

        return objects;
    }

    public static String[] createDefaultNamesArray(int n) {
        String[] names = new String[n];

        for (int i = 0; i < n; i++) {
            names[i] = "var" + i;
        }

        return names;
    }
}