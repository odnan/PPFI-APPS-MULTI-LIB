package id.or.ppfi.entities;

public class M_MealPlan {
	private String imageName;
	private String imageDesc;
	private String imageURL;
	private String isShow;
	private String updateAt;
	private String updateBy;


	public M_MealPlan() {
		super();
	}

	public M_MealPlan(
			String imageName,
			String imageDesc,
			String imageURL,
			String isShow,
			String updateAt,
			String updateBy
	) {
		this.setImageName(imageName);
		this.setImageDesc(imageDesc);
		this.setImageURL(imageURL);
		this.setIsShow(isShow);
		this.setUpdateAt(updateAt);
		this.setUpdateBy(updateBy);
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}