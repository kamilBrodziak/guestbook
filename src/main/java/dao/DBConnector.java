package dao;

import java.security.InvalidParameterException;
import java.sql.*;

public class DBConnector {

    private Connection conn;

    public void connect() throws SQLException{
        String url = "jdbc:postgresql://localhost:5432/guestbook";
        this.conn = DriverManager.getConnection(url , "postgres", "123");
        this.conn.setAutoCommit(false);
    }

    public void disconnect() throws SQLException{
        this.conn.close();
    }

    public ResultSet executeSelect(String query, String[] attr) throws SQLException{
        this.connect();
        PreparedStatement pstmt = preparePSTMT(query, attr);
        ResultSet rs = pstmt.executeQuery();
//            pstmt.close();
//            this.conn.close();
        return rs;
    }

    public void updateSQL(String query, String[] attr) throws SQLException {
        this.connect();
        PreparedStatement pstmt = preparePSTMT(query, attr);
        pstmt.executeUpdate();
        pstmt.close();
        this.disconnect();
        conn.close();
    }

    private PreparedStatement preparePSTMT(String query, String[] attr) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(query);
        if(query.split("\\?").length - 1 != attr.length) {
            throw new InvalidParameterException();
        }
        for(int i = 1; i <= attr.length; ++i) {
            pstmt.setString(i, attr[i - 1]);
        }

        return pstmt;
    }
}