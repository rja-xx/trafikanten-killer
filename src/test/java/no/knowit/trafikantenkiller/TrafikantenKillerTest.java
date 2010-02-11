
package no.knowit.trafikantenkiller;

import junit.framework.Assert;
import no.knowit.trafikantenkiller.model.nodes.Station;
import no.knowit.trafikantenkiller.propertyutils.ApplickationProperties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * Unit test for simple App.
 */
public class TrafikantenKillerTest 
{

	
	private static EmbeddedGraphDatabase database;

	@BeforeClass
	public static void initNeo4jDatabase(){
		ApplickationProperties properties = ApplickationProperties.getInstance();
		String databaseLocation = properties.getDatabaseLocation();
		database = new EmbeddedGraphDatabase(databaseLocation);
	}

	@Test
	public void testHentingAvHopOptimertRute(){
		Station majorstuen = Stations.MAJORSTUEN.getStationFromDatabase(database);
		Station jernbanetorget = Stations.JERNBANETORGET.getStationFromDatabase(database);
		Routeplanner planlegger = RouteplannerFactory.getBytteOptimertRuteplanlegger(database);
		Route route = planlegger.planRute(jernbanetorget, majorstuen);
		Assert.assertNotNull(route);
		Assert.assertNotNull(route.iterator());
		Assert.assertTrue(route.iterator().hasNext());
	}
	
	@AfterClass
	public static void exitNeo4jDatabase(){
		database.shutdown();
	}

	
}
