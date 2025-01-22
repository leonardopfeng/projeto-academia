package br.com.lelis.repositories;

import br.com.lelis.model.ExerciseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseGroupRepository extends JpaRepository<ExerciseGroup, Long> {
}
