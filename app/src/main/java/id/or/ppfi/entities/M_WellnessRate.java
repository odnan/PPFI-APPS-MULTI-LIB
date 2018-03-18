package id.or.ppfi.entities;

public class M_WellnessRate {
	private String username_atlet;
	private String name;
	private String cidera;
	private String created_dttm_today;
	private String created_date;
	private String created_time;
	private String value_wellness;
	private String gambar;
	private String groupcode;
	private String master_group_name;
	private String group_category;
	private String wellness_rate;


	public M_WellnessRate() {
		super();
	}

	public M_WellnessRate(
			String username_atlet,
			String name,
			String cidera,
			String created_dttm_today,
			String created_date,
			String created_time,
			String value_wellness,
			String gambar,
			String groupcode,
			String master_group_name,
			String group_category,
			String wellness_rate

	) {
		this.setUsername_atlet(username_atlet);
		this.setName(name);
		this.setCidera(cidera);
		this.setCreated_dttm_today(created_dttm_today);
		this.setCreated_date(created_date);
		this.setCreated_time(created_time);
		this.setValue_wellness(value_wellness);
		this.setGambar(gambar);
		this.setGroupcode(groupcode);
		this.setMaster_group_name(master_group_name);
		this.setGroup_category(group_category);
		this.setWellness_rate(wellness_rate);

	}

	public String getUsername_atlet() {
		return username_atlet;
	}

	public void setUsername_atlet(String username_atlet) {
		this.username_atlet = username_atlet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCidera() {
		return cidera;
	}

	public void setCidera(String cidera) {
		this.cidera = cidera;
	}

	public String getCreated_dttm_today() {
		return created_dttm_today;
	}

	public void setCreated_dttm_today(String created_dttm_today) {
		this.created_dttm_today = created_dttm_today;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getValue_wellness() {
		return value_wellness;
	}

	public void setValue_wellness(String value_wellness) {
		this.value_wellness = value_wellness;
	}

	public String getGambar() {
		return gambar;
	}

	public void setGambar(String gambar) {
		this.gambar = gambar;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public String getMaster_group_name() {
		return master_group_name;
	}

	public void setMaster_group_name(String master_group_name) {
		this.master_group_name = master_group_name;
	}

	public String getGroup_category() {
		return group_category;
	}

	public void setGroup_category(String group_category) {
		this.group_category = group_category;
	}

	public String getWellness_rate() {
		return wellness_rate;
	}

	public void setWellness_rate(String wellness_rate) {
		this.wellness_rate = wellness_rate;
	}
}