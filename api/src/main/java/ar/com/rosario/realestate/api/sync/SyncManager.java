package ar.com.rosario.realestate.api.sync;

import ar.com.rosario.realestate.persistence.entity.JpaSyncOutbox;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataSyncOutboxRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Drains sync_outbox: picks up unprocessed records and marks them processed.
 * In a full deployment, events are forwarded to a cloud gateway before ack.
 */
@Component
public class SyncManager {

    private static final Logger log = LoggerFactory.getLogger(SyncManager.class);

    private final SpringDataSyncOutboxRepo outbox;

    public SyncManager(SpringDataSyncOutboxRepo outbox) {
        this.outbox = outbox;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void drain() {
        List<JpaSyncOutbox> pending = outbox.findPendingOrderByCreatedAt();
        if (pending.isEmpty()) return;
        log.info("Draining {} outbox events", pending.size());
        for (JpaSyncOutbox event : pending) {
            try {
                process(event);
                event.setProcessedAt(Instant.now());
                outbox.save(event);
            } catch (Exception e) {
                event.setAttempts(event.getAttempts() + 1);
                event.setLastError(e.getMessage());
                outbox.save(event);
                log.warn("Failed to process outbox event {}: {}", event.getId(), e.getMessage());
            }
        }
    }

    private void process(JpaSyncOutbox event) {
        log.debug("Sync {} {} tenant={}", event.getOp(), event.getEntityId(), event.getTenantId());
        // Future: forward to cloud API or message broker
    }
}
