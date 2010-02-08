package no.knowit.neo4j;

import java.io.IOException;

import junit.framework.Assert;
import no.knowit.neo4j.relationships.MyRelationshipTypes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.omg.CORBA.DATA_CONVERSION;

/**
 * Unit test for simple App.
 */
public class Neo4JTest 
{

	private static EmbeddedGraphDatabase database;

	@BeforeClass
	public static void initNeo4jDatabase(){
		ApplickationProperties properties = ApplickationProperties
		.getInstance();
		String databaseLocation = properties.getDatabaseLocation();
		database = new EmbeddedGraphDatabase(databaseLocation);
	}

	@AfterClass
	public static void exitNeo4jDatabase(){
		database.shutdown();
	}

	@Test
	public void testOppsettAvRelasjoner(){
		new Neo4JOperation(){
			@Override
			protected void operation() {
				Node node1 = database.createNode();
				Node node2 = database.createNode();
				Relationship relationship = node1.createRelationshipTo(node2, MyRelationshipTypes.KNOWS);
				node1.setProperty("name", "node en");
				node2.setProperty("name", "node to");
				relationship.setProperty("description", "har kjennskap tell");				
			}
		}.executeInTransaction(database);
	}

	@Test
	public void testTraversing(){
		String rels = new Neo4JReturningOperation<String>(database){
			@Override
			protected String retreival() {
				StringBuilder sb = new StringBuilder();
				Iterable<Node> nodes = database.getAllNodes();
				Node node1 = null;
				for (Node n : nodes) {
					if(n.getProperty("name", "").equals("node en")){
						node1 = n;
					}
				}
				Assert.assertNotNull("Node en skal finnes i databasen", node1);
				Iterable<Relationship> relationships = node1.getRelationships(MyRelationshipTypes.KNOWS, Direction.OUTGOING);
				for (Relationship relationship : relationships) {
					String description = (String) relationship.getProperty("description", "");
					String node1name = (String) relationship.getStartNode().getProperty("name", "");
					Object node2name = relationship.getEndNode().getProperty("name", "");
					sb.append(node1name).append(" ").append(description).append(" ").append(node2name).append("\n");
				}
				return sb.toString();
			}

		}.retreiveInTransaction();
		Assert.assertNotSame("Relasjonen skal ha en beskrivelse", "", rels);
		System.out.println(rels);
	}

	@Test
	public void testShell() throws IOException{
		Assert.assertTrue(database.enableRemoteShell());
//		char read = (char) System.in.read();
//		while(read != 'q'){
//			read = (char) System.in.read();	
//		}
	}
	
}
