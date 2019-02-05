package dao;

import model.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CommentDAOPostgreSQL implements CommentDAO{
    private DBConnector dbConnector;

    public CommentDAOPostgreSQL(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Optional<Comment> getCommentByID(int id) throws SQLException {
        String query = "SELECT * FROM comments WHERE id=?::integer";
        String[] args = {Integer.toString(id)};
        return getComment(query, args);
    }

    @Override
    public Optional<Comment> getCommentByLoginID(int id) throws SQLException {
        String query = "SELECT * FROM comments WHERE login_id=?::integer";
        String[] args = {Integer.toString(id)};
        return getComment(query, args);
    }

    private Optional<Comment> getComment(String query, String[] args) throws SQLException{
        ResultSet rs = dbConnector.executeSelect(query, args);
        if(rs.next()) {
            return Optional.of(new Comment(rs.getInt("id"), rs.getInt("login_id"),
                    rs.getString("comment"), rs.getTimestamp("date")));
        }
        return Optional.ofNullable(null);
    }

    @Override
    public void addComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comments(login_id, comment, date) VALUES(?::integer, ?, ?::timestamp);";
        String[] attr = {Integer.toString(comment.getLoginID()), comment.getCommentText(),
                comment.getDate().toString()};
        dbConnector.updateSQL(query, attr);
    }

    @Override
    public void updateComment(Comment comment) throws SQLException {
        String query = "UPDATE logins SET comment=? WHERE id=?::integer;";
        String[] attr = {comment.getCommentText(), Integer.toString(comment.getID())};
        dbConnector.updateSQL(query, attr);
    }

    @Override
    public void deleteCommentByID(int id) throws SQLException {
        String query = "DELETE comments WHERE id=?::integer;";
        String[] attr = {Integer.toString(id)};
        dbConnector.updateSQL(query, attr);
    }
}
