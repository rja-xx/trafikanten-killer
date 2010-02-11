package no.knowit.trafikantenkiller;

import org.neo4j.kernel.EmbeddedGraphDatabase;

public class RouteplannerFactory {

	public static Routeplanner getBytteOptimertRuteplanlegger(EmbeddedGraphDatabase database) {
		return new HopOptimizedRouteplanner(database);
	}

	
	
}
