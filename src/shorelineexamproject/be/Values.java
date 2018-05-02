/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.be;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Elisabeth
 */
public class Values
{

    private final StringProperty varAssetSerialNumber = new SimpleStringProperty();

    public String getVarAssetSerialNumber()
    {
        return varAssetSerialNumber.get();
    }

    public void setVarAssetSerialNumber(String value)
    {
        varAssetSerialNumber.set(value);
    }

    public StringProperty varAssetSerialNumberProperty()
    {
        return varAssetSerialNumber;
    }
    
    
}
//private String varSiteName = ""; //is okay if it's always empty
//    private String varAssetSerialNumber = "asset.id";
//    private String varType = "Order Type";
//    private String varExternalWorkOrderId = "Order";
//    private String varSystemStatus = "System Status";
//    private String varUserStatus = "User Status";
//    private String varCreatedOn = "Datetime Object(Date now)";
//    private String varCreatedBy = "SAP";
//    private String varName = "Opr.short text, if empty then Description 2";
//    private String varPriority = "Priority, if not set, Low";
//    private String varStatus = "New"; //always new
//    private String varLatestFinishDate = "Datetime Object";
//    private String varEarliestStartDate = "Datetime Object";
//    private String varLatestStartDate = "Datetime Object";
//    private String varEstimatedTime = ""; //Hours if exist in the input, else null(?)