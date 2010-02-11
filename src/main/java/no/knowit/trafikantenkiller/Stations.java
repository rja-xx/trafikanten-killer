package no.knowit.trafikantenkiller;



import org.apache.log4j.Logger;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import no.knowit.trafikantenkiller.model.nodes.Station;

public enum Stations {

	MAJORSTUEN(1), JERNBANETORGET(2);
	
	private final int nodeId;

	private Stations(int nodeId){
		this.nodeId = nodeId;
	}

	public Station getStationFromDatabase(EmbeddedGraphDatabase database) {
		Transaction transaction = database.beginTx();
		Station res = null;
		try{
			res = new Station(database.getNodeById(this.nodeId));
			transaction.success();
		}catch(Exception e){
			Logger.getLogger(Station.class).error("Unable to retreive "+this.name()+" from the graph-database.", e);
			transaction.failure();
		}finally{
			transaction.finish();
		}
		return res;
	}
	
}
