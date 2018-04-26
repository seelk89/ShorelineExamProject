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
 * @author Jesper
 */
public class ListViewObject
{
    private final StringProperty stringObject = new SimpleStringProperty();

    public String getStringObject() {
        return stringObject.get();
    }

    public void setStringObject(String value) {
        stringObject.set(value);
    }

    public StringProperty stringObjectProperty() {
        return stringObject;
    }

}
