/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shorelineexamproject.be.ListViewObject;

/**
 *
 * @author Jesper
 */
public class DAOXLSXReaderTest
{
    
    public DAOXLSXReaderTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of readXLSXHeaders method, of class DAOXLSXReader.
     */
    @org.junit.Test
    public void testReadXLSXHeaders()
    {
        System.out.println("readXLSXHeaders");
        String filepath = "";
        DAOXLSXReader instance = new DAOXLSXReader();
        List<ListViewObject> expResult = null;
        List<ListViewObject> result = instance.readXLSXHeaders(filepath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXLSXHeaderValues method, of class DAOXLSXReader.
     */
    @org.junit.Test
    public void testGetXLSXHeaderValues()
    {
        System.out.println("getXLSXHeaderValues");
        String filepath = "";
        String header = "";
        ArrayList<String> headerList = null;
        DAOXLSXReader instance = new DAOXLSXReader();
        instance.getXLSXHeaderValues(filepath, header, headerList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
