package epaw.lab4.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import epaw.lab4.model.Bubble;
import epaw.lab4.model.User;
import epaw.lab4.repository.BubbleRepository;
import epaw.lab4.repository.MembershipRepository;
import epaw.lab4.repository.UserRepository;
import jakarta.servlet.http.Part;

/**
 * Business logic for accounts: registration (with validation, hashing and
 * automatic Bubble assignment by location), login, the follow graph and
 * profile-picture storage.
 */
public class UserService {

    private static UserService instance;
    private final UserRepository userRepository;
    private final BubbleRepository bubbleRepository;
    private final MembershipRepository membershipRepository;

    private static final String NAME_RE     = "^[A-Za-zÀ-ÿ\\s\\-']{2,60}$";
    private static final String EMAIL_RE    = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final String PHONE_RE    = "^\\d{7,15}$";
    private static final String DNI_RE      = "^\\d{8}[A-Za-z]$";
    private static final String ZIP_RE      = "^\\d{5}$";
    private static final String USERNAME_RE = "^[A-Za-z0-9_]{4,30}$";
    private static final String PASSWORD_RE = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$";

    private static final Set<String> BLOCKED_USERNAMES = Set.of("admin", "root", "administrator");

    private UserService() {
        this.userRepository = UserRepository.getInstance();
        this.bubbleRepository = BubbleRepository.getInstance();
        this.membershipRepository = MembershipRepository.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Map<String, String> register(User user, String passwordConfirm) {
        Map<String, String> errors = new LinkedHashMap<>();

        // --- Format validations ---
        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Full name is required.");
        } else if (!name.matches(NAME_RE)) {
            errors.put("name", "Name must be 2–60 letters (accents, hyphens and apostrophes allowed).");
        }

        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!email.matches(EMAIL_RE)) {
            errors.put("email", "Enter a valid email address.");
        }

        String phone = user.getPhone();
        if (phone != null && !phone.trim().isEmpty() && !phone.matches(PHONE_RE)) {
            errors.put("phone", "Phone must be 7–15 digits (no spaces or dashes).");
        }

        String dni = user.getDni();
        if (dni == null || dni.trim().isEmpty()) {
            errors.put("dni", "DNI is required.");
        } else if (!dni.matches(DNI_RE)) {
            errors.put("dni", "DNI must be 8 digits followed by a letter (e.g. 12345678A).");
        }

        String zip = user.getZip();
        if (zip == null || zip.trim().isEmpty()) {
            errors.put("zip", "ZIP code is required.");
        } else if (!zip.matches(ZIP_RE)) {
            errors.put("zip", "ZIP code must be exactly 5 digits.");
        }

        if (user.getCity() == null || user.getCity().trim().isEmpty()) {
            errors.put("city", "City is required.");
        }

        String country = user.getCountry();
        if (country == null || country.trim().isEmpty()) {
            errors.put("country", "Country code is required.");
        } else if (country.length() != 2) {
            errors.put("country", "Country must be a 2-letter ISO code (e.g. ES, US, FR).");
        }

        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (!username.matches(USERNAME_RE)) {
            errors.put("username", "Username must be 4–30 characters (letters, digits, underscores).");
        } else if (BLOCKED_USERNAMES.contains(username.toLowerCase())) {
            errors.put("username", "That username is reserved. Please choose another.");
        }

        String password = user.getPassword();
        if (password == null || !password.matches(PASSWORD_RE)) {
            errors.put("password", "Password needs 8+ characters with an uppercase letter, a digit and a special character (!@#$%^&*).");
        } else if (!password.equals(passwordConfirm)) {
            errors.put("confirmPassword", "Passwords do not match.");
        }

        if (!errors.isEmpty()) return errors;

        // --- Uniqueness checks ---
        if (userRepository.existsByEmail(email))      errors.put("email", "This email address is already registered.");
        if (userRepository.existsByDni(dni))          errors.put("dni", "This DNI is already registered.");
        if (userRepository.existsByUsername(username)) errors.put("username", "This username is already taken.");
        if (!errors.isEmpty()) return errors;

        // --- Automatic Bubble assignment by location ---
        Optional<Bubble> bubble = bubbleRepository.findByLocation(zip, user.getCity(), country);
        if (bubble.isPresent()) {
            user.setBubbleId(bubble.get().getId());
            // Open bubbles approve immediately; closed ones need admin approval.
            user.setStatus(bubble.get().isOpen() ? "APPROVED" : "PENDING");
        } else {
            user.setBubbleId(null);
            user.setStatus("APPROVED"); // no local bubble yet — can still browse the map
        }

        user.setRole("USER");
        user.setDni(dni.toUpperCase());
        user.setCountry(country.toUpperCase());
        user.setPassword(DigestUtils.sha256Hex(password));

        int id = userRepository.save(user);
        user.setId(id);
        // Record the home bubble as the user's first membership.
        if (id > 0 && user.getBubbleId() != null) {
            membershipRepository.setStatus(id, user.getBubbleId(), user.getStatus());
        }
        return errors;
    }

    /**
     * Validate and persist the editable profile fields (name, username,
     * picture). Username keeps its format rules and must stay unique across
     * other accounts. Returns a (possibly empty) map of field errors.
     */
    public Map<String, String> updateProfile(User user) {
        Map<String, String> errors = new LinkedHashMap<>();

        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Full name is required.");
        } else if (!name.matches(NAME_RE)) {
            errors.put("name", "Name must be 2–60 letters (accents, hyphens and apostrophes allowed).");
        }

        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (!username.matches(USERNAME_RE)) {
            errors.put("username", "Username must be 4–30 characters (letters, digits, underscores).");
        } else if (BLOCKED_USERNAMES.contains(username.toLowerCase())) {
            errors.put("username", "That username is reserved. Please choose another.");
        }

        if (!errors.isEmpty()) return errors;

        if (userRepository.existsByUsernameExcept(username, user.getId())) {
            errors.put("username", "This username is already taken.");
            return errors;
        }

        userRepository.updateProfile(user);
        return errors;
    }

    public Map<String, String> login(User user) {
        Map<String, String> errors = new LinkedHashMap<>();
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        if (!userRepository.checkLogin(user)) {
            errors.put("password", "Username or password is incorrect.");
        }
        return errors;
    }

    // ---------- follow graph ----------

    public List<User> getFollowedUsers(Integer id, Integer start, Integer count) {
        return userRepository.findFollowed(id, start, count).orElse(null);
    }

    public List<User> getNotFollowedUsers(Integer id, Integer start, Integer count) {
        return userRepository.findNotFollowed(id, start, count).orElse(null);
    }

    public void follow(Integer followerId, Integer followeeId) {
        if (followerId != null && followeeId != null && !followerId.equals(followeeId)) {
            userRepository.followUser(followerId, followeeId);
        }
    }

    public void unfollow(Integer followerId, Integer followeeId) {
        userRepository.unfollowUser(followerId, followeeId);
    }

    // ---------- files ----------

    public String saveProfilePicture(Part filePart, String username) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }
        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = username + extension;
            String resourcesDir = "EXTERNAL_RESOURCES";
            Files.createDirectories(Paths.get(resourcesDir));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
