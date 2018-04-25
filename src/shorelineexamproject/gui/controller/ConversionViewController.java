/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class ConversionViewController implements Initializable
{

    @FXML
    private JFXButton btnTest;
    @FXML
    private JFXTextField txtTest;
    @FXML
    private JFXListView<?> lstTest;
    @FXML
    private JFXButton btnGet;

    private Window stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    /**
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickTest(ActionEvent event) throws IOException
    {
        String filepath = txtTest.getText();
        
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));

            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();

                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext())
                {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println("");
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

    /**
     * Opens a FileChooser that is only able to get xlsx files. Jesper
     *
     * @param event
     */
    @FXML
    private void clickGet(ActionEvent event)
    {
        String absolutePath = null;

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        //Adds a file filter that will only allow xlsx files (excel output files).
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Opens a window based on setting set above.
        File xlxsFile = fileChooser.showOpenDialog(stage);
        if (xlxsFile != null)
        {
            absolutePath = xlxsFile.getAbsolutePath();
        }

        txtTest.setText(absolutePath);
    }

}
