package no.knowit.trafikantenkiller.init;

import no.knowit.trafikantenkiller.model.nodes.Station;
import no.knowit.trafikantenkiller.model.relationships.Traveltype;
import no.knowit.trafikantenkiller.propertyutils.ApplickationProperties;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class Initializer {

	/**
	 * This program initializes the graph-database for the "trafikanten-killer" nosql workshop.
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		ApplickationProperties properties = ApplickationProperties.getInstance();
		String databaseLocation = properties.getDatabaseLocation();
		EmbeddedGraphDatabase database = new EmbeddedGraphDatabase(databaseLocation);

		boolean alreadyInitiated = checkIfDatabaseIsAlreadyInitiated(database);
		if(!alreadyInitiated){
			initDatabase(database);
			System.out.println("Databasen er nå initiert!");
		}else{
			System.out.println("Databasen var allerede initiert!");
		}

		database.shutdown();
	}

	private static boolean checkIfDatabaseIsAlreadyInitiated(EmbeddedGraphDatabase database) {
		Iterable<Node> nodes = database.getAllNodes();
		int i = 0;
		for (Node node : nodes) {
			i++;
		}
		return i == 6;
	}

	private static void initDatabase(EmbeddedGraphDatabase database) {
		Transaction transaction = database.beginTx();
		try{
			Node emptyNode = database.createNode();
			Station baseNode = new Station(emptyNode);
			baseNode.setName("Utgangsnode");
			
			emptyNode = database.createNode();
			Station majorstuen = new Station(emptyNode);
			majorstuen.setName("Majorstuen");

			emptyNode = database.createNode();
			Station bislett = new Station(emptyNode);
			bislett.setName("Bislett");

			emptyNode = database.createNode();
			Station tullinlokka = new Station(emptyNode);
			tullinlokka.setName("Tullinløkka");
			
			emptyNode = database.createNode();
			Station nasjonalteateret = new Station(emptyNode);
			nasjonalteateret.setName("Nasjonalteateret");
			
			emptyNode = database.createNode();
			Station jernbanetorget= new Station(emptyNode);
			jernbanetorget.setName("Jernbanetorget");
			
			baseNode.getUnderlyingNode().createRelationshipTo(majorstuen.getUnderlyingNode(), Table.STATIONS);
			baseNode.getUnderlyingNode().createRelationshipTo(bislett.getUnderlyingNode(), Table.STATIONS);
			baseNode.getUnderlyingNode().createRelationshipTo(tullinlokka.getUnderlyingNode(), Table.STATIONS);
			baseNode.getUnderlyingNode().createRelationshipTo(nasjonalteateret.getUnderlyingNode(), Table.STATIONS);
			baseNode.getUnderlyingNode().createRelationshipTo(jernbanetorget.getUnderlyingNode(), Table.STATIONS);
			
			makeConnection(majorstuen, nasjonalteateret, 5, Traveltype.SUB);
			makeConnection(majorstuen, nasjonalteateret, 12, Traveltype.TRAM);
			makeConnection(jernbanetorget, nasjonalteateret, 5, Traveltype.SUB);
			makeConnection(jernbanetorget, nasjonalteateret, 8, Traveltype.TRAM);
			makeConnection(jernbanetorget, tullinlokka, 8, Traveltype.TRAM);
			makeConnection(bislett, tullinlokka, 8, Traveltype.TRAM);
			
			transaction.success();
		}
		catch(Exception e){
			Logger.getLogger(Initializer.class).fatal("Kunne ikke skape demonstrasjonsdatabase.", e);
			transaction.failure();
		}finally{
			transaction.finish();
		}
	}

	private static void makeConnection(Station from, Station to, int duration, Traveltype traveltype) {
		Relationship rute = from.getUnderlyingNode().createRelationshipTo(to.getUnderlyingNode(), traveltype);
		rute.setProperty("duration", duration);
		
		rute = to.getUnderlyingNode().createRelationshipTo(from.getUnderlyingNode(), traveltype);
		rute.setProperty("duration", duration);
	}
	
}