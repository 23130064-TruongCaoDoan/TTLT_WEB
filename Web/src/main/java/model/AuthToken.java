package model;

import java.time.LocalDateTime;

public class AuthToken {
    private int id;
    private int userId;
    private String selector;
    private String validatorHash;
    private LocalDateTime expiresAt;

    public AuthToken() {
    }

    public AuthToken(int userId, String selector, String validatorHash, LocalDateTime expiresAt) {
        this.userId = userId;
        this.selector = selector;
        this.validatorHash = validatorHash;
        this.expiresAt = expiresAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getValidatorHash() {
        return validatorHash;
    }

    public void setValidatorHash(String validatorHash) {
        this.validatorHash = validatorHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
