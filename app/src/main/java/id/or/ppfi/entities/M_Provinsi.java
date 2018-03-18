package id.or.ppfi.entities;

public class M_Provinsi {
	private String provinsiID;
	private String provinsiName;

	public M_Provinsi() {
		super();
	}

	public M_Provinsi(
			String provinsiID,
			String provinsiName
			) {
	this.setProvinsiID(provinsiID);
	this.setProvinsiName(provinsiName);
}

	public String getProvinsiID() {return provinsiID;}
	public void setProvinsiID(String provinsiID) {this.provinsiID = provinsiID;}

	public String getProvinsiName() {return provinsiName;}
	public void setProvinsiName(String provinsiName) {this.provinsiName = provinsiName;}
	

}