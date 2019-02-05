package dao;

import model.Comment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CommentDAO {
    public Optional<Comment> getCommentByID(int id) throws SQLException;
    public List<Comment> getCommentList() throws SQLException;
    public Optional<Comment> getCommentByLogin(String login) throws SQLException;
    public void addComment(Comment comment) throws SQLException;
    public void updateComment(Comment login) throws SQLException;
    public void deleteCommentByID(int id) throws SQLException;
}
