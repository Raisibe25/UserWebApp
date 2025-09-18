package com.userwebapp.dao;

import com.userwebapp.model.User;
import com.userwebapp.util.PasswordUtils;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    // 1. Lookup by username
    private static final String FIND_BY_USERNAME =
            "SELECT id, username, password_hash, full_name, email, role " +
                    "FROM users WHERE username = ?";

    // 2. Insert new user (omit role → uses ENUM default 'USER')
    private static final String INSERT_USER =
            "INSERT INTO users (username, password_hash, full_name, email) " +
                    "VALUES (?, ?, ?, ?)";

    // 3. Update profile (full name, email, optional password)
    //    We'll build this SQL dynamically in updateProfile()

    /**
     * Registers a new user. Returns generated user ID.
     * @param user contains username, fullName, email (role ignored)
     * @param plainPassword raw password to hash
     */
    public int register(User user, String plainPassword) throws SQLException {
        String hash = PasswordUtils.hash(plainPassword);
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, hash);
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    /**
     * Authenticates credentials. Returns a User if valid.
     */
    public Optional<User> authenticate(String username, String plainPassword)
            throws SQLException {
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                String storedHash = rs.getString("password_hash");
                if (!PasswordUtils.safeCheck(plainPassword, storedHash)) {
                    return Optional.empty();
                }

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        null, // do not expose hash
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                return Optional.of(user);
            }
        }
    }

    /**
     * Updates full name, email, and optionally password.
     * Returns true if a row was updated.
     */
    public boolean updateProfile(User user, String newPlainPassword)
            throws SQLException {
        boolean changePassword = newPlainPassword != null && !newPlainPassword.isBlank();
        StringBuilder sql = new StringBuilder(
                "UPDATE users SET full_name = ?, email = ?"
        );
        if (changePassword) {
            sql.append(", password_hash = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setString(idx++, user.getFullName());
            ps.setString(idx++, user.getEmail());

            if (changePassword) {
                String newHash = PasswordUtils.hash(newPlainPassword);
                ps.setString(idx++, newHash);
            }

            ps.setInt(idx, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Finds a user by ID.
     */
    public Optional<User> findById(int id) throws SQLException {
        String sql =
                "SELECT id, username, full_name, email, role " +
                        "FROM users WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        null,
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                return Optional.of(user);
            }
        }
    }
}