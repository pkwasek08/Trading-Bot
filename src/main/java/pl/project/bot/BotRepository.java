package pl.project.bot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends CrudRepository<BotsEntity, Integer> {
}
