package com.ubb.domain_layer.entities;

public abstract class User extends Entity<Long>{

    private String username;
    private String password;
    private String email;

    /**
     * Creates an instance of an app User
     * @param id the ID of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email the email of the user
     */
    public User(Long id, String username, String password, String email) {
        super(id);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Returns the username of the app user
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the username of the app user
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the app user
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Changes the password of the user
     * @param password the new user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email of the user
     * @return the user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email of the user
     * @param email thw new user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Reacts to an Event finishing
     */
    public void react(){
    }

    public String toString(){
        return username;
    }
}

