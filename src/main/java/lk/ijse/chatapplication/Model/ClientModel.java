package lk.ijse.chatapplication.Model;

import lk.ijse.chatapplication.DBConnection.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientModel {
    public static String generateNextOrderId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT User_ID FROM user ORDER BY User_ID DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String string) {
        if(string != null) {
            String[] strings = string.split("U0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "U00"+id;
            }else {
                if (length < 3){
                    return "U0"+id;
                }else {
                    return "U"+id;
                }
            }
        }
        return "U001";
    }

    public static List<String> getUserName() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT User_Name FROM user";

        List<String> username = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            username.add(resultSet.getString(1));
        }
        return username;
    }

    public static String searchByUser_Name(String userName) throws SQLException, IOException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT User_Password FROM User WHERE User_Name = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, userName);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static String searchByUser_ID(String userName) throws SQLException, IOException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT User_ID FROM User WHERE User_Name = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, userName);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}

