/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shorelineexamproject.be.ListViewObject;

/**
 *
 * @author Jesper
 */
public interface StrategyConversion
{

    List<ListViewObject> headers(String filepath);

    void valuesInHeaderColumn(String filepath,
            String header, ArrayList<String> headerList);
}

//class XLSXConversion implements StrategyConversion
//{
//
//    @Override
//    public List<ListViewObject> headers(String filepath)
//    {
//        List<ListViewObject> lstHeaders = new ArrayList();
//        Hashtable<String, Integer> headerDuplicates = new Hashtable<String, Integer>();
//
//        try
//        {
//            FileInputStream file = new FileInputStream(new File(filepath));
//
//            //Get the workbook instance for xlsx file 
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//            //Get first sheet from the workbook
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            //Gets the first row from the first sheet
//            Iterator<Row> rowIterator = sheet.iterator();
//            Row row = rowIterator.next();
//
//            //Iterate through the cells of the first row
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext())
//            {
//                //Creates a ListViewObject Where the Headers of the XLSX file can be put as objects for the listView
//                ListViewObject listViewObject = new ListViewObject();
//
//                Cell cell = cellIterator.next();
//
//                switch (cell.getCellType())
//                {
//                    //Case the cells value is of type double it will be parsed as String before the value is stored in a ListViewObject and then added to the ListView
//                    case Cell.CELL_TYPE_NUMERIC:
//
//                        Integer countN = headerDuplicates.get(cell.getStringCellValue());
//                        if (countN == null)
//                        {
//                            headerDuplicates.put(cell.getStringCellValue(), 1);
//                            listViewObject.setStringObject(String.valueOf(cell.getNumericCellValue()));
//                            lstHeaders.add(listViewObject);
//                        } else
//                        {
//                            headerDuplicates.put(cell.getStringCellValue(), ++countN);
//                            listViewObject.setStringObject(String.valueOf(cell.getNumericCellValue()) + " " + countN);
//                            lstHeaders.add(listViewObject);
//                        }
//
//                        break;
//                    //Case the cells value is of type String it will be put into a ListViewObject and then added to the ListView
//                    case Cell.CELL_TYPE_STRING:
//
//                        Integer countS = headerDuplicates.get(cell.getStringCellValue());
//                        if (countS == null)
//                        {
//                            headerDuplicates.put(cell.getStringCellValue(), 1);
//                            listViewObject.setStringObject(cell.getStringCellValue());
//                            lstHeaders.add(listViewObject);
//                        } else
//                        {
//                            headerDuplicates.put(cell.getStringCellValue(), ++countS);
//                            listViewObject.setStringObject(cell.getStringCellValue() + " " + countS);
//                            lstHeaders.add(listViewObject);
//                        }
//
//                        break;
//                }
//            }
//
//            file.close();
//
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        return lstHeaders;
//
//    }
//
//    @Override
//    public void valuesInHeaderColumn(String filepath, String header, ArrayList<String> headerList)
//    {
//        try
//        {
//            FileInputStream file = new FileInputStream(new File(filepath));
//
//            String cellData = null;
//
//            int colIndex = 0;
//            int rowIndex = 0;
//            int number = 1;
//            int numberCount = 0;
//            String headerWithoutNumeration = header;
//
//            //Divides the header by whitespace
//            String[] stringPart;
//            stringPart = header.trim().split("\\s+");
//
//            //Sets number = to the number behind a header, for example, if the header is description 3, it will be set to 3
//            if (isInteger(stringPart[stringPart.length - 1]) == true)
//            {
//                number = Integer.parseInt(stringPart[stringPart.length - 1]);
//                headerWithoutNumeration = header.substring(0, header.lastIndexOf(" "));
//            }
//
//            //Get the workbook instance for xlsx file 
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//            //Get first sheet from the workbook
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            //Gets the first row from the first sheet
//            Iterator<Row> rowIterator = sheet.iterator();
//            Row row = rowIterator.next();
//
//            //Iterate through the cells of the first row
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext())
//            {
//                Cell cell = cellIterator.next();
//
//                switch (cell.getCellType())
//                {
//                    //Case the cells value is of type double it will be parsed as String and used to compare to the header
//                    case Cell.CELL_TYPE_NUMERIC:
//
//                        cellData = String.valueOf(cell.getNumericCellValue());
//
//                        if (cellData.equals(headerWithoutNumeration))
//                        {
//                            numberCount = numberCount + 1;
//                            if (number == numberCount)
//                            {
//                                colIndex = cell.getColumnIndex();
//
//                                //runs down the rows and prints out the values
//                                while (rowIterator.hasNext())
//                                {
//                                    rowIndex = rowIndex + 1;
//                                    Row r = rowIterator.next();
//                                    if (r != null)
//                                    {
//                                        headerList.add(r.getCell(colIndex).toString());
//                                    }
//                                }
//                            }
//                        }
//
//                        break;
//                    //Case the cells value is of type String it will be compared to the header
//                    case Cell.CELL_TYPE_STRING:
//
//                        cellData = cell.getStringCellValue();
//
//                        if (cellData.equals(headerWithoutNumeration))
//                        {
//                            numberCount = numberCount + 1;
//                            if (number == numberCount)
//                            {
//                                colIndex = cell.getColumnIndex();
//
//                                //runs down the rows and prints out the values
//                                while (rowIterator.hasNext())
//                                {
//                                    rowIndex = rowIndex + 1;
//                                    Row r = rowIterator.next();
//                                    if (r != null)
//                                    {
//                                        headerList.add(r.getCell(colIndex).toString());
//                                    }
//                                }
//                            }
//                        }
//                        break;
//                }
//            }
//
//            file.close();
//
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
//
//class CSVConversion implements StrategyConversion
//{
//
//    /**
//     * Converts a CSV file into a String[][] and gets the String objects on
//     * [0][i]
//     *
//     * @param filepath
//     * @return
//     */
//    @Override
//    public List<ListViewObject> headers(String filepath)
//    {
//        List<ListViewObject> lstHeaders = new ArrayList();
//        Hashtable<String, Integer> headerDuplicates = new Hashtable<String, Integer>();
//
//        BufferedReader reader;
//        try
//        {
//            reader = new BufferedReader(new FileReader(filepath));
//
//            String line = null;
//
//            List<String[]> lines = new ArrayList<String[]>();
//            try
//            {
//                while ((line = reader.readLine()) != null)
//                {
//                    lines.add(line.split(","));
//                }
//            } catch (IOException ex)
//            {
//                Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            //Convert our list to a String array.
//            String[][] csvArray = new String[lines.size()][0];
//            lines.toArray(csvArray);
//
//            //Reads headers for the header list
//            for (int i = 0; i < colCount(filepath); i++)
//            {
//                ListViewObject listViewObject = new ListViewObject();
//
//                Integer count = headerDuplicates.get(csvArray[0][i]);
//                if (count == null)
//                {
//                    headerDuplicates.put(csvArray[0][i], 1);
//                    listViewObject.setStringObject(csvArray[0][i]);
//                    lstHeaders.add(listViewObject);
//                } else
//                {
//                    headerDuplicates.put(csvArray[0][i], ++count);
//                    listViewObject.setStringObject(String.valueOf(csvArray[0][i]) + " " + count);
//                    lstHeaders.add(listViewObject);
//                }
//            }
//
//        } catch (FileNotFoundException ex)
//        {
//            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return lstHeaders;
//    }
//
//    @Override
//    public void valuesInHeaderColumn(String filepath,
//            String header, ArrayList<String> headerList)
//    {
//        List<ListViewObject> lstHeaders = new ArrayList();
//        String headerWithoutNumeration = header;
//        int number = 1;
//        int numberCount = 0;
//
//        //Divides the header by whitespace
//        String[] stringPart;
//        stringPart = header.trim().split("\\s+");
//
//        //Sets number = to the number behind a header, for example, if the header is description 3, it will be set to 3
//        if (isInteger(stringPart[stringPart.length - 1]) == true)
//        {
//            number = Integer.parseInt(stringPart[stringPart.length - 1]);
//            headerWithoutNumeration = header.substring(0, header.lastIndexOf(" "));
//        }
//
//        BufferedReader reader;
//        try
//        {
//            reader = new BufferedReader(new FileReader(filepath));
//
//            String line = null;
//
//            List<String[]> lines = new ArrayList<String[]>();
//            try
//            {
//                while ((line = reader.readLine()) != null)
//                {
//                    lines.add(line.split(","));
//                }
//            } catch (IOException ex)
//            {
//                Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            //Convert our list to a String array.
//            String[][] csvArray = new String[lines.size()][0];
//            lines.toArray(csvArray);
//
//            //Reads headers for the header list
//            for (int i = 0; i < colCount(filepath); i++)
//            {
//                if (csvArray[0][i].equals(headerWithoutNumeration))
//                {
//                    numberCount = numberCount + 1;
//                    if (number == numberCount)
//                    {
//                        for (int j = 1; j < csvArray.length; j++)
//                        {
//                            headerList.add(csvArray[j][i]);
//                        }
//                    }
//                }
//            }
//
//        } catch (FileNotFoundException ex)
//        {
//            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /**
//     * Counts the number of columns in a CSV file by counting the , and adding 1
//     *
//     * @param filepath
//     * @return
//     * @throws FileNotFoundException
//     */
//    private int colCount(String filepath) throws FileNotFoundException
//    {
//        int colCount = 0;
//        BufferedReader reader;
//
//        reader = new BufferedReader(new FileReader(filepath));
//
//        //Count ,
//        String firstLine = null;
//        List<String> linesBecause = new ArrayList<>();
//
//        try
//        {
//            while ((firstLine = reader.readLine()) != null)
//            {
//                linesBecause.add(firstLine);
//            }
//        } catch (IOException ex)
//        {
//            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        firstLine = linesBecause.get(0);
//        colCount = firstLine.length() - firstLine.replace(",", "").length() + 1;
//
//        return colCount;
//    }
//
//}