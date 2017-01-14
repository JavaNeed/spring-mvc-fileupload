package com.websystique.springmvc.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={"com.websystique.springmvc.repository"})
@ComponentScan({ "com.websystique.springmvc.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment environment) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		// dataSource
		entityManagerFactoryBean.setDataSource(dataSource);
		
		// Hibernate JPA Vendor Adapter
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		// package to scan
		entityManagerFactoryBean.setPackagesToScan("com.websystique.springmvc.model");
		
		// JPA Properties
		entityManagerFactoryBean.setJpaProperties(getProperties(environment));

		return entityManagerFactoryBean;
	}


	public Properties getProperties(Environment environment){
		Properties jpaProperties = new Properties();

		//Configures the used database dialect. This allows Hibernate to create SQL that is optimized for the used database.
		jpaProperties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));

		//Specifies the action that is invoked to the database when the Hibernate SessionFactory is created or closed.
		jpaProperties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));

		//If the value of this property is true, Hibernate writes all SQL
		jpaProperties.put("hibernate.show_sql",	environment.getRequiredProperty("hibernate.show_sql"));

		//If the value of this property is true, Hibernate will format the SQL
		jpaProperties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));

		return jpaProperties;
	}


	@Bean
	public DataSource dataSource(Environment environment) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

		return dataSource;
	}

	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}
}