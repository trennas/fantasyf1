package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import fantasyf1.domain.SeasonInformation;

public interface SeasonInformationRepository extends CrudRepository<SeasonInformation, String> {
	List<SeasonInformation> findByYear(final String year);
}
