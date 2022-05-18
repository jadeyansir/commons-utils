package top.jadeyan.commons.redisson;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 客户端配置
 * 主要处理redis 分布式锁，
 * 不用的主要原因是 codis 有些命令不支持
 *
 * @author yan
 * @date 2022/03/08
 **/
@Configuration
public class RedissonClientConfig {

    @Value("${redisson.redis.host}")
    private String redisHost;
    @Value("${redisson.redis.port}")
    private Integer redisPort;
    @Value("${redisson.redis.database}")
    private Integer redisDatabase;
    @Value("${redisson.redis.password}")
    private String redisPassword;

    /**
     * 创建 redisson 客户端
     *
     * @return redisson 客户端
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = String.format("redis://%s:%d", redisHost, redisPort);
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase);
        if (StringUtils.isNoneBlank(redisPassword)) {
            singleServerConfig.setPassword(redisPassword);
        }
        return Redisson.create(config);
    }
}
