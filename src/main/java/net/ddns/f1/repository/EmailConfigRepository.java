package net.ddns.f1.repository;

import net.ddns.f1.domain.EmailConfig;
import org.springframework.data.repository.CrudRepository;

public interface EmailConfigRepository extends CrudRepository<EmailConfig, String> {
}
