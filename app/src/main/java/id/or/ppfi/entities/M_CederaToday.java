package id.or.ppfi.entities;

public class M_CederaToday {
	private String userName;
	private String usernameAtlet;
	private String nameAtlet;
	private String cidera;
	private String createdDttmToday;
	private String createdDateToday;
	private String createdTimeToday;
	private String valueWellness;
	private String gambar;
	private String groupCode;
	private String groupName;
	private String timeOutNotif;


	public M_CederaToday() {
		super();
	}

	public M_CederaToday(
			String userName,
			String usernameAtlet,
			String nameAtlet,
			String cidera,
			String createdDttmToday,
			String createdDateToday,
			String createdTimeToday,
			String valueWellness,
			String gambar,
			String groupCode,
			String groupName,
			String timeOutNotif
	) {
		this.setUserName(userName);
		this.setUsernameAtlet(usernameAtlet);
		this.setNameAtlet(nameAtlet);
		this.setCidera(cidera);
		this.setCreatedDttmToday(createdDttmToday);
		this.setCreatedDateToday(createdDateToday);
		this.setCreatedTimeToday(createdTimeToday);
		this.setValueWellness(valueWellness);
		this.setGambar(gambar);
		this.setGroupCode(groupCode);
		this.setGroupName(groupName);

	}

	public String getTimeOutNotif() {
		return timeOutNotif;
	}

	public void setTimeOutNotif(String timeOutNotif) {
		this.timeOutNotif = timeOutNotif;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUsernameAtlet() {
		return usernameAtlet;
	}

	public void setUsernameAtlet(String usernameAtlet) {
		this.usernameAtlet = usernameAtlet;
	}

	public String getNameAtlet() {
		return nameAtlet;
	}

	public void setNameAtlet(String nameAtlet) {
		this.nameAtlet = nameAtlet;
	}

	public String getCidera() {
		return cidera;
	}

	public void setCidera(String cidera) {
		this.cidera = cidera;
	}

	public String getCreatedDttmToday() {
		return createdDttmToday;
	}

	public void setCreatedDttmToday(String createdDttmToday) {
		this.createdDttmToday = createdDttmToday;
	}

	public String getCreatedDateToday() {
		return createdDateToday;
	}

	public void setCreatedDateToday(String createdDateToday) {
		this.createdDateToday = createdDateToday;
	}

	public String getCreatedTimeToday() {
		return createdTimeToday;
	}

	public void setCreatedTimeToday(String createdTimeToday) {
		this.createdTimeToday = createdTimeToday;
	}

	public String getValueWellness() {
		return valueWellness;
	}

	public void setValueWellness(String valueWellness) {
		this.valueWellness = valueWellness;
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
}