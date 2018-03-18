package id.or.ppfi.carousel.entities;

public class Member {
	private String member_id;
	private String name;
	private String level;
	private String experience;
	private String url_image;
	
	public Member() {
		super();
	}

	public Member(
			String member_id,
			String name,
			String level,
			String experience,
			String url_image
	) {
		this.setMember_id(member_id);
		this.setName(name);
		this.setLevel(level);
		this.setExperience(experience);
		this.setUrl_image(url_image);

}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getUrl_image() {
		return url_image;
	}

	public void setUrl_image(String url_image) {
		this.url_image = url_image;
	}
}