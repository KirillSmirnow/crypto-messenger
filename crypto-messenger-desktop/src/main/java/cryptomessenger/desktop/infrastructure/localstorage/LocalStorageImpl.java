package cryptomessenger.desktop.infrastructure.localstorage;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class LocalStorageImpl implements LocalStorage {

    private final Path basePath = Path.of("./local-storage");
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SneakyThrows
    public LocalStorageImpl() {
        Files.createDirectories(basePath);
    }

    @Override
    @SneakyThrows
    public void save(String key, byte[] value) {
        try {
            lock.writeLock().lock();
            Files.write(basePath.resolve(key), value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @SneakyThrows
    public byte[] getBytes(String key) {
        var path = basePath.resolve(key);
        if (Files.notExists(path)) {
            return new byte[0];
        }
        try {
            lock.readLock().lock();
            return Files.readAllBytes(path);
        } finally {
            lock.readLock().unlock();
        }
    }
}
