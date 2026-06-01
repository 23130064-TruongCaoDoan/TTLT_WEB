package dao;

import model.AuthToken;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuthTokenDao extends BaseDao {
    public void saveToken(AuthToken token) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    INSERT INTO remember_tokens
                    (user_id, selector, validatorHash, expiresAt)
                    VALUES
                    (:userId, :selector, :validatorHash, :expiresAt)
                    """)
                        .bind("userId", token.getUserId())
                        .bind("selector", token.getSelector())
                        .bind("validatorHash", token.getValidatorHash())
                        .bind("expiresAt", token.getExpiresAt())
                        .execute()
        );
    }

    public Optional<AuthToken> findBySelector(String selector) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                    SELECT *
                    FROM remember_tokens
                    WHERE selector = :selector
                    """)
                        .bind("selector", selector)
                        .map((rs, ctx) -> {
                            AuthToken at = new AuthToken();
                            at.setId(rs.getInt("id"));
                            at.setUserId(rs.getInt("user_id"));
                            at.setSelector(rs.getString("selector"));
                            at.setValidatorHash(rs.getString("validatorHash"));
                            at.setExpiresAt(
                                    rs.getTimestamp("expiresAt")
                                            .toLocalDateTime()
                            );
                            return at;
                        })
                        .findOne()
        );
    }
    public void deleteBySelector(String selector) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    DELETE FROM remember_tokens
                    WHERE selector = :selector
                    """)
                        .bind("selector", selector)
                        .execute()
        );
    }
    public void deleteByUserId(int userId) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    DELETE FROM remember_tokens
                    WHERE user_id = :userId
                    """)
                        .bind("userId", userId)
                        .execute()
        );
    }
}
