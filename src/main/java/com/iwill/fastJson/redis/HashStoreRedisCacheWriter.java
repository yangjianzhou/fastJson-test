/*
package com.iwill.fastJson.redis;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class HashStoreRedisCacheWriter implements RedisCacheWriter {

    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;
    String SEPARATOR = "\\|";

    */
/**
     * @param connectionFactory must not be {@literal null}.
     *//*

    public HashStoreRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO);
    }

    */
/**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *          to disable locking.
     *//*

    public HashStoreRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
    }

    */
/*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#put(java.lang.String, byte[], byte[], java.time.Duration)
     *//*

    @Override
    public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        HashCacheField hashCacheField =  parse(key, true);
        execute(name, connection -> {
//            log.info(">>>>>>>>>put: {} -> {}: {}", new String(hashCacheField.getKey()), new String(hashCacheField.getField()), new String(value));
            if (shouldExpireWithin(ttl)) {
                connection.hSet(hashCacheField.getKey(), hashCacheField.getField(), value);
                connection.pExpire(hashCacheField.getKey(),  ttl.toMillis());
//                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.upsert());
            } else {
                connection.set(key, value);
            }

            return "OK";
        });
    }

    */
/*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#get(java.lang.String, byte[])
     *//*

    @Override
    public byte[] get(String name, byte[] key) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        HashCacheField hashCacheField =  parse(key, true);

        return execute(name, connection -> connection.hGet(hashCacheField.getKey(), hashCacheField.getField()));
    }

    */
/*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#putIfAbsent(java.lang.String, byte[], byte[], java.time.Duration)
     *//*

    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, @Nullable Duration ttl) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        HashCacheField hashCacheField =  parse(key, true);

        return execute(name, connection -> {

            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }

            try {
                if (connection.hSetNX(hashCacheField.getKey(), hashCacheField.getField(), value)) {

                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(hashCacheField.getKey(), ttl.toMillis());
                    }
                    return null;
                }

                return connection.hGet(hashCacheField.getKey(), hashCacheField.getField());
            } finally {

                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    */
/*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#remove(java.lang.String, byte[])
     *//*

    @Override
    public void remove(String name, byte[] key) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        HashCacheField hashCacheField =  parse(key, false);
//        log.info(">>>>>>>>>remove: {}", new String(hashCacheField.getKey()));
        execute(name, connection -> connection.del(hashCacheField.getKey()));
    }

    */
/*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#clean(java.lang.String, byte[])
     *//*

    @Override
    public void clean(String name, byte[] pattern) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");

        throw new IllegalArgumentException("not support allEntries delete");
    }

    */
/**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     *//*

    void lock(String name) {
        execute(name, connection -> doLock(name, connection));
    }

    */
/**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     *//*

    void unlock(String name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    private Boolean doLock(String name, RedisConnection connection) {
        return connection.setNX(createCacheLockKey(name), new byte[0]);
    }

    private Long doUnlock(String name, RedisConnection connection) {
        return connection.del(createCacheLockKey(name));
    }

    boolean doCheckLock(String name, RedisConnection connection) {
        return connection.exists(createCacheLockKey(name));
    }

    */
/**
     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
     *//*

    private boolean isLockingCacheWriter() {
        return !sleepTime.isZero() && !sleepTime.isNegative();
    }

    private <T> T execute(String name, Function<RedisConnection, T> callback) {

        RedisConnection connection = connectionFactory.getConnection();
        try {

            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }

    private void executeLockFree(Consumer<RedisConnection> callback) {

        RedisConnection connection = connectionFactory.getConnection();

        try {
            callback.accept(connection);
        } finally {
            connection.close();
        }
    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {

        if (!isLockingCacheWriter()) {
            return;
        }

        try {

            while (doCheckLock(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {

            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();

            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name),
                    ex);
        }
    }

    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    private static byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }


    private HashCacheField parse(byte[] keyBytes, boolean needField) {
        String keyString = new String(keyBytes);
        String[] arr = keyString.split(SEPARATOR);
        if (needField && arr.length != 2)
            throw new IllegalArgumentException("The format of the key is incorrect, should be {hashKey}|{hashField}");
        if (!needField && arr.length != 1)
            throw new IllegalArgumentException("The format of the key is incorrect, should be {hashKey}");
        return new HashCacheField(arr[0].getBytes(),
                arr.length == 2 ? arr[1].getBytes() : null);
    }

    private class HashCacheField{
        private byte[] key;
        private byte[] field;
        public HashCacheField(byte[] key, byte[] field) {
            this.key = key;
            this.field = field;
        }
        public byte[] getKey() {
            return key;
        }

        public void setKey(byte[] key) {
            this.key = key;
        }

        public void setField(byte[] field) {
            this.field = field;
        }

        public byte[] getField() {
            return field;
        }

        @Override
        public String toString() {
            return "HashCacheField{" +
                    "key='" + key + '\'' +
                    ", field='" + field + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashCacheField that = (HashCacheField) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, field);
        }
    }
}
*/
