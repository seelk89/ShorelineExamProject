/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shorelineexamproject.be.Customization;

/**
 *
 * @author Elisabeth
 */
public class ConversionViewControllerTest
{

    /**
     * Test of CreateJsonObjects method, of class ConversionViewController.
     */
    @Test
    public void testCreateJsonObjects_12args()
    {
        System.out.println("CreateJsonObjects");
        ArrayList<String> lstVarAssetSerialNumber1 = new ArrayList<String>();
        lstVarAssetSerialNumber1.add("1");
        ArrayList<String> lstVarType1 = new ArrayList<String>();
        lstVarType1.add("2");
        ArrayList<String> lstVarExternalWorkOrderId1 = new ArrayList<String>();
        lstVarExternalWorkOrderId1.add("3");
        ArrayList<String> lstVarSystemStatus1 = new ArrayList<String>();
        lstVarSystemStatus1.add("4");
        ArrayList<String> lstVarUserStatus1 = new ArrayList<String>();
        lstVarUserStatus1.add("5");
        ArrayList<String> lstVarName1 = new ArrayList<String>();
        lstVarName1.add("6");
        ArrayList<String> lstVarPriority1 = new ArrayList<String>();
        lstVarPriority1.add("7");
        ArrayList<String> lstVarLatestFinishDate1 = new ArrayList<String>();
        lstVarLatestFinishDate1.add("8");
        ArrayList<String> lstVarEarliestStartDate1 = new ArrayList<String>();
        lstVarEarliestStartDate1.add("9");
        ArrayList<String> lstVarLatestStartDate1 = new ArrayList<String>();
        lstVarLatestStartDate1.add("10");
        ArrayList<String> lstVarEstimatedTime1 = new ArrayList<String>();
        lstVarEstimatedTime1.add("11");
        ArrayList<String> lstVarDescription21 = new ArrayList<String>();
        lstVarDescription21.add("12");
        
        ConversionViewController instance = new ConversionViewController();
 
        String s = "[{\"assetSerialNumber\":\"1\",\"planning\":{\"estimatedTime\":\"11\",\"latestFinishDate\":\"8\",\"earliestStartDate\":\"9\",\"latestStartDate\":\"10\"},\"userStatus\":\"5\",\"createdBy\":\"SAP\",\"systemStatus\":\"4\",\"name\":\"6\",\"siteName\":\"\",\"externalWorkOrderId\":\"3\",\"type\":\"2\",\"priority\":\"7\",\"createdOn\":\"17-05-2018\",\"status\":\"NEW\"}]";
        JSONArray result = instance.CreateJsonObjects(lstVarAssetSerialNumber1,
                lstVarType1, lstVarExternalWorkOrderId1, lstVarSystemStatus1,
                lstVarUserStatus1, lstVarName1, lstVarDescription21, lstVarPriority1,
                lstVarLatestFinishDate1, lstVarEarliestStartDate1, lstVarLatestStartDate1,
                lstVarEstimatedTime1);
        assertEquals(s, result.toString());
    }

}
