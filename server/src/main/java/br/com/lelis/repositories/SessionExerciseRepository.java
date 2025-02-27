package br.com.lelis.repositories;

import br.com.lelis.model.SessionExercise;
import br.com.lelis.model.SessionExerciseId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionExerciseRepository extends JpaRepository<SessionExercise, SessionExerciseId> {

    @Query("SELECT s FROM SessionExercise s WHERE s.id.sessionId = :sessionId AND s.id.exerciseId = :exerciseId")
    SessionExercise findBySessionAndExerciseId(@Param("sessionId") long sessionId, @Param("exerciseId") long exerciseId);

    @Query("SELECT s FROM SessionExercise s WHERE s.id.sessionId = :sessionId")
    Page<SessionExercise> findAllBySessionId(@Param("sessionId") long sessionId, Pageable pageable);

}
