package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.tdar.test.SimpleAppConfiguration;

/**
 * This template demonstrates how to develop a standalone test case for Hibernate ORM.  Although this is perfectly
 * acceptable as a reproducer, usage of ORMUnitTestCase is preferred!
 */

@ContextConfiguration(classes = SimpleAppConfiguration.class)
public class ORMStandaloneTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	@Before
	public void setup() {
//		StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder()
//			// Add in any settings that are specific to your test. See resources/hibernate.properties for the defaults.
//			.applySetting( "hibernate.show_sql", "true" )
//			.applySetting( "hibernate.format_sql", "true" )
//			.applySetting( "hibernate.hbm2ddl.auto", "update" );
//
//		Metadata metadata = new MetadataSources( srb.build() )
//		// Add your entities here.
//		//	.addAnnotatedClass( Foo.class )
//			.buildMetadata();
//
//		sessionFactory = metadata.buildSessionFactory();
	}

	// Add your tests, using standard JUnit.

	@Autowired
	SessionFactory sessionFactory;
	
	@Test
	public void hhh123Test() throws Exception {
	    Session session = sessionFactory.getCurrentSession();
	    String QUERY_HQL_MANY_TO_ONE_REFERENCES = "from InformationResource r1 where r1.publisher.id in (:idlist)";
        Query query = session.createQuery(QUERY_HQL_MANY_TO_ONE_REFERENCES);
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        query.setParameterList("idlist", idList);

        query.setFetchSize(10);
        ScrollableResults scroll = query.scroll(ScrollMode.FORWARD_ONLY);

	    
	}
}
