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
import java.util.logging.Level;
import java.util.logging.Logger;
import shorelineexamproject.be.LogIn;
import shorelineexamproject.dal.database.connection.DBConnector;

/**
 *
 * @author Elisabeth
 */
public class DAOLogIn
{

    private final DBConnector cm;

    public DAOLogIn() throws IOException
    {
        this.cm = new DBConnector();
    }

    /**
     * makes a connection to the db to check that the username and password are
     * correct
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean UserLogin(String userName, String password)
    {

        try (Connection con = cm.getConnection())
        {
            PreparedStatement stmt = con.prepareStatement("SELECT userName, password FROM [User] WHERE userName = ? AND password = ?");
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return true;
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOLogIn.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Adds a login entry to the db.
     *
     * @param l
     */
    public void addUserToDB(LogIn l)
    {
        try (Connection con = cm.getConnection())
        {
            String sql
                    = "INSERT INTO [User]" //if [] are removed, error message says syntax near ","
                    + "(userName, password)"
                    + "VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, l.getUserName());
            pstmt.setString(2, l.getPassword());

            int affected = pstmt.executeUpdate();
            if (affected < 1)
            {
                throw new SQLException("User could not be added");
            }

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
            {
                //char.setId(rs.getInt(1));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DAOLogIn.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

    }
}
