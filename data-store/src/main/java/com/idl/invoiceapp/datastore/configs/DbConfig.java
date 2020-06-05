package com.idl.invoiceapp.datastore.configs;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@Log4j2
public class DbConfig {

    @Autowired
    Environment env;

    @Bean
    @DependsOn("mariaDB4j")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(env.getProperty("jdbc.driverClassName"))
                .url(env.getProperty("jdbc.url"))
                .username(env.getProperty("jdbc.user"))
                .password(env.getProperty("jdbc.pass"))
                .build();
        //com.mariaDB4j.com.mysql.cj.jdbc.Driver org.mariadb.jdbc.Driver
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean bn = new LocalContainerEntityManagerFactoryBean();
        bn.setDataSource(dataSource());
        bn.setPackagesToScan(new String[]{"com.idl.invoiceapp.datastore.model"});

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        bn.setJpaVendorAdapter(vendorAdapter);
        bn.setJpaProperties(hibernateProperties());

        return bn;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean session = new LocalSessionFactoryBean();
        session.setDataSource(dataSource());
        session.setPackagesToScan(new String[]{"com.idl.invoiceapp.datastore.model"});
        session.setHibernateProperties(hibernateProperties());
        return session;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txn = new HibernateTransactionManager(sessionFactory);
        return txn;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public MariaDB4jSpringService mariaDB4j() {
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService(){

            @Override
            public void start() {
                super.start();
                try{
                    getDB().createDB("invoices", "dbAdmin", "dbAdmin");
                }catch(Exception ex){
                    log.error(ex);
                }
            }
        };

        return mariaDB4jSpringService;
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
//                setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
//                setProperty("hibernate.connection.password","dbAdmin");
//                setProperty("hibernate.connection.username","dbAdmin");
//                setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3308/invoices");//jdbc:mariadb://localhost:3308/invoices
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
            }
        };
    }
}
