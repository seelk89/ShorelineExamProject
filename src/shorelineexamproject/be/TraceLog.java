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
public class TraceLog
{
    private final StringProperty user = new SimpleStringProperty();
    private final StringProperty fileName = new SimpleStringProperty();
    private final StringProperty customization = new SimpleStringProperty();
    private final StringProperty date = new SimpleStringProperty();

    public String getDate()
    {
        return date.get();
    }

    public void setDate(String value)
    {
        date.set(value);
    }

    public StringProperty dateProperty()
    {
        return date;
    }

    public String getCustomization()
    {
        return customization.get();
    }

    public void setCustomization(String value)
    {
        customization.set(value);
    }

    public StringProperty customizationProperty()
    {
        return customization;
    }

    public String getFileName()
    {
        return fileName.get();
    }

    public void setFileName(String value)
    {
        fileName.set(value);
    }

    public StringProperty fileNameProperty()
    {
        return fileName;
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
    
}
