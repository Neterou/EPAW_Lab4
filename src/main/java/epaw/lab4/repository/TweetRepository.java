package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Tweet;

public class TweetRepository extends BaseRepository {

    private static TweetRepository instance;

    private TweetRepository() {
        super();
    }

    public static synchronized TweetRepository getInstance() {
        if (instance == null) {
            instance = new TweetRepository();
        }
        return instance;
    }

    public void save(Tweet tweet) {
        String query = "INSERT INTO tweets (user_id, bubble_id, content, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweet.getUserId());
            statement.setInt(2, tweet.getBubbleId());
            statement.setString(3, tweet.getContent());
            statement.setString(4, tweet.getType() != null ? tweet.getType() : "STANDARD");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Delete a tweet only if it belongs to {@code userId}. */
    public void delete(Integer id, Integer userId) {
        String query = "DELETE FROM tweets WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Personalized timeline: posts authored by the people {@code viewerId}
     * follows, newest first, flagged with the viewer's own like state.
     */
    public Optional<List<Tweet>> findFollowedTimeline(Integer viewerId, Integer start, Integer count) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.id, t.user_id, t.bubble_id, t.content, t.type, t.created_at, "
                + "u.username, u.picture, "
                + "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS likes, "
                + "(SELECT COUNT(*) FROM comments c WHERE c.tweet_id = t.id) AS comments, "
                + "EXISTS(SELECT 1 FROM likes l2 WHERE l2.tweet_id = t.id AND l2.user_id = ?) AS liked "
                + "FROM tweets t JOIN users u ON t.user_id = u.id "
                + "WHERE t.user_id IN (SELECT followee_id FROM follows WHERE follower_id = ?) "
                + "ORDER BY t.created_at DESC, t.id DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, viewerId);
            statement.setInt(2, viewerId);
            statement.setInt(3, start);
            statement.setInt(4, count);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(rs.getInt("id"));
                    tweet.setUserId(rs.getInt("user_id"));
                    tweet.setBubbleId(rs.getInt("bubble_id"));
                    tweet.setContent(rs.getString("content"));
                    tweet.setType(rs.getString("type"));
                    tweet.setCreatedAt(rs.getTimestamp("created_at"));
                    tweet.setUsername(rs.getString("username"));
                    tweet.setUserPicture(rs.getString("picture"));
                    tweet.setLikeCount(rs.getInt("likes"));
                    tweet.setCommentCount(rs.getInt("comments"));
                    tweet.setLikedByMe(rs.getInt("liked") == 1);
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Posts authored by a user, newest first, enriched with like/comment counts. */
    public Optional<List<Tweet>> findByUser(Integer userId, Integer start, Integer count) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.id, t.user_id, t.bubble_id, t.content, t.type, t.created_at, "
                + "u.username, u.picture, "
                + "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS likes, "
                + "(SELECT COUNT(*) FROM comments c WHERE c.tweet_id = t.id) AS comments "
                + "FROM tweets t JOIN users u ON t.user_id = u.id "
                + "WHERE t.user_id = ? "
                + "ORDER BY t.created_at DESC, t.id DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, start);
            statement.setInt(3, count);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(rs.getInt("id"));
                    tweet.setUserId(rs.getInt("user_id"));
                    tweet.setBubbleId(rs.getInt("bubble_id"));
                    tweet.setContent(rs.getString("content"));
                    tweet.setType(rs.getString("type"));
                    tweet.setCreatedAt(rs.getTimestamp("created_at"));
                    tweet.setUsername(rs.getString("username"));
                    tweet.setUserPicture(rs.getString("picture"));
                    tweet.setLikeCount(rs.getInt("likes"));
                    tweet.setCommentCount(rs.getInt("comments"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Posts in a bubble, newest first, enriched with like/comment counts. */
    public Optional<List<Tweet>> findByBubble(Integer bubbleId, Integer viewerId, Integer start, Integer count) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.id, t.user_id, t.bubble_id, t.content, t.type, t.created_at, "
                + "u.username, u.picture, "
                + "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS likes, "
                + "(SELECT COUNT(*) FROM comments c WHERE c.tweet_id = t.id) AS comments, "
                + "EXISTS(SELECT 1 FROM likes l2 WHERE l2.tweet_id = t.id AND l2.user_id = ?) AS liked "
                + "FROM tweets t JOIN users u ON t.user_id = u.id "
                + "WHERE t.bubble_id = ? "
                + "ORDER BY t.created_at DESC, t.id DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, viewerId);
            statement.setInt(2, bubbleId);
            statement.setInt(3, start);
            statement.setInt(4, count);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(rs.getInt("id"));
                    tweet.setUserId(rs.getInt("user_id"));
                    tweet.setBubbleId(rs.getInt("bubble_id"));
                    tweet.setContent(rs.getString("content"));
                    tweet.setType(rs.getString("type"));
                    tweet.setCreatedAt(rs.getTimestamp("created_at"));
                    tweet.setUsername(rs.getString("username"));
                    tweet.setUserPicture(rs.getString("picture"));
                    tweet.setLikeCount(rs.getInt("likes"));
                    tweet.setCommentCount(rs.getInt("comments"));
                    tweet.setLikedByMe(rs.getInt("liked") == 1);
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
