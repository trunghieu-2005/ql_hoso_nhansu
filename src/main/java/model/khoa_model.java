package model;

public class khoa_model {
	private int id;
	private String tenKhoa;
	private String moTa;

	public khoa_model() {
	}

	public khoa_model(int id, String tenKhoa, String moTa) {
		this.id = id;
		this.tenKhoa = tenKhoa;
		this.moTa = moTa;
	}

	public khoa_model(String tenKhoa, String moTa) {
		this.tenKhoa = tenKhoa;
		this.moTa = moTa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTenKhoa() {
		return tenKhoa;
	}

	public void setTenKhoa(String tenKhoa) {
		this.tenKhoa = tenKhoa;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	@Override
	public String toString() {
    	return tenKhoa;
	}
}
