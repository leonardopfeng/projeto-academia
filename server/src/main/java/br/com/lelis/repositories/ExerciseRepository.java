package br.com.lelis.repositories;

import br.com.lelis.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {


    @Query(
            value = "SELECT " +
                    "e.id as exercise_id, " +
                    "e.name as exercise_name, " +
                    "e.video_url as exercise_url," +
                    "e.group_id as exercise_group_id, " +
                    "eg.id as group_id, " +
                    "eg.name as group_name " +
                    "FROM exercises e " +
                    "INNER JOIN exercise_groups eg ON e.group_id = eg.id " +
                    "ORDER BY eg.name, e.name",
            nativeQuery = true
    )
    List<Object[]> findAllWithGroup();
}
