/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.util.ArrayList;
import java.util.List;
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

class XLSXConversion implements StrategyConversion
{
    DAOXLSXReader xlsxReader = new DAOXLSXReader();
    
    @Override
    public List<ListViewObject> headers(String filepath)
    {
        return xlsxReader.readXLSXHeaders(filepath);
    }

    @Override
    public void valuesInHeaderColumn(String filepath, String header, ArrayList<String> headerList)
    {
        xlsxReader.getXLSXHeaderValues(filepath, header, headerList);
    }

}

class CSVConversion implements StrategyConversion
{
    DAOCSVReader csvReader = new DAOCSVReader();
    
    @Override
    public List<ListViewObject> headers(String filepath)
    {
        return csvReader.readCSVHeaders(filepath);
    }

    @Override
    public void valuesInHeaderColumn(String filepath, String header, ArrayList<String> headerList)
    {
        csvReader.getCSVHeaderValues(filepath, header, headerList);
    }

}