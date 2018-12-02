package cassone.util;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Andrea
 * Date: 10-apr-2004
 * Time: 15.48.16
 * To change this template use File | Settings | File Templates.
 */
public class RtfTable {

    private DataTable dataTable;

    private Table rtfTable;

    private int columns;

    /**
     * Constructor
     */
    public RtfTable(DataTable table) throws BadElementException {
        dataTable = table;
        columns = dataTable.getCol();
        rtfTable = new Table(columns);
        fillTable();

    }

    /**
     * @throws BadElementException
     */
    private void fillTable() throws BadElementException {

        for (int i = 1; i <= dataTable.getRows(); i++) {
            insertRow(i);
        }
    }

    /**
     * @param row
     * @throws BadElementException
     */
    private void insertRow(int row) throws BadElementException {

        String value;
        for (int i = 1; i <= columns; i++) {
            value = (String) dataTable.getElement(row, i);
            rtfTable.addCell(formatCell(value));
        }
    }

    /**
     * @param text
     * @return
     */
    private Phrase formatCell(String text) {
        return new Phrase(text);
    }


    /**
     * @return
     * @throws BadElementException
     */
    public Table getTable() throws BadElementException {
        return rtfTable;
    }


}
