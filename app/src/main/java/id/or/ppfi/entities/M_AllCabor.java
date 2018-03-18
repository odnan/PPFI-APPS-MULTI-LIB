package id.or.ppfi.entities;

public class M_AllCabor {
	private String groupCode;
	private String groupCodeWaka;
	private String groupName;
	private String qtyAtlet;
	private String qtyPelatih;
	private String groupHead;
	private String groupLogo;
	private String totalAtletPutra;
	private String totalAtletPutri;

	public M_AllCabor() {
		super();
	}

	public M_AllCabor(
			String groupCode,
			String groupCodeWaka,
			String groupName,
			String qtyAtlet,
			String qtyPelatih,
			String groupHead,
			String groupLogo,
			String totalAtletPutra,
			String totalAtletPutri

	) {
		this.setGroupCode(groupCode);
		this.setGroupCodeWaka(groupCodeWaka);
		this.setGroupName(groupName);
		this.setQtyAtlet(qtyAtlet);
		this.setQtyPelatih(qtyPelatih);
		this.setGroupHead(groupHead);
		this.setGroupLogo(groupLogo);
		this.setTotalAtletPutra(totalAtletPutra);
		this.setTotalAtletPutri(totalAtletPutri);

	}

	public String getTotalAtletPutra() {
		return totalAtletPutra;
	}

	public void setTotalAtletPutra(String totalAtletPutra) {
		this.totalAtletPutra = totalAtletPutra;
	}

	public String getTotalAtletPutri() {
		return totalAtletPutri;
	}

	public void setTotalAtletPutri(String totalAtletPutri) {
		this.totalAtletPutri = totalAtletPutri;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCodeWaka() {
		return groupCodeWaka;
	}

	public void setGroupCodeWaka(String groupCodeWaka) {
		this.groupCodeWaka = groupCodeWaka;
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

	public String getGroupHead() {
		return groupHead;
	}

	public void setGroupHead(String groupHead) {
		this.groupHead = groupHead;
	}

	public String getGroupLogo() {
		return groupLogo;
	}

	public void setGroupLogo(String groupLogo) {
		this.groupLogo = groupLogo;
	}


}