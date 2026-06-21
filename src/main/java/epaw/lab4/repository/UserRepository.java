package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.User;

public class UserRepository extends BaseRepository {

    private static UserRepository instance;

    private UserRepository() {
        super();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // ---------- uniqueness checks ----------

    public boolean existsByUsername(String username) {
        return countWhere("LOWER(username) = LOWER(?)", username);
    }

    public boolean existsByEmail(String email) {
        return countWhere("LOWER(email) = LOWER(?)", email);
    }

    public boolean existsByDni(String dni) {
        return countWhere("dni = ?", dni == null ? null : dni.toUpperCase());
    }

    /** True if another user (different id) already uses this username. */
    public boolean existsByUsernameExcept(String username, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(?) AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setInt(2, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean countWhere(String condition, String value) {
        String query = "SELECT COUNT(*) FROM users WHERE " + condition;
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, value);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------- authentication ----------

    public boolean checkLogin(User user) {
        String query = "SELECT id, name, email, username, picture, status, role, bubble_id "
                + "FROM users WHERE LOWER(username) = LOWER(?) AND password = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    mapBasic(user, rs);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------- persistence ----------

    /** Persist a new user and return the generated id (or -1 on failure). */
    public int save(User user) {
        String query = "INSERT INTO users (name, email, phone, phone_country, dni, zip, city, country, "
                + "gender, password, username, picture, status, role, bubble_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPhoneCountry());
            statement.setString(5, user.getDni() != null ? user.getDni().toUpperCase() : null);
            statement.setString(6, user.getZip());
            statement.setString(7, user.getCity());
            statement.setString(8, user.getCountry() != null ? user.getCountry().toUpperCase() : null);
            statement.setString(9, user.getGender());
            statement.setString(10, user.getPassword());
            statement.setString(11, user.getUsername());
            statement.setString(12, user.getPicture());
            statement.setString(13, user.getStatus());
            statement.setString(14, user.getRole() != null ? user.getRole() : "USER");
            if (user.getBubbleId() != null) {
                statement.setInt(15, user.getBubbleId());
            } else {
                statement.setNull(15, java.sql.Types.INTEGER);
            }
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** Update the editable profile fields (name, username, picture). */
    public boolean updateProfile(User user) {
        String query = "UPDATE users SET name = ?, username = ?, picture = ? WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPicture());
            statement.setInt(4, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> findById(Integer id) {
        String query = "SELECT id, name, email, username, picture, status, role, bubble_id FROM users WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    mapBasic(user, rs);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // ---------- follow graph ----------

    public void followUser(Integer followerId, Integer followeeId) {
        String query = "INSERT OR IGNORE INTO follows (follower_id, followee_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unfollowUser(Integer followerId, Integer followeeId) {
        String query = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Users that {@code id} follows. */
    public Optional<List<User>> findFollowed(Integer id, Integer start, Integer count) {
        String query = "SELECT u.id, u.name, u.username, u.picture, u.status, u.role, u.bubble_id "
                + "FROM users u JOIN follows f ON u.id = f.followee_id "
                + "WHERE f.follower_id = ? ORDER BY u.username LIMIT ?, ?";
        return queryUsers(query, id, start, count);
    }

    /** Users that {@code id} does NOT yet follow (excluding self). */
    public Optional<List<User>> findNotFollowed(Integer id, Integer start, Integer count) {
        String query = "SELECT u.id, u.name, u.username, u.picture, u.status, u.role, u.bubble_id "
                + "FROM users u WHERE u.id <> ? "
                + "AND u.id NOT IN (SELECT followee_id FROM follows WHERE follower_id = ?) "
                + "ORDER BY u.username LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, start);
            statement.setInt(4, count);
            return readUsers(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<List<User>> queryUsers(String query, Integer id, Integer start, Integer count) {
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, start);
            statement.setInt(3, count);
            return readUsers(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<List<User>> readUsers(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPicture(rs.getString("picture"));
                user.setStatus(rs.getString("status"));
                user.setRole(rs.getString("role"));
                user.setBubbleId(rs.getObject("bubble_id") != null ? rs.getInt("bubble_id") : null);
                users.add(user);
            }
            return Optional.of(users);
        }
    }

    private void mapBasic(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setPicture(rs.getString("picture"));
        user.setStatus(rs.getString("status"));
        user.setRole(rs.getString("role"));
        user.setBubbleId(rs.getObject("bubble_id") != null ? rs.getInt("bubble_id") : null);
    }
}
