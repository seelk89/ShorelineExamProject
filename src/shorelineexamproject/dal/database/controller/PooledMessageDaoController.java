/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.dal.database.controller;

import mechachatapp.dal.database.dao.MessageDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import mechachatapp.be.Message;
import mechachatapp.be.User;
import mechachatapp.dal.database.connection.ConnectionPool;
import mechachatapp.dal.exceptions.DalException;

/**
 *
 * @author pgn
 */
public class PooledMessageDaoController
{

    private final ConnectionPool conPool;
    private final MessageDAO msgDao;

    public PooledMessageDaoController(ConnectionPool conPool)
    {
        this.conPool = conPool;
        msgDao = new MessageDAO();
    }

    public Message createMessage(User sender, String msg) throws DalException
    {
        try
        {
            Connection con = conPool.checkOut();
            Message message = msgDao.createMessage(con, sender.getId(), msg);
            conPool.checkIn(con);
            return message;
        } catch (SQLException ex)
        {
            throw new DalException(ex.getMessage(), ex);
        }
    }

    public void deleteMessage(Message message) throws DalException
    {
        try
        {
            Connection con = conPool.checkOut();
            msgDao.deleteMessage(con, message);
            conPool.checkIn(con);
        } catch (SQLException ex)
        {
            throw new DalException(ex.getMessage(), ex);
        }
    }

    public List<Message> getAllMessages() throws DalException
    {
        try
        {
            Connection con = conPool.checkOut();
            List<Message> allMessages = msgDao.getAllMessages(con);
            conPool.checkIn(con);
            return allMessages;
        } catch (SQLException ex)
        {
            throw new DalException(ex.getMessage(), ex);
        }
    }

}
