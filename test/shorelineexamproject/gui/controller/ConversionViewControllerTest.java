/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.json.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Elisabeth
 */
public class ConversionViewControllerTest
{
    
    public ConversionViewControllerTest()
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
     * Test of initialize method, of class ConversionViewController.
     */
    @Test
    public void testInitialize()
    {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        ConversionViewController instance = new ConversionViewController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class ConversionViewController.
     */
    @Test
    public void testStart()
    {
        System.out.println("start");
        ConversionViewController instance = new ConversionViewController();
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class ConversionViewController.
     */
    @Test
    public void testStop()
    {
        System.out.println("stop");
        ConversionViewController instance = new ConversionViewController();
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pause method, of class ConversionViewController.
     */
    @Test
    public void testPause() throws Exception
    {
        System.out.println("pause");
        ConversionViewController instance = new ConversionViewController();
        instance.pause();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resume method, of class ConversionViewController.
     */
    @Test
    public void testResume()
    {
        System.out.println("resume");
        ConversionViewController instance = new ConversionViewController();
        instance.resume();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSuspended method, of class ConversionViewController.
     */
    @Test
    public void testIsSuspended()
    {
        System.out.println("isSuspended");
        ConversionViewController instance = new ConversionViewController();
        boolean expResult = false;
        boolean result = instance.isSuspended();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDone method, of class ConversionViewController.
     */
    @Test
    public void testIsDone()
    {
        System.out.println("isDone");
        ConversionViewController instance = new ConversionViewController();
        boolean expResult = false;
        boolean result = instance.isDone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateJsonObjects method, of class ConversionViewController.
     */
    @Test
    public void testCreateJsonObjects()
    {
        System.out.println("CreateJsonObjects");
        ConversionViewController instance = new ConversionViewController();
        JSONArray expResult = null;
        JSONArray result = instance.CreateJsonObjects();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
