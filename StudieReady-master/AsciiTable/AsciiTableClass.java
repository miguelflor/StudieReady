package AsciiTable;

import java.util.ArrayList;
import java.util.List;

public class AsciiTableClass implements AsciiTable{
    //constants
    //Errors
    private static final String DIFFERENT_NUMBER_COLONS = "The number of columns is not the same in every row";
    private static final String corners = "+";
    private static final String lines = "-";

    //variables
    private final int nRows;
    private final int nCols;

    private final List<List<String>> rows;

    public AsciiTableClass(List<List<String>> rows) {
        nCols = rows.get(0).size();
        for (List<String> row : rows) {
            if(row.size()!=nCols){
                throw new IllegalArgumentException(DIFFERENT_NUMBER_COLONS);
            }
        }
        this.rows = new ArrayList<>(rows);
        nRows = rows.size();
    }

    public AsciiTableClass(){
        nCols = 0;
        nRows = 0;
        rows = new ArrayList<>();
    }

    public String getTable(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                sb.append(rows.get(i).get(j));
                if (j!= nCols - 1) {
                    sb.append(" | ");
                }
            }
            if (i!= nRows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
