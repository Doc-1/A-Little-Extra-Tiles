package com.alet.common.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LeadConnectionData {
	
	public int connectionID;
	public int color;
	public double thickness;
	public double tautness;
	public Set<UUID> uuidsConnected = new HashSet<UUID>();
	public Set<Integer> idsConnected = new HashSet<Integer>();
	
	public LeadConnectionData(int color, double thickness, double tautness) {
		this.color = color;
		this.thickness = thickness;
		this.tautness = tautness;
	}
	
	@Override
	public boolean equals(Object data) {
		LeadConnectionData d = (LeadConnectionData) data;
		if (this.color == d.color && this.thickness == d.thickness && this.tautness == d.tautness)
			return true;
		else
			return false;
	}
}
