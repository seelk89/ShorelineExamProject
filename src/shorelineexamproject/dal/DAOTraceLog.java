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
import shorelineexamproject.be.TraceLog;
import shorelineexamproject.dal.database.connection.ConnectionPool;
import shorelineexamproject.dal.exceptions.DalException;

/**
 *
 * @author Elisabeth
 */
public class DAOTraceLog
{

    private final ConnectionPool conPool;
    private final DBConnector cm;

    public DAOTraceLog() throws IOException, DalException
    {
        this.cm = new DBConnector();
        this.conPool = new ConnectionPool();
    }

    /**
     * This method gets all tracelogs
     *
     * @return
     * @throws shorelineexamproject.dal.exceptions.DalException
     */
    public List<TraceLog> getAllTraceLogs() throws DalException
    {
        System.out.println("Getting all TraceLogs.");

        List<TraceLog> allTraceLogs = new ArrayList();
        Connection con = conPool.checkOut(); //added this

        try
        {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM TraceLog");
            ResultSet rs = stmt.executeQuery();

            System.out.println(con); //how many connections are there even?
            while (rs.next())
            {
                TraceLog t = new TraceLog();
                t.setUser(rs.getString("user"));
                t.setFileName(rs.getString("fileName"));
                t.setCustomization(rs.getString("customization"));
                t.setDate(rs.getString("date"));
                t.setError(rs.getString("error"));

                allTraceLogs.add(t);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOTraceLog.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally
        {
            conPool.checkIn(con); //and this
        }
        return allTraceLogs;
    }

    public void addTraceLogToDB(TraceLog t) throws DalException
    {
        Connection con = conPool.checkOut(); //added this
        try
        {

            String sql
                    = "INSERT INTO TraceLog" //if [] are removed, error message says syntax near ","
                    + "([user], fileName, customization, date, error)"
                    + "VALUES(?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, t.getUser());
            pstmt.setString(2, t.getFileName());
            pstmt.setString(3, t.getCustomization());
            pstmt.setString(4, t.getDate());
            pstmt.setString(5, t.getError());

            int affected = pstmt.executeUpdate();
            if (affected < 1)
            {
                throw new SQLException("TraceLog could not be added");
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(DAOLogIn.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally
        {
            conPool.checkIn(con); //and this
        }
        System.out.println("User added");
    }
}
