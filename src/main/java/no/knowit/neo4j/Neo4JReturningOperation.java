package no.knowit.neo4j;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public abstract class Neo4JReturningOperation<T> {

	private final EmbeddedGraphDatabase database;

	public Neo4JReturningOperation(EmbeddedGraphDatabase database) {
		this.database = database;
	}

	public T retreiveInTransaction() {
		Transaction tx = database.beginTx();
		T result = null;
		try{
			result = this.retreival();
			tx.success();
		}catch (Exception e) {
			tx.failure();
		}finally{
			tx.finish();
		}
		return result;
	}

	protected abstract T retreival();
}
