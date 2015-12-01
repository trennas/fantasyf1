package net.ddns.f1.domain;

import java.util.Map;

public interface PointScorer {
	long getTotalPoints();

	Map<Integer, Integer> getPointsPerEvent();

	void setTotalPoints(final long totalPoints);

	void setPointsPerEvent(final Map<Integer, Integer> pointsPerEvent);
}
