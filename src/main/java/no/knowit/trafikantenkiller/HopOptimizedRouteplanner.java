package no.knowit.trafikantenkiller;

import no.knowit.trafikantenkiller.model.nodes.Station;
import no.knowit.trafikantenkiller.model.relationships.Traveltype;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HopOptimizedRouteplanner implements Routeplanner {

	private final EmbeddedGraphDatabase database;

	public HopOptimizedRouteplanner(EmbeddedGraphDatabase database) {
		this.database = database;
	}

	@Override
	public Route planRute(Station start, final Station dest) {
		Node startNode = start.getUnderlyingNode();
		Route res = new Route();
		
		Traverser traverser = startNode.traverse(
		        Traverser.Order.BREADTH_FIRST, 
		        new StopEvaluator(){
					@Override
					public boolean isStopNode(TraversalPosition currentPos) {
						long currentNodeId = currentPos.currentNode().getId();
						long destinationNodeId = dest.getUnderlyingNode().getId();
						
						return currentNodeId == destinationNodeId;
					}
		        },
		        ReturnableEvaluator.ALL,
		        Direction.OUTGOING);
		    
		for (Node node : traverser) {
			String name = (String) node.getProperty("name");
			RouteElement routeElement = new RouteElement(name);
			res.addRouteelement(routeElement);
		}

		return res;
	}

}
