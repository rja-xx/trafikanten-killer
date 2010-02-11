package no.knowit.trafikantenkiller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

public class Route implements Iterable<RouteElement>
{

	private List<RouteElement> routeElements = new ArrayList<RouteElement>();

	@Override
	public Iterator<RouteElement> iterator() {
		return this.routeElements.iterator();
	}

	public void addRouteelement(RouteElement routeElement) {
		Assert.assertNotNull(routeElement);
		routeElements.add(routeElement);
	}

}
