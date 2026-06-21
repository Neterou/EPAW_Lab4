package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import epaw.lab4.model.JoinRequest;

/**
 * Persistence for the many-to-many user&lt;-&gt;bubble membership relation
 * (table {@code user_bubbles}). A membership is APPROVED (full member) or
 * PENDING (join request awaiting approval for a closed bubble).
 */
public class MembershipRepository extends BaseRepository {

    private static MembershipRepository instance;

    private MembershipRepository() {
        super();
    }

    public static synchronized MembershipRepository getInstance() {
        if (instance == null) {
            instance = new MembershipRepository();
        }
        return instance;
    }

    /** Insert or update the membership row with the given status. */
    public void setStatus(Integer userId, Integer bubbleId, String status) {
        String query = "INSERT INTO user_bubbles (user_id, bubble_id, status) VALUES (?, ?, ?) "
                + "ON CONFLICT(user_id, bubble_id) DO UPDATE SET status = excluded.status";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, bubbleId);
            statement.setString(3, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(Integer userId, Integer bubbleId) {
        String query = "DELETE FROM user_bubbles WHERE user_id = ? AND bubble_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, bubbleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Returns "APPROVED", "PENDING", or null if the user is not a member. */
    public String getStatus(Integer userId, Integer bubbleId) {
        String query = "SELECT status FROM user_bubbles WHERE user_id = ? AND bubble_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, bubbleId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isApproved(Integer userId, Integer bubbleId) {
        return "APPROVED".equals(getStatus(userId, bubbleId));
    }

    /**
     * Pending join requests across every bubble owned by {@code ownerId},
     * enriched with requester and bubble details, oldest first.
     */
    public List<JoinRequest> findPendingForOwner(Integer ownerId) {
        List<JoinRequest> requests = new ArrayList<>();
        String query = "SELECT m.user_id, m.bubble_id, m.joined_at, "
                + "u.name, u.username, u.picture, "
                + "b.name AS bubble_name, b.category AS bubble_category "
                + "FROM user_bubbles m "
                + "JOIN users u ON u.id = m.user_id "
                + "JOIN bubbles b ON b.id = m.bubble_id "
                + "WHERE m.status = 'PENDING' AND b.owner_id = ? "
                + "ORDER BY m.joined_at ASC";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, ownerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    JoinRequest r = new JoinRequest();
                    r.setUserId(rs.getInt("user_id"));
                    r.setBubbleId(rs.getInt("bubble_id"));
                    r.setRequestedAt(rs.getTimestamp("joined_at"));
                    r.setName(rs.getString("name"));
                    r.setUsername(rs.getString("username"));
                    r.setPicture(rs.getString("picture"));
                    r.setBubbleName(rs.getString("bubble_name"));
                    r.setBubbleCategory(rs.getString("bubble_category"));
                    requests.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
}
