package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.persistence.entity.JpaSyncOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataSyncOutboxRepo extends JpaRepository<JpaSyncOutbox, Long> {

    @Query("SELECT e FROM JpaSyncOutbox e WHERE e.processedAt IS NULL ORDER BY e.createdAt ASC")
    List<JpaSyncOutbox> findPendingOrderByCreatedAt();
}
