package br.com.lelis.repositories;



import br.com.lelis.model.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    @Query("SELECT t FROM TrainingSession t WHERE t.clientId=:clientId")
    Page<TrainingSession> findAllByClientId(@Param("clientId") long clientId, Pageable pageable);

    @Query("SELECT t FROM TrainingSession t WHERE t.coachId=:coachId")
    Page<TrainingSession> findAllByCoachId(@Param("coachId") long coachId, Pageable pageable);
}
