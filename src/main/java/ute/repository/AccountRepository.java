package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ute.entity.Account;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);
    Account findByEmail(String email);
}
