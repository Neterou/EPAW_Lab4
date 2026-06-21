package epaw.lab4.model;

import java.io.Serializable;

/**
 * BubbleNet user. Carries the full registration profile plus the
 * bubble the user belongs to and their role (USER / ADMIN).
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String email;
	private String phone;
	private String phoneCountry;
	private String dni;
	private String zip;
	private String city;
	private String country;
	private String gender;
	private String password;
	private String username;
	private String picture;
	private String status;
	private String role;
	private Integer bubbleId;

	public User() {
		super();
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getPhoneCountry() { return phoneCountry; }
	public void setPhoneCountry(String phoneCountry) { this.phoneCountry = phoneCountry; }

	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public String getZip() { return zip; }
	public void setZip(String zip) { this.zip = zip; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }

	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPicture() { return picture; }
	public void setPicture(String picture) { this.picture = picture; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	public Integer getBubbleId() { return bubbleId; }
	public void setBubbleId(Integer bubbleId) { this.bubbleId = bubbleId; }

	/** Convenience flag used by views to decide who can compose posts. */
	public boolean isAdmin() {
		return "ADMIN".equalsIgnoreCase(role);
	}
}
