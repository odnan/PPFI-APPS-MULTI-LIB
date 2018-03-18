package id.or.ppfi.entities;

public class M_GroupPrima {
	private String userName;
	private String groupCode;
	private String groupName;
	private String qtyAtlet;
	private String qtyPelatih;
	private String groupHead;
	private String groupLogo;
	private String groupType;
	private String groupStatus;
	private String qtyKSC;
	private String qtySSC;
	private String qtyHPD;
	private String groupCategory;

	public M_GroupPrima() {
		super();
	}

	public M_GroupPrima(
			String userName,
			String groupCode,
			String groupName,
			String qtyAtlet,
			String qtyPelatih,
			String groupHead,
			String groupLogo,
			String groupType,
			String groupStatus,
			String qtyKSC,
			String qtySSC,
			String qtyHPD,
			String groupCategory
	) {
		this.setUserName(userName);
		this.setGroupCode(groupCode);
		this.setGroupName(groupName);
		this.setGroupCategory(groupCategory);
		this.setQtyAtlet(qtyAtlet);
		this.setQtyPelatih(qtyPelatih);
		this.setGroupLogo(groupLogo);
		this.setGroupType(groupType);
		this.setGroupStatus(groupStatus);
		this.setGroupHead(groupHead);
		this.setQtyKSC(qtyKSC);
		this.setQtySSC(qtySSC);
		this.setGroupCategory(groupCategory);

	}

	public String getGroupHead() {
		return groupHead;
	}

	public void setGroupHead(String groupHead) {
		this.groupHead = groupHead;
	}

	public String getQtyKSC() {
		return qtyKSC;
	}

	public void setQtyKSC(String qtyKSC) {
		this.qtyKSC = qtyKSC;
	}

	public String getQtySSC() {
		return qtySSC;
	}

	public void setQtySSC(String qtySSC) {
		this.qtySSC = qtySSC;
	}

	public String getQtyHPD() {
		return qtyHPD;
	}

	public void setQtyHPD(String qtyHPD) {
		this.qtyHPD = qtyHPD;
	}

	public java.lang.String getGroupType() {
		return groupType;
	}

	public void setGroupType(java.lang.String groupType) {
		this.groupType = groupType;
	}

	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCategory() {
		return groupCategory;
	}

	public void setGroupCategory(String groupCategory) {
		this.groupCategory = groupCategory;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getQtyAtlet() {
		return qtyAtlet;
	}

	public void setQtyAtlet(String qtyAtlet) {
		this.qtyAtlet = qtyAtlet;
	}

	public String getQtyPelatih() {
		return qtyPelatih;
	}

	public void setQtyPelatih(String qtyPelatih) {
		this.qtyPelatih = qtyPelatih;
	}

	public String getGroupLogo() {
		return groupLogo;
	}

	public void setGroupLogo(String groupLogo) {
		this.groupLogo = groupLogo;
	}



}