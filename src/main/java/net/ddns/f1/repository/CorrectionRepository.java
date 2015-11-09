package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Correction;

import org.springframework.data.repository.CrudRepository;

public interface CorrectionRepository extends CrudRepository<Correction, Integer> {
	List<Correction> findByRound(final int round);
}
