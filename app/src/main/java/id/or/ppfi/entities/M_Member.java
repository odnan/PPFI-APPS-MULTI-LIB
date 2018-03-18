package id.or.ppfi.entities;

public class M_Member {
	private String userName;
	private String name;
	private String licenceCode;
	private String gambar;
	private String groupCode;
	private String groupName;
	private String roleName;
	private String dummyNilaiWellness;
	private String valueWellness;
	private String nomorEvent;
	private String wellnessDate;
	private String cidera;
	private String wellnessTime;

	public M_Member() {
		super();
	}

	public M_Member(
			String userName,
			String name,
			String licenceCode,
			String gambar,
			String groupCode,
			String groupName,
			String roleName,
			String dummyNilaiWellness,
			String valueWellness,
			String nomorEvent,
			String wellnessDate,
			String cidera,
			String wellnessTime
			) {
	this.setUserName(userName);
	this.setName(name);
	this.setLicenceCode(licenceCode);
	this.setGambar(gambar);
	this.setGroupCode(groupCode);
	this.setGroupName(groupName);
	this.setRoleName(roleName);
	this.setDummyNilaiWellness(dummyNilaiWellness);
	this.setValueWellness(valueWellness);
	this.setNomorEvent(nomorEvent);
	this.setWellnessDate(wellnessDate);
	this.setCidera(cidera);
		this.setWellnessTime(wellnessTime);
}

	public String getWellnessTime() {
		return wellnessTime;
	}

	public void setWellnessTime(String wellnessTime) {
		this.wellnessTime = wellnessTime;
	}

	public String getCidera() {return cidera;}
	public void setCidera(String cidera) {this.cidera = cidera;}

	public String getWellnessDate() {return wellnessDate;}
	public void setWellnessDate(String wellnessDate) {this.wellnessDate = wellnessDate;}

	public String getNomorEvent() {return nomorEvent;}
	public void setNomorEvent(String nomorEvent) {this.nomorEvent = nomorEvent;}
	

	public String getValueWellness() {
		return valueWellness;
	}

	public void setValueWellness(String valueWellness) {
		this.valueWellness = valueWellness;
	}

	public String getDummyNilaiWellness() {
		return dummyNilaiWellness;
	}

	public void setDummyNilaiWellness(String dummyNilaiWellness) {
		this.dummyNilaiWellness = dummyNilaiWellness;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicenceCode() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}

	public String getGambar() {
		return gambar;
	}

	public void setGambar(String gambar) {
		this.gambar = gambar;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	

}