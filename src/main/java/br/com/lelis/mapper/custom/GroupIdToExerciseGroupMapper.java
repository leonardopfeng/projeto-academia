package br.com.lelis.mapper.customMapper;

import br.com.lelis.model.ExerciseGroup;
import br.com.lelis.repositories.ExerciseGroupRepository;
import com.github.dozermapper.core.CustomConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupIdToExerciseGroupMapper implements CustomConverter {

    @Autowired
    private ExerciseGroupRepository repository;

    @Override
    public Object convert(Object source, Object destination, Class<?> sourceClass, Class<?> destinationClass) {
        if (source instanceof ExerciseGroup && destinationClass == Long.class) {
            // Extract ID from ExerciseGroup
            return ((ExerciseGroup) source).getId();
        } else if (source instanceof Long && destinationClass == ExerciseGroup.class) {
            // Fetch ExerciseGroup using ID
            return repository.findById((Long) source)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Group ID: " + source));
        }
        return null;
    }
}
