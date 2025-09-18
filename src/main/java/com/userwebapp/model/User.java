package com.userwebapp.model;

/**
 * Represents an application user.
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String role;

    // Constructor for creating a new User before persistence
    public User(String username, String fullName, String email) {
        this(0, username, null, fullName, email, "USER");
    }

    // Full constructor (e.g. when loading from the database)
    public User(int id,
                String username,
                String passwordHash,
                String fullName,
                String email,
                String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}