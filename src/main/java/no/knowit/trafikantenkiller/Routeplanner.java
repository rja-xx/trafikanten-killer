package no.knowit.trafikantenkiller;

import no.knowit.trafikantenkiller.model.nodes.Station;

public interface Routeplanner {

	public Route planRute(Station from, Station dest);

}
