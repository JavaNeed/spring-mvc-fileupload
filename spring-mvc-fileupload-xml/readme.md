
`

<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:jpa="http://www.springframework.org/schema/data/jpa"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
		
	<!-- Enables the Spring MVC @Controller programming model -->
	<mvc:annotation-driven />
	<context:component-scan base-package="com.websystique.springmvc.controller" />

	<mvc:resources location="/static/" mapping="/static/**" />
	
	<!-- Load database.properties file -->
	<context:property-placeholder location="classpath:application.properties" />
		
	<!-- Enable JPA Repositories -->
	<jpa:repositories base-package="com.websystique.springmvc.repository" />
	
	<!-- Enable Transaction Manager -->
	<tx:annotation-driven transaction-manager="transactionManager" />
	
	<!-- Necessary to get the entity manager injected into the factory bean -->
	<beans:bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />


	<!-- ====== MYSQL DataSource ====== -->
	<beans:bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource" >
		<beans:property name="driverClassName" value="${jdbc.driverClassName}" />
		<beans:property name="url" value="${jdbc.url}" />
		<beans:property name="username" value="${jdbc.username}" />
		<beans:property name="password" value="${jdbc.password}" />
	</beans:bean>

    <!-- ====== Hibernate JPA Vendor Adaptor ======= -->
    <beans:bean id="jpaVendorAdapter" class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
        <beans:property name="showSql" value="${hibernate.show_sql}"/>
        <beans:property name="generateDdl" value="true"/>
        <beans:property name="database" value="${jdbc.database.vendor}"/>
    </beans:bean>
    
    
    <!-- Beans -->
    <beans:bean id="userDocumentService" class="com.websystique.springmvc.service.UserDocumentServiceImpl" />
    <beans:bean id="userService" class="com.websystique.springmvc.service.UserServiceImpl" />
	<beans:bean id="fileValidator" class="com.websystique.springmvc.util.FileValidator" />

	<!-- ======== Entity Manager factory ======== -->
	<beans:bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<!-- Data Source -->
        <beans:property name="dataSource" ref="dataSource"/>
        
        <!-- JPA Vendor Adaptor -->
        <beans:property name="jpaVendorAdapter" ref="jpaVendorAdapter"/>
        
        <!-- spring based scanning for entity classes-->
        <beans:property name="packagesToScan" value="com.websystique.springmvc.model" />
        
        <beans:property name="jpaProperties">
        	<beans:props>
        		<beans:prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</beans:prop>  <!-- validate | update | create | create-drop -->
        		<beans:prop key="hibernate.dialect">${hibernate.dialect}</beans:prop> 
        		<!-- <beans:prop key="hibernate.cache.use_query_cache">true</beans:prop> --> 
        	</beans:props>
        </beans:property>
    </beans:bean>
	
	<!-- ======== Transaction Manager ==== -->
	<beans:bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
		<beans:property name="entityManagerFactory" ref="entityManagerFactory" />
	</beans:bean>

	<beans:bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver" />

	<!-- Localization of hibernate messages during validation!-->
    <beans:bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
        <beans:property name="basename" value="classpath:messages" />
        <beans:property name="defaultEncoding" value="UTF-8" />
    </beans:bean>

    <beans:bean name="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>


	<!-- This is for JSP -->
	<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<beans:property name="prefix" value="/WEB-INF/views/" />
		<beans:property name="suffix" value=".jsp" />
	</beans:bean>	
</beans:beans>

`