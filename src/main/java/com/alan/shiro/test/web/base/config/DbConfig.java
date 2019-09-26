package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.service.props.DbProp;
import com.alan.shiro.test.service.props.RedisProps;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

@Configuration
@MapperScan(basePackages = {"com.alan.shiro.test.dao.api"})
public class DbConfig {

    @Autowired
    DbProp dbProp;
    @Autowired
    RedisProps redisProps;

    @Bean
    public DruidDataSource dataSource(){
        DruidDataSource dataSource =  new DruidDataSource();
        dataSource.setUrl(dbProp.getUrl());
        dataSource.setDriverClassName(dbProp.getDriver());
        dataSource.setUsername(dbProp.getUserName());
        dataSource.setPassword(dbProp.getPassword());
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:com/alan/shiro/test/dao/**/*.xml");
        bean.setMapperLocations(resources);
        PageInterceptor pageInterceptor = new PageInterceptor();
        Interceptor[] interceptors = new org.apache.ibatis.plugin.Interceptor[]{pageInterceptor};
        bean.setPlugins(interceptors);
        return bean;
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(redisProps.getMaxIdle());
        config.setMinIdle(redisProps.getMinIdle());
        config.setTestOnBorrow(redisProps.getTestOneBorrow());
        config.setMaxWaitMillis(redisProps.getMaxWaitMillis());
        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setPoolConfig(jedisPoolConfig());
        connectionFactory.setPort(redisProps.getPort());
        connectionFactory.setHostName(redisProps.getHost());
        connectionFactory.setPassword(redisProps.getPassword());
        connectionFactory.setTimeout(-1);
        connectionFactory.setDatabase(redisProps.getDatabase());
        connectionFactory.setUsePool(true);
        return connectionFactory;
    }

    @Bean(name = "redisCacheTemplate")
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
}
