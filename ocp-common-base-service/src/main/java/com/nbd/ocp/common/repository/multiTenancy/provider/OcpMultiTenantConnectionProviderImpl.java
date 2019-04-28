package com.nbd.ocp.common.repository.multiTenancy.provider;

import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import lombok.Data;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个类是Hibernate框架拦截sql语句并在执行sql语句之前更换数据源提供的类
 * @author lanyuanxiaoyao
 * @version 1.0
 */
public class OcpMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final Logger logger= LoggerFactory.getLogger(OcpMultiTenantConnectionProviderImpl.class);
    private static ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    private static DataConfig dataConfigModel;
    public OcpMultiTenantConnectionProviderImpl(){
        initDefaultConnectionProviderForTenant();
    }
    /**
     *在没有提供tenantId的情况下返回默认数据源
      */

    @Override
    protected DataSource selectAnyDataSource() {
        return getTenantDataSource(InvocationInfoProxy.getTenantId());
    }
    static {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://127.0.0.1:3306/nbd?useUnicode=yes&characterEncoding=UTF-8&useSSL=false");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("passw0rd");
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");

        DataSourceBuilder dataSourceBuilder1 = DataSourceBuilder.create();
        dataSourceBuilder1.url("jdbc:mysql://127.0.0.1:3306/nbd1?useUnicode=yes&characterEncoding=UTF-8&useSSL=false");
        dataSourceBuilder1.username("root");
        dataSourceBuilder1.password("passw0rd");
        dataSourceBuilder1.driverClassName("com.mysql.jdbc.Driver");

        dataSourceMap.put("nbd1",dataSourceBuilder1.build());
        dataSourceMap.put("nbd",dataSourceBuilder.build());
    }
    /**
     提供了tenantId的话就根据ID来返回数据源
      */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return getTenantDataSource(tenantIdentifier);
    }

    /**
     *根据传进来的tenantId决定返回的数据源
      */

    public static DataSource getTenantDataSource(String tenantId) {
        if(dataSourceMap.containsKey(tenantId)){
            return dataSourceMap.get(tenantId);
        }else{
            return dataConfigModel.getDataSource();
        }
    }

    private void initDefaultConnectionProviderForTenant(){
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String driverClassName=properties.getProperty("spring.datasource.driver-class-name");
        String url=properties.getProperty("spring.datasource.url");
        String username=properties.getProperty("spring.datasource.username");
        String password=properties.getProperty("spring.datasource.password");
        dataConfigModel=new DataConfig(driverClassName,url,username,password);
        dataSourceMap.put("default",dataConfigModel.getDataSource());
    }

    @Data
    public static class DataConfig{
        private String driverClassName;
        private String username;
        private String password;
        private String url;
        private String urlFormat;
        private DataSource dataSource;
        public  DataConfig(String driverClassName,String url,String username,String password) {
            this.driverClassName=driverClassName;
            this.username=username;
            this.password=password;
            this.url=url;
            this.dataSource=buildDataSource(url);
        }
        public DataSource buildDataSource(String url){
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(url);
            dataSourceBuilder.username(username);
            dataSourceBuilder.password(password);
            dataSourceBuilder.driverClassName(driverClassName);
            return dataSourceBuilder.build();
        }
    }
}
