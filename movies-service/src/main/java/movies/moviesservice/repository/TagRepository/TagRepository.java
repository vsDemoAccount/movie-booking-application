package movies.moviesservice.repository.TagRepository;


import movies.moviesservice.entity.Tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByCode(String code);
    Optional<Tag> findByName(String name);
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
