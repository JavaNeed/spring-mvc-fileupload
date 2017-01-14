
HelloWorldConfiguration
`
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.websystique.springmvc")
public class HelloWorldConfiguration extends WebMvcConfigurerAdapter{

	@Bean(name="multipartResolver")
	public StandardServletMultipartResolver resolver(){
		return new StandardServletMultipartResolver();
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		registry.viewResolver(viewResolver);
	}


	// Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

	//Configure MessageSource to lookup any validation/error message in internationalized property files
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}

	/**Optional. It's only required when handling '.' in @PathVariables which otherwise ignore everything after last '.' in @PathVaidables argument.
	 * It's a known bug in Spring [https://jira.spring.io/browse/SPR-6164], still present in Spring 4.1.7.
	 * This is a workaround for this issue.
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer matcher) {
		matcher.setUseRegisteredSuffixPatternMatch(true);
	}
}
`

HelloWorldInitializer
`
public class HelloWorldInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/*Set these variables for your project needs*/ 

	private static final String LOCATION = "C:/mytemp/";
	private static final long MAX_FILE_SIZE = 1024 * 1024 * 25;//25MB
	private static final long MAX_REQUEST_SIZE = 1024 * 1024 * 30;//30MB
	private static final int FILE_SIZE_THRESHOLD = 0;

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { HelloWorldConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setMultipartConfig(getMultipartConfigElement());
	}

	private MultipartConfigElement getMultipartConfigElement(){
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
		return multipartConfigElement;
	}
}
`

HibernateConfiguration
`
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
`
