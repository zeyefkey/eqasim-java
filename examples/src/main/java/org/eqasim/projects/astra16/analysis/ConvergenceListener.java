package org.eqasim.projects.astra16.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;

public class ConvergenceListener implements IterationEndsListener, PersonDepartureEventHandler {
	private final OutputDirectoryHierarchy outputHierarchy;

	private final List<Integer> trips = new LinkedList<>();
	private int iterationTrips = 0;

	public ConvergenceListener(OutputDirectoryHierarchy outputHierarchy) {
		this.outputHierarchy = outputHierarchy;
	}

	@Override
	public void handleEvent(PersonDepartureEvent event) {
		iterationTrips++;
	}

	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		trips.add(iterationTrips);
		iterationTrips = 0;

		try {
			BufferedWriter writer = IOUtils.getBufferedWriter(outputHierarchy.getOutputFilename("amod_trips.csv"));

			writer.write("iteration;computed\n");

			for (int i = 0; i < trips.size(); i++) {
				writer.write(String.format("%d;%d\n", i, trips.get(i)));
			}

			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
