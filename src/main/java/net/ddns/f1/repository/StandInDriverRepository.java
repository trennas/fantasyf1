package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.StandInDriver;

import org.springframework.data.repository.CrudRepository;

public interface StandInDriverRepository extends CrudRepository<StandInDriver, Integer> {
	List<StandInDriver> findByRoundAndStandingInFor(final Integer round, final Driver standingInFor);
}
