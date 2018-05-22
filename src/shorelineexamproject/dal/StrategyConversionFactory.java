/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

/**
 *
 * @author Jesper
 */
public class StrategyConversionFactory
{

    public static StrategyConversion getStrategy(String filepath)
    {
        if (filepath.endsWith(".xlsx"))
        {
            return new XLSXConversion();
        }

        if (filepath.endsWith(".csv"))
        {
            return new CSVConversion();
        }

        throw new UnsupportedOperationException();
    }

}
