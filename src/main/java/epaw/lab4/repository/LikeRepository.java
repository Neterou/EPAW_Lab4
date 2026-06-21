package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeRepository extends BaseRepository {

    private static LikeRepository instance;

    private LikeRepository() {
        super();
    }

    public static synchronized LikeRepository getInstance() {
        if (instance == null) {
            instance = new LikeRepository();
        }
        return instance;
    }

    public boolean isLiked(Integer userId, Integer tweetId) {
        String query = "SELECT 1 FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void add(Integer userId, Integer tweetId) {
        String query = "INSERT OR IGNORE INTO likes (user_id, tweet_id) VALUES (?, ?)";
        run(query, userId, tweetId);
    }

    public void remove(Integer userId, Integer tweetId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        run(query, userId, tweetId);
    }

    public int countForTweet(Integer tweetId) {
        String query = "SELECT COUNT(*) FROM likes WHERE tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void run(String query, Integer userId, Integer tweetId) {
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
