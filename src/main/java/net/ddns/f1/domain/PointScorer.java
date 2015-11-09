package net.ddns.f1.domain;

import java.util.Map;

public interface PointScorer {
	long getTotalPoints();
	Map<Integer, Integer> getPointsPerEvent();
	void setTotalPoints(long totalPoints);
	void setPointsPerEvent(Map<Integer, Integer> pointsPerEvent);	
}
