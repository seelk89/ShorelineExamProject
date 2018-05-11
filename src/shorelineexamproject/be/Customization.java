/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Elisabeth
 */
public class Customization
{

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty user = new SimpleStringProperty();
    private final StringProperty dateOfCreation = new SimpleStringProperty();
    private final StringProperty nameOfCustomization = new SimpleStringProperty();
    private final StringProperty assetSerialNumber = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty externalWorkOrderId = new SimpleStringProperty();
    private final StringProperty systemStatus = new SimpleStringProperty();
    private final StringProperty userStatus = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty latestFinishDate = new SimpleStringProperty();
    private final StringProperty earliestStartDate = new SimpleStringProperty();
    private final StringProperty latestStartDate = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();

    public String getEstimatedTime()
    {
        return estimatedTime.get();
    }

    public void setEstimatedTime(String value)
    {
        estimatedTime.set(value);
    }

    public StringProperty estimatedTimeProperty()
    {
        return estimatedTime;
    }

    public String getLatestStartDate()
    {
        return latestStartDate.get();
    }

    public void setLatestStartDate(String value)
    {
        latestStartDate.set(value);
    }

    public StringProperty latestStartDateProperty()
    {
        return latestStartDate;
    }

    public String getEarliestStartDate()
    {
        return earliestStartDate.get();
    }

    public void setEarliestStartDate(String value)
    {
        earliestStartDate.set(value);
    }

    public StringProperty earliestStartDateProperty()
    {
        return earliestStartDate;
    }

    public String getLatestFinishDate()
    {
        return latestFinishDate.get();
    }

    public void setLatestFinishDate(String value)
    {
        latestFinishDate.set(value);
    }

    public StringProperty latestFinishDateProperty()
    {
        return latestFinishDate;
    }

    public String getStatus()
    {
        return status.get();
    }

    public void setStatus(String value)
    {
        status.set(value);
    }

    public StringProperty statusProperty()
    {
        return status;
    }

    public Customization()
    {
    }

    public String getPriority()
    {
        return priority.get();
    }

    public void setPriority(String value)
    {
        priority.set(value);
    }

    public StringProperty priorityProperty()
    {
        return priority;
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String value)
    {
        name.set(value);
    }

    public StringProperty nameProperty()
    {
        return name;
    }

    public String getUserStatus()
    {
        return userStatus.get();
    }

    public void setUserStatus(String value)
    {
        userStatus.set(value);
    }

    public StringProperty userStatusProperty()
    {
        return userStatus;
    }

    public String getSystemStatus()
    {
        return systemStatus.get();
    }

    public void setSystemStatus(String value)
    {
        systemStatus.set(value);
    }

    public StringProperty systemStatusProperty()
    {
        return systemStatus;
    }

    public String getExternalWorkOrderId()
    {
        return externalWorkOrderId.get();
    }

    public void setExternalWorkOrderId(String value)
    {
        externalWorkOrderId.set(value);
    }

    public StringProperty externalWorkOrderIdProperty()
    {
        return externalWorkOrderId;
    }

    public String getType()
    {
        return type.get();
    }

    public void setType(String value)
    {
        type.set(value);
    }

    public StringProperty typeProperty()
    {
        return type;
    }

    public String getAssetSerialNumber()
    {
        return assetSerialNumber.get();
    }

    public void setAssetSerialNumber(String value)
    {
        assetSerialNumber.set(value);
    }

    public StringProperty assetSerialNumberProperty()
    {
        return assetSerialNumber;
    }

    public String getNameOfCustomization()
    {
        return nameOfCustomization.get();
    }

    public void setNameOfCustomization(String value)
    {
        nameOfCustomization.set(value);
    }

    public StringProperty nameOfCustomizationProperty()
    {
        return nameOfCustomization;
    }

    public String getDateOfCreation()
    {
        return dateOfCreation.get();
    }

    public void setDateOfCreation(String value)
    {
        dateOfCreation.set(value);
    }

    public StringProperty dateOfCreationProperty()
    {
        return dateOfCreation;
    }

    public String getUser()
    {
        return user.get();
    }

    public void setUser(String value)
    {
        user.set(value);
    }

    public StringProperty userProperty()
    {
        return user;
    }

    public int getId()
    {
        return id.get();
    }

    public void setId(int value)
    {
        id.set(value);
    }

    public IntegerProperty idProperty()
    {
        return id;
    }
    
}
