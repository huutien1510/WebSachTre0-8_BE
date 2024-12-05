package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.entity.Contestant;

import java.util.List;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant,Integer> {

    @Query("SELECT c FROM Contestant c WHERE c.contest.id= :contestID AND c.account.id= :accountID")
    public Contestant isRegister(@Param("accountID") Integer accountID,@Param("contestID") Integer contestID);

    @Query("SELECT c FROM Contestant c WHERE c.contest.id= :contestID")
    public List<Contestant> getContestantByContest(@Param("contestID") Integer contestID);
}
