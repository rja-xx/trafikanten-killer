package no.knowit.trafikantenkiller.model.util;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public abstract class Neo4JOperation {

	public void executeInTransaction(EmbeddedGraphDatabase database) throws Exception{
		Transaction tx = database.beginTx();
		try{
			this.operation();
			tx.success();
		}catch (Exception e) {
			tx.failure();
			throw e;
		}finally{
			tx.finish();
		}
	}

	protected abstract void operation();
	
}
