package net.ddns.f1.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public final class Rules {
	@Value("${budget}")
	private int budget;
	private final Integer fastestLapBonus = 50;
	private final Integer bothCarsFinishedBonus = 50;

	private final Map<Integer, Integer> driverQualPoints = new HashMap<Integer, Integer>();
	{
		driverQualPoints.put(1, 200);
		driverQualPoints.put(2, 160);
		driverQualPoints.put(3, 128);
		driverQualPoints.put(4, 104);
		driverQualPoints.put(5, 88);
		driverQualPoints.put(6, 80);
		driverQualPoints.put(7, 72);
		driverQualPoints.put(8, 68);
		driverQualPoints.put(9, 64);
		driverQualPoints.put(10, 60);
		driverQualPoints.put(11, 56);
		driverQualPoints.put(12, 52);
		driverQualPoints.put(13, 48);
		driverQualPoints.put(14, 44);
		driverQualPoints.put(15, 40);
		driverQualPoints.put(16, 36);
		driverQualPoints.put(17, 32);
		driverQualPoints.put(18, 28);
		driverQualPoints.put(19, 24);
		driverQualPoints.put(20, 20);
	}

	private final Map<Integer, Integer> driverRacePoints = new HashMap<Integer, Integer>();
	{
		driverRacePoints.put(1, 500);
		driverRacePoints.put(2, 400);
		driverRacePoints.put(3, 320);
		driverRacePoints.put(4, 260);
		driverRacePoints.put(5, 220);
		driverRacePoints.put(6, 200);
		driverRacePoints.put(7, 180);
		driverRacePoints.put(8, 170);
		driverRacePoints.put(9, 160);
		driverRacePoints.put(10, 150);
		driverRacePoints.put(11, 140);
		driverRacePoints.put(12, 130);
		driverRacePoints.put(13, 120);
		driverRacePoints.put(14, 110);
		driverRacePoints.put(15, 100);
		driverRacePoints.put(16, 90);
		driverRacePoints.put(17, 80);
		driverRacePoints.put(18, 70);
		driverRacePoints.put(19, 60);
		driverRacePoints.put(20, 50);
	}

	private final Map<Integer, Integer> carQualPoints = new HashMap<Integer, Integer>();
	{
		carQualPoints.put(1, 100);
		carQualPoints.put(2, 80);
		carQualPoints.put(3, 64);
		carQualPoints.put(4, 52);
		carQualPoints.put(5, 44);
		carQualPoints.put(6, 40);
		carQualPoints.put(7, 36);
		carQualPoints.put(8, 34);
		carQualPoints.put(9, 32);
		carQualPoints.put(10, 30);
		carQualPoints.put(11, 28);
		carQualPoints.put(12, 26);
		carQualPoints.put(13, 24);
		carQualPoints.put(14, 22);
		carQualPoints.put(15, 20);
		carQualPoints.put(16, 18);
		carQualPoints.put(17, 16);
		carQualPoints.put(18, 14);
		carQualPoints.put(19, 12);
		carQualPoints.put(20, 10);
	}

	private final Map<Integer, Integer> carRacePoints = new HashMap<Integer, Integer>();
	{
		carRacePoints.put(1, 200);
		carRacePoints.put(2, 160);
		carRacePoints.put(3, 128);
		carRacePoints.put(4, 104);
		carRacePoints.put(5, 88);
		carRacePoints.put(6, 80);
		carRacePoints.put(7, 72);
		carRacePoints.put(8, 68);
		carRacePoints.put(9, 64);
		carRacePoints.put(10, 60);
		carRacePoints.put(11, 56);
		carRacePoints.put(12, 52);
		carRacePoints.put(13, 48);
		carRacePoints.put(14, 44);
		carRacePoints.put(15, 40);
		carRacePoints.put(16, 36);
		carRacePoints.put(17, 32);
		carRacePoints.put(18, 28);
		carRacePoints.put(19, 24);
		carRacePoints.put(20, 20);
	}

	private final Map<Integer, Integer> engineQualPoints = new HashMap<Integer, Integer>();
	{
		engineQualPoints.put(1, 50);
		engineQualPoints.put(2, 40);
		engineQualPoints.put(3, 32);
		engineQualPoints.put(4, 26);
		engineQualPoints.put(5, 22);
		engineQualPoints.put(6, 20);
		engineQualPoints.put(7, 18);
		engineQualPoints.put(8, 17);
		engineQualPoints.put(9, 16);
		engineQualPoints.put(10, 15);
		engineQualPoints.put(11, 14);
		engineQualPoints.put(12, 13);
		engineQualPoints.put(13, 12);
		engineQualPoints.put(14, 11);
		engineQualPoints.put(15, 10);
		engineQualPoints.put(16, 9);
		engineQualPoints.put(17, 8);
		engineQualPoints.put(18, 7);
		engineQualPoints.put(19, 6);
		engineQualPoints.put(20, 5);
	}

	private final Map<Integer, Integer> engineRacePoints = new HashMap<Integer, Integer>();
	{
		engineRacePoints.put(1, 100);
		engineRacePoints.put(2, 80);
		engineRacePoints.put(3, 64);
		engineRacePoints.put(4, 52);
		engineRacePoints.put(5, 44);
		engineRacePoints.put(6, 40);
		engineRacePoints.put(7, 36);
		engineRacePoints.put(8, 34);
		engineRacePoints.put(9, 32);
		engineRacePoints.put(10, 30);
		engineRacePoints.put(11, 28);
		engineRacePoints.put(12, 26);
		engineRacePoints.put(13, 24);
		engineRacePoints.put(14, 22);
		engineRacePoints.put(15, 20);
		engineRacePoints.put(16, 18);
		engineRacePoints.put(17, 16);
		engineRacePoints.put(18, 14);
		engineRacePoints.put(19, 12);
		engineRacePoints.put(20, 10);
	}
}
