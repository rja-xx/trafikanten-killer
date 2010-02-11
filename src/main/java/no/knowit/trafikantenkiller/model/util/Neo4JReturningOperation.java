package no.knowit.trafikantenkiller.model.util;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public abstract class Neo4JReturningOperation<T> {

	protected final EmbeddedGraphDatabase database;

	public Neo4JReturningOperation(EmbeddedGraphDatabase database) {
		this.database = database;
	}

	public T retreiveInTransaction() throws Exception {
		Transaction tx = database.beginTx();
		T result = null;
		try{
			result = this.retreival();
			tx.success();
		}catch (Exception e) {
			tx.failure();
			throw e;
		}finally{
			tx.finish();
		}
		return result;
	}

	protected abstract T retreival();
}
