package com.sobey.mvc;

import org.apache.commons.lang3.StringUtils;

import com.sobey.framework.utils.Identities;

public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Identities.randomLong());
		System.out.println(Identities.uuid());
		System.out.println(Identities.uuid2());
		System.out.println(Identities.randomBase62(12));
		
	}

}
