package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ute.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
}
