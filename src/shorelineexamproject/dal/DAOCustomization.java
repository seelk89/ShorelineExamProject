/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shorelineexamproject.dal.database.connection.DBConnector;
import shorelineexamproject.be.Customization;
import shorelineexamproject.dal.database.connection.ConnectionPool;
import shorelineexamproject.dal.exceptions.DalException;

/**
 *
 * @author Elisabeth
 */
public class DAOCustomization
{

    private final ConnectionPool conPool;
    private final DBConnector cm;

    public DAOCustomization() throws IOException, DalException
    {
        this.cm = new DBConnector();
        this.conPool = new ConnectionPool();
    }

    /**
     * This method gets all the movies on a list way.
     *
     * @return
     */
    public List<Customization> getAllCustomizations() throws DalException
    {
        System.out.println("Getting all Customizations.");

        List<Customization> allCustomizations = new ArrayList();
Connection con = conPool.checkOut(); //added this
        try 
        {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Customization");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Customization c = new Customization();
                c.setId(rs.getInt("id"));
                c.setUser(rs.getString("user"));
                c.setDateOfCreation(rs.getString("dateOfCreation"));
                c.setNameOfCustomization(rs.getString("nameOfCustomization"));
                c.setAssetSerialNumber(rs.getString("assetSerialNumber"));
                c.setType(rs.getString("type"));
                c.setExternalWorkOrderId(rs.getString("externalWorkOrderId")); //stort i, bug
                c.setSystemStatus(rs.getString("systemStatus"));
                c.setUserStatus(rs.getString("userStatus"));
                c.setName(rs.getString("name"));
                c.setPriority(rs.getString("priority"));
                c.setStatus(rs.getString("status"));
                c.setLatestFinishDate(rs.getString("latestFinishDate"));
                c.setEarliestStartDate(rs.getString("earliestStartDate"));
                c.setLatestStartDate(rs.getString("latestStartDate"));
                c.setEstimatedTime(rs.getString("estimatedTime"));

                allCustomizations.add(c);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOCustomization.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        finally
        {
            conPool.checkIn(con); //and this
        }
        return allCustomizations;
    }

    /**
     * Adds an entry to Customization
     *
     * @param c
     */
    public void addCustomizationToDB(Customization c) throws DalException
    {
        Connection con = conPool.checkOut(); //added this
        try 
        {
            String sql
                    = "INSERT INTO Customization"
                    + "([user], dateOfCreation, nameOfCustomization, assetSerialNumber, type, externalWorkOrderId, systemStatus, userStatus,"
                    + " name, priority, status, latestFinishDate, earliestStartDate, latestStartdate, estimatedTime) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, c.getUser());
            pstmt.setString(2, c.getDateOfCreation());
            pstmt.setString(3, c.getNameOfCustomization());
            pstmt.setString(4, c.getAssetSerialNumber());
            pstmt.setString(5, c.getType());
            pstmt.setString(6, c.getExternalWorkOrderId());
            pstmt.setString(7, c.getSystemStatus());
            pstmt.setString(8, c.getUserStatus());
            pstmt.setString(9, c.getName());
            pstmt.setString(10, c.getPriority());
            pstmt.setString(11, c.getStatus());
            pstmt.setString(12, c.getLatestFinishDate());
            pstmt.setString(13, c.getEarliestStartDate());
            pstmt.setString(14, c.getLatestStartDate());
            pstmt.setString(15, c.getEstimatedTime());

            int affected = pstmt.executeUpdate();
            if (affected < 1)
            {
                throw new SQLException("Customization could not be added");
            }

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
            {
                //char.setId(rs.getInt(1));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOCustomization.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        finally
        {
            conPool.checkIn(con); //and this
        }
    }

    /**
     * Removes selected movie from Movie and CatMovie.
     *
     * @param selectedCustomization
     */
    public void removeCustomizationFromDb(Customization selectedCustomization) throws DalException
    {Connection con = conPool.checkOut(); //added this
        try 
        {
            String sql
                    = "DELETE FROM Customization WHERE nameOfCustomization=? ";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selectedCustomization.getNameOfCustomization());
            pstmt.execute();

        } catch (SQLException ex)
        {
            Logger.getLogger(DAOCustomization.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        finally
        {
            conPool.checkIn(con); //and this
        }
    }

}
