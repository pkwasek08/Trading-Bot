package pl.project.bot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotRepository extends CrudRepository<BotsEntity, Long> {
    Optional<BotsEntity> deleteBotsEntityById(Long id);
}
