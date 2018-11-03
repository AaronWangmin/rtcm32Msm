package com.gnss.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Component
//@Scope("prototype")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalData {
	
	private String frequencyBand;	// L1/L2/L3
	
	/**
	 * Reference to the table "GPS Signal ID Mapping" on the P224 of RTCM10403.2
	 * eg: 1C/1P/1W/2C/2P/2W
	 */
	private String signalCode;
	
	private double pseudorange = -1;
	private double phaseRange = -1;
	private double snr = -1;
	private long lockTime = 30;
//	private double doppler = -1;

}
