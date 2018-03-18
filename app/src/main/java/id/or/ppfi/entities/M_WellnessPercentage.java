package id.or.ppfi.entities;

public class M_WellnessPercentage {
	private String username;
	private String wellness_rate;
	private String total_atlet_sum;
	private String total_atlet;
	private String total_percentage;


	public M_WellnessPercentage() {
		super();
	}

	public M_WellnessPercentage(
			String username,
			String wellness_rate,
			String total_atlet_sum,
			String total_atlet,
			String total_percentage
			) {
		this.setUsername(username);
		this.setWellness_rate(wellness_rate);
		this.setTotal_atlet_sum(total_atlet_sum);
		this.setTotal_atlet(total_atlet);
		this.setTotal_percentage(total_percentage);
}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWellness_rate() {
		return wellness_rate;
	}

	public void setWellness_rate(String wellness_rate) {
		this.wellness_rate = wellness_rate;
	}

	public String getTotal_atlet_sum() {
		return total_atlet_sum;
	}

	public void setTotal_atlet_sum(String total_atlet_sum) {
		this.total_atlet_sum = total_atlet_sum;
	}

	public String getTotal_atlet() {
		return total_atlet;
	}

	public void setTotal_atlet(String total_atlet) {
		this.total_atlet = total_atlet;
	}

	public String getTotal_percentage() {
		return total_percentage;
	}

	public void setTotal_percentage(String total_percentage) {
		this.total_percentage = total_percentage;
	}
}