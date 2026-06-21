package epaw.lab4.model;

import java.io.Serializable;

/**
 * A Bubble is a hyper-local community channel. It anchors to a physical
 * location (lat/lng) and has a category that drives its colour/emoji on
 * the map. {@code memberCount} is derived (not stored) for the markers.
 */
public class Bubble implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String category;
	private String zip;
	private String city;
	private String country;
	private double lat;
	private double lng;
	private boolean open;
	private Integer ownerId;    // creator/owner who approves join requests
	private int memberCount;
	private String membership;  // current viewer's status: APPROVED | PENDING | null
	private boolean home;       // is this the viewer's registration home bubble

	public Bubble() {
		super();
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public String getZip() { return zip; }
	public void setZip(String zip) { this.zip = zip; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }

	public double getLat() { return lat; }
	public void setLat(double lat) { this.lat = lat; }

	public double getLng() { return lng; }
	public void setLng(double lng) { this.lng = lng; }

	public boolean isOpen() { return open; }
	public void setOpen(boolean open) { this.open = open; }

	public Integer getOwnerId() { return ownerId; }
	public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

	public int getMemberCount() { return memberCount; }
	public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

	public String getMembership() { return membership; }
	public void setMembership(String membership) { this.membership = membership; }

	public boolean isMember() { return "APPROVED".equals(membership); }
	public boolean isPending() { return "PENDING".equals(membership); }

	public boolean isHome() { return home; }
	public void setHome(boolean home) { this.home = home; }
}
