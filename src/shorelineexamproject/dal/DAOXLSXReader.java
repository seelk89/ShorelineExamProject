/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shorelineexamproject.be.ListViewObject;

/**
 *
 * @author Jesper
 */
public class DAOXLSXReader
{

    /**
     * Takes the String filepath of a xlsx file and finds
     * the headers before putting them into a ListView Jesper
     * 
     * @param filepath
     */
    public List<ListViewObject> readXLSXHeaders(String filepath)
    {
        List<ListViewObject> lstHeaders = new ArrayList();
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));

            //Get the workbook instance for xlsx file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Gets the first row from the first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();

            //Iterate through the cells of the first row
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                //Creates a ListViewObject Where the Headers of the XLSX file can be put as objects for the listView
                ListViewObject listViewObject = new ListViewObject();

                Cell cell = cellIterator.next();

                switch (cell.getCellType())
                {
                    //Case the cells value is of type double it will be parsed as String before the value is stored in a ListViewObject and then added to the ListView
                    case Cell.CELL_TYPE_NUMERIC:
                        listViewObject.setStringObject(String.valueOf(cell.getNumericCellValue()));
                        lstHeaders.add(listViewObject);

                        break;
                    //Case the cells value is of type String it will be put into a ListViewObject and then added to the ListView
                    case Cell.CELL_TYPE_STRING:
                        listViewObject.setStringObject(cell.getStringCellValue());
                        lstHeaders.add(listViewObject);

                        break;
                }
            }

            file.close();
            FileOutputStream out = new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return lstHeaders;
    }

    /**
     * Takes a filepath as a String, a String representation of a header in the
     * currently selected xlsx file and gets values from the rows below the
     * selected header in the currently selected xlsx file and puts them in an assigned list. Jesper
     *
     * @param filepath
     * @param header
     * @param headerList
     */
    public void getXLSXHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));
            String cellData = null;
            int colIndex = 0;
            int rowIndex = 0;

            //Get the workbook instance for xlsx file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Gets the first row from the first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();

            //Iterate through the cells of the first row
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                switch (cell.getCellType())
                {
                    //Case the cells value is of type double it will be parsed as String and used to compare to the header
                    case Cell.CELL_TYPE_NUMERIC:

                        cellData = String.valueOf(cell.getNumericCellValue());

                        if (cellData.equals(header))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                rowIndex = rowIndex + 1;
                                Row r = rowIterator.next();
                                if (r != null)
                                {
                                    headerList.add(r.getCell(colIndex).toString());
                                }

                            }
                        }

                        break;
                    //Case the cells value is of type String it will be compared to the header
                    case Cell.CELL_TYPE_STRING:

                        cellData = cell.getStringCellValue();

                        if (cellData.equals(header))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                rowIndex = rowIndex + 1;
                                Row r = rowIterator.next();
                                if (r != null)
                                {
                                    headerList.add(r.getCell(colIndex).toString());
                                }
                            }
                        }
                        break;
                }
            }

            file.close();
            FileOutputStream out = new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
