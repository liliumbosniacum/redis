package com.lilium.redis.service;

import com.lilium.redis.service.locker.DistributedLocker;
import com.lilium.redis.service.locker.LockExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * Service used to test out our other implementations.
 *
 * @author mirza.
 */
@Service
public class PlaygroundService {
    private static final Logger LOG = LoggerFactory.getLogger(PlaygroundService.class);

    private final DistributedLocker locker;

    @Autowired
    public PlaygroundService(DistributedLocker locker) {
        this.locker = locker;
    }

    @PostConstruct
    private void setup() {
        CompletableFuture.runAsync(() -> runTask("1", 3000));
        CompletableFuture.runAsync(() -> runTask("2", 1000));
        CompletableFuture.runAsync(() -> runTask("3", 100));
    }

    private void runTask(final String taskNumber, final long sleep) {
        LOG.info("Running task : '{}'", taskNumber);

        LockExecutionResult<String> result = locker.lock("some-key", 5, 6, () -> {
            LOG.info("Sleeping for '{}' ms", sleep);
            Thread.sleep(sleep);
            LOG.info("Executing task '{}'", taskNumber);
            return taskNumber;
        });

        LOG.info("Task result : '{}' -> exception : '{}'", result.getResultIfLockAcquired(), result.hasException());
    }
}
