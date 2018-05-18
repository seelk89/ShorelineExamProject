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

/**
 *
 * @author Elisabeth
 */
public class DAOTraceLog
{

    private final DBConnector cm;

    public DAOTraceLog() throws IOException
    {
        this.cm = new DBConnector();
    }

    /**
     * This method gets all tracelogs from the db through a connectionpool
     *
     * @return
     */
    public List<TraceLog> getAllTraceLogs()
    {

        List<TraceLog> allTraceLogs = new ArrayList();

        try (Connection con = cm.getConnection())
        {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM TraceLog");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                TraceLog t = new TraceLog();
                t.setUser(rs.getString("user"));
                t.setFileName(rs.getString("fileName"));
                t.setCustomization(rs.getString("customization"));
                t.setDate(rs.getString("date"));

                allTraceLogs.add(t);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOTraceLog.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return allTraceLogs;
    }

    /**
     * Adds a tracelog to the db, so we are able to see who did what and when it happened.
     * @param t
     */
    public void addTraceLogToDB(TraceLog t)
    {
        try (Connection con = cm.getConnection())
        {
            String sql
                    = "INSERT INTO TraceLog" //if [] are removed, error message says syntax near ","
                    + "([user], fileName, customization, date)"
                    + "VALUES(?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, t.getUser());
            pstmt.setString(2, t.getFileName());
            pstmt.setString(3, t.getCustomization());
            pstmt.setString(4, t.getDate());

            int affected = pstmt.executeUpdate();
            if (affected < 1)
            {
                throw new SQLException("TraceLog could not be added");
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(DAOLogIn.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        System.out.println("User added");
    }
}
