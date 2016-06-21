package org.tdar.test;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@EnableTransactionManagement()
@EnableAspectJAutoProxy(proxyTargetClass = true)
// NOTE: this exclude filter is to ensure that we don't instantiate every @Configuration by default. @Configuration is a subclass of @Component, so the
// autowiring happens by default w/o this
@ComponentScan(basePackages = { "org.tdar" },
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                SimpleAppConfiguration.class
                        })
        })
@Configuration
public class SimpleAppConfiguration implements Serializable {

    protected static final String HIBERNATE_PROPERTIES = "hibernate.properties";
    private static final long serialVersionUID = 2190713147269025044L;
    public transient Logger logger = LoggerFactory.getLogger(getClass());
    public static transient Logger staticLogger = LoggerFactory.getLogger(SimpleAppConfiguration.class);
    Properties properties = new Properties();

    public SimpleAppConfiguration() {
        logger.debug("Initializing Simple Application Context");

        try {
            properties.load(new FileInputStream(new File("src/test/resources/" , HIBERNATE_PROPERTIES)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * tDAR primarily uses the SLF4j fascade for all logging of the application, but then filters that through to log4j2 on the backend.
         * This does produce some complexities with Hibernate. These issues are related to the following:
         * * hibernate tries to auto-discover the logging source
         * * some versions of jboss-logging will introduce their own custom versions of log4j
         * * commons-logging and other logging options can also produce conflicts.
         */
//        System.setProperty("org.jboss.logging.provider", "slf4j");

        System.setProperty("java.awt.headless", "true");
        ImageIO.scanForPlugins();
    }

    @Autowired
    protected Environment env;
//    private SessionFactory buildSessionFactory;

    @Bean
    @Primary
    public HibernateTransactionManager transactionManager()
            throws PropertyVetoException, FileNotFoundException, IOException, URISyntaxException {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager(getSessionFactory());
        return hibernateTransactionManager;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory()
            throws FileNotFoundException, IOException, URISyntaxException, PropertyVetoException {
        ComboPooledDataSource ds = getDataSource();
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(ds);

        builder.scanPackages(new String[] { "org.tdar" });
        builder.addProperties(properties);
//        SessionBuilder sessionBuilder = builder.buildSessionFactory().withOptions().eventListeners(new FilestoreLoggingSessionEventListener());
        return builder.buildSessionFactory();
    }

    @Bean
    public ComboPooledDataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setProperties(properties);
        ds.setJdbcUrl(properties.getProperty("hibernate.connection.url"));
        ds.setDriverClass(properties.getProperty("hibernate.connection.driver_class"));
        return ds;
    }


}
