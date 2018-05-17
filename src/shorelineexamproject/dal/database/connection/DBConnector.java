/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal.database.connection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import javax.activation.DataSource;

/**
 *
 * @author pgn
 */
public class DBConnector
{
    private final SQLServerDataSource ds;

    public DBConnector() throws IOException
    {

        ds = new SQLServerDataSource();
        ds.setDatabaseName("DBShorelineExamProject");
        ds.setUser("CS2017B_1");
        ds.setPassword("9kolol9A");
        ds.setPortNumber(1433);
        ds.setServerName("10.176.111.31");
    }

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }

    DataSource getDataSource() //dunno what this method is for
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
