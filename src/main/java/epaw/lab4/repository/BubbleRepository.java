package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Bubble;

public class BubbleRepository extends BaseRepository {

    private static BubbleRepository instance;

    private BubbleRepository() {
        super();
    }

    public static synchronized BubbleRepository getInstance() {
        if (instance == null) {
            instance = new BubbleRepository();
        }
        return instance;
    }

    /** All bubbles with a derived member count, for the map. */
    public List<Bubble> findAll() {
        List<Bubble> bubbles = new ArrayList<>();
        String query = "SELECT b.id, b.name, b.category, b.zip, b.city, b.country, b.lat, b.lng, b.open, b.owner_id,"
                + "(SELECT COUNT(*) FROM user_bubbles m WHERE m.bubble_id = b.id AND m.status = 'APPROVED') AS members "
                + "FROM bubbles b ORDER BY b.name";
        try (PreparedStatement statement = db.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                bubbles.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bubbles;
    }

    /** All bubbles, annotated with the given user's membership status + home flag. */
    public List<Bubble> findAllWithMembership(Integer userId) {
        List<Bubble> bubbles = new ArrayList<>();
        String query = "SELECT b.id, b.name, b.category, b.zip, b.city, b.country, b.lat, b.lng, b.open, b.owner_id,"
                + "(SELECT COUNT(*) FROM user_bubbles m WHERE m.bubble_id = b.id AND m.status = 'APPROVED') AS members, "
                + "(SELECT status FROM user_bubbles m2 WHERE m2.bubble_id = b.id AND m2.user_id = ?) AS membership, "
                + "(SELECT u.bubble_id FROM users u WHERE u.id = ?) AS home_id "
                + "FROM bubbles b ORDER BY b.name";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Bubble b = map(rs);
                    b.setMembership(rs.getString("membership"));
                    Object home = rs.getObject("home_id");
                    b.setHome(home != null && rs.getInt("home_id") == b.getId());
                    bubbles.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bubbles;
    }

    public Optional<Bubble> findById(Integer id) {
        String query = "SELECT b.id, b.name, b.category, b.zip, b.city, b.country, b.lat, b.lng, b.open, b.owner_id,"
                + "(SELECT COUNT(*) FROM user_bubbles m WHERE m.bubble_id = b.id AND m.status = 'APPROVED') AS members "
                + "FROM bubbles b WHERE b.id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Best-effort lookup used to assign a bubble at registration. */
    public Optional<Bubble> findByLocation(String zip, String city, String country) {
        // Try exact zip + country first, then fall back to city + country.
        String exact = "SELECT b.id, b.name, b.category, b.zip, b.city, b.country, b.lat, b.lng, b.open, b.owner_id,0 AS members "
                + "FROM bubbles b WHERE b.zip = ? AND UPPER(b.country) = UPPER(?) LIMIT 1";
        Optional<Bubble> byZip = single(exact, zip, country);
        if (byZip.isPresent()) return byZip;

        String byCity = "SELECT b.id, b.name, b.category, b.zip, b.city, b.country, b.lat, b.lng, b.open, b.owner_id,0 AS members "
                + "FROM bubbles b WHERE LOWER(b.city) = LOWER(?) AND UPPER(b.country) = UPPER(?) LIMIT 1";
        return single(byCity, city, country);
    }

    private Optional<Bubble> single(String query, String a, String b) {
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, a);
            statement.setString(2, b);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Bubble map(ResultSet rs) throws SQLException {
        Bubble b = new Bubble();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setCategory(rs.getString("category"));
        b.setZip(rs.getString("zip"));
        b.setCity(rs.getString("city"));
        b.setCountry(rs.getString("country"));
        b.setLat(rs.getDouble("lat"));
        b.setLng(rs.getDouble("lng"));
        b.setOpen(rs.getInt("open") == 1);
        b.setOwnerId(rs.getObject("owner_id") != null ? rs.getInt("owner_id") : null);
        b.setMemberCount(rs.getInt("members"));
        return b;
    }
}
