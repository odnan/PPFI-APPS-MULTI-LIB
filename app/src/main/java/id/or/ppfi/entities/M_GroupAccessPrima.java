package id.or.ppfi.entities;

public class M_GroupAccessPrima {
	private String userName;
	private String groupCode;
	private String groupName;
	private String qtyAtlet;
	private String qtyPelatih;
	private String totaKSC;
	private String totalHPD;

	public M_GroupAccessPrima() {
		super();
	}

	public M_GroupAccessPrima(
			String userName,
			String groupCode,
			String groupName,
			String qtyAtlet,
			String qtyPelatih,
			String totaKSC,
			String totalHPD
	) {
		this.setUserName(userName);
		this.setGroupCode(groupCode);
		this.setGroupName(groupName);
		this.setQtyAtlet(qtyAtlet);
		this.setQtyPelatih(qtyPelatih);
		this.setTotaKSC(totaKSC);
		this.setTotalHPD(totalHPD);

	}


	public String getTotaKSC() {
		return totaKSC;
	}

	public void setTotaKSC(String totaKSC) {
		this.totaKSC = totaKSC;
	}

	public String getTotalHPD() {
		return totalHPD;
	}

	public void setTotalHPD(String totalHPD) {
		this.totalHPD = totalHPD;
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




}