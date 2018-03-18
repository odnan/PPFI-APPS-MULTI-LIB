package id.or.ppfi.entities;

public class M_Periodisasi {
	private String uid;
	private String createdDate;
	private String createdTime;
	private String plannedType;
	private String username;
	private String groupCode;
	private String urlImage;
	private String title;
	private String notes;
	private String name;
	private String master_group_name;
	private String url_profile;

	public M_Periodisasi() {
		super();
	}

	public M_Periodisasi(
			String uid,
			String createdDate,
			String createdTime,
			String plannedType,
			String username,
			String groupCode,
			String urlImage,
			String title,
			String notes,
			String name,
			String master_group_name,
			String url_profile
			) {
		this.setUid(uid);
		this.setCreatedDate(createdDate);
		this.setCreatedTime(createdTime);
		this.setPlannedType(plannedType);
		this.setUsername(username);
		this.setGroupCode(groupCode);
		this.setUrlImage(urlImage);
		this.setTitle(title);
		this.setNotes(notes);
		this.setName(name);
		this.setMaster_group_name(master_group_name);
		this.setUrl_profile(url_profile);
}

	public String getUrl_profile() {
		return url_profile;
	}

	public void setUrl_profile(String url_profile) {
		this.url_profile = url_profile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaster_group_name() {
		return master_group_name;
	}

	public void setMaster_group_name(String master_group_name) {
		this.master_group_name = master_group_name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getPlannedType() {
		return plannedType;
	}

	public void setPlannedType(String plannedType) {
		this.plannedType = plannedType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}