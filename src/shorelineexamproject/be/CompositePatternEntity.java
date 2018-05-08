/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.be;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Elisabeth
 */
public class CompositePatternEntity
{

    private final StringProperty keyVariable = new SimpleStringProperty();
    private final StringProperty valueVariable = new SimpleStringProperty();
    private final ListProperty<CompositePatternEntity> listVariable = new SimpleListProperty<>();

    public ObservableList getListVariable()
    {
        return listVariable.get();
    }

    public void setListVariable(ObservableList value)
    {
        listVariable.set(value);
    }

    public ListProperty listVariableProperty()
    {
        return listVariable;
    }
    
    public String getValueVariable()
    {
        return valueVariable.get();
    }

    public void setValueVariable(String value)
    {
        valueVariable.set(value);
    }

    public StringProperty valueVariableProperty()
    {
        return valueVariable;
    }
    
    public String getKeyVariable()
    {
        return keyVariable.get();
    }

    public void setKeyVariable(String value)
    {
        keyVariable.set(value);
    }

    public StringProperty keyVariableProperty()
    {
        return keyVariable;
    }
    
}
