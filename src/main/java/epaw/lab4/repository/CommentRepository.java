package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Comment;

public class CommentRepository extends BaseRepository {

    private static CommentRepository instance;

    private CommentRepository() {
        super();
    }

    public static synchronized CommentRepository getInstance() {
        if (instance == null) {
            instance = new CommentRepository();
        }
        return instance;
    }

    public void save(Comment comment) {
        String query = "INSERT INTO comments (tweet_id, user_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, comment.getTweetId());
            statement.setInt(2, comment.getUserId());
            statement.setString(3, comment.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id, Integer userId) {
        String query = "DELETE FROM comments WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<List<Comment>> findByTweet(Integer tweetId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.id, c.tweet_id, c.user_id, c.content, c.created_at, u.username, u.picture "
                + "FROM comments c JOIN users u ON c.user_id = u.id "
                + "WHERE c.tweet_id = ? ORDER BY c.created_at ASC, c.id ASC";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id"));
                    c.setTweetId(rs.getInt("tweet_id"));
                    c.setUserId(rs.getInt("user_id"));
                    c.setContent(rs.getString("content"));
                    c.setCreatedAt(rs.getTimestamp("created_at"));
                    c.setUsername(rs.getString("username"));
                    c.setUserPicture(rs.getString("picture"));
                    comments.add(c);
                }
                return Optional.of(comments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
