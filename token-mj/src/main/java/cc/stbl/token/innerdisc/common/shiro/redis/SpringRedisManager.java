package cc.stbl.token.innerdisc.common.shiro.redis;

import org.crazycake.shiro.BaseRedisManager;
import org.crazycake.shiro.IRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;


public class SpringRedisManager extends BaseRedisManager implements IRedisManager {

    private Logger logger = LoggerFactory.getLogger(SpringRedisManager.class);

    private JedisConnectionFactory jedisConnectionFactory;

    public SpringRedisManager(JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    @Override
    protected Jedis getJedis() {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        if(redisConnection instanceof JedisConnection) {
            return ((JedisConnection) redisConnection).getNativeConnection();
        }
//        if(redisConnection instanceof RedisClusterConnection) return ((RedisClusterConnection)redisConnection).getNativeConnection();
        throw new RuntimeException("RedisClusterConnection not impl");
    }
}
