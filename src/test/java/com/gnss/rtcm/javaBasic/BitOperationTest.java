package com.gnss.rtcm.javaBasic;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitOperationTest {
	
	@Test
	public void test() {
		long a = 1;
		System.out.println("a:" + Long.toBinaryString(a));
		System.out.println("~a:" + Long.toBinaryString(~a));
		
		System.out.println("a>>1:" + Long.toBinaryString(a>>1));
		System.out.println("a<<1:" + Long.toBinaryString(a<<1));
		
		long b = -1;
		System.out.println("b:" + Long.toBinaryString(b));
		System.out.println("~b:" + Long.toBinaryString(~b));
		
		System.out.println("b>>1:" + Long.toBinaryString(b>>1));
		System.out.println("b<<1:" + Long.toBinaryString(b<<1));
		
		log.info("dadfasdf");
	}
	
	@Test
	public void test1() {
		double a = 2830212;
		int b = 241343;
		log.info("a/1000 = " + a/1000 );
		log.info("a/1000 + b = " + (a/1000 + b));
	}
	

}
