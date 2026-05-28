package in.api_2;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Login_API_Repository extends JpaRepository<Login_API, Integer> {

    // ─── EXISTS CHECKS (for duplicate validation) ───────────────────
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);


    // ─── FIND BY SINGLE FIELD ────────────────────────────────────────
    Optional<Login_API> findByEmail(String email);

    Optional<Login_API> findByUserName(String userName);

    List<Login_API> findByMobile(String mobile);


    // ─── FIND BY EMAIL+PASS / USERNAME+PASS (optional - only if not using BCrypt) ───
    // NOTE: These two methods only work for PLAIN TEXT passwords.
    // Since we now use BCrypt in the controller (passwordEncoder.matches()),
    // these methods are NO LONGER NEEDED and kept here only for reference.
    // You can safely DELETE them if you are using BCrypt.

    // Optional<Login_API> findByEmailAndPass(String email, String pass);
    // Optional<Login_API> findByUserNameAndPass(String userName, String pass);

}