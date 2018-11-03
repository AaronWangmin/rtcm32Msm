package com.gnss.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

//@Component
//@Scope("prototype")
@Data
@NoArgsConstructor
public class GnssObservationData {
	private Date time;
	private int weekNumber;
	private long second;
	private long ms;
	
	/**
	 * Map<satelliteSystem,HashMap<prn,satelliteDate>>
	 */
	private Map<String,HashMap<Integer,SatelliteData>> satelliteDataMap = new HashMap<String,HashMap<Integer,SatelliteData>>();
	
	public GnssObservationData(Date time,int weekNumber,long second,long ms) {
		this.time = time;
		this.weekNumber = weekNumber;
		this.second = second;
		this.ms = ms;
		this.satelliteDataMap = new HashMap<String,HashMap<Integer,SatelliteData>>();
	}
	
	public void addSatelliteData(SatelliteData satelliteData) {
		if(satelliteData.getSatelliteSystem() == null) return;
		if(satelliteDataMap.get(satelliteData.getSatelliteSystem()) == null){
			satelliteDataMap.put(satelliteData.getSatelliteSystem(), new HashMap<Integer,SatelliteData>());
		}
		satelliteDataMap.get(satelliteData.getSatelliteSystem()).put(satelliteData.getPrn(), satelliteData);
	}
	
	public SatelliteData getSatelliteData(String satelliteSystem,int prn) {
		return satelliteDataMap.get(satelliteSystem).get(prn);
	}
	
	public Map<Integer,SatelliteData> getSatelliteDatas(String satelliteSystem) {
		return satelliteDataMap.get(satelliteSystem);
	}
	
	
}
