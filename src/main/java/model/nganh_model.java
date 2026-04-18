package model;

public class nganh_model {
    private int id;
    private String tenNganh;
    private int khoaId;
    private String tenKhoa; // dùng để hiển thị trên bảng (JOIN)
    private String moTa;

    // Constructor rỗng
    public nganh_model() {}

    // Constructor đầy đủ (dùng khi INSERT/UPDATE)
    public nganh_model(int id, String tenNganh, int khoaId, String moTa) {
        this.id = id;
        this.tenNganh = tenNganh;
        this.khoaId = khoaId;
        this.moTa = moTa;
    }

    // Constructor có tenKhoa (dùng khi SELECT JOIN)
    public nganh_model(int id, String tenNganh, int khoaId, String tenKhoa, String moTa) {
        this.id = id;
        this.tenNganh = tenNganh;
        this.khoaId = khoaId;
        this.tenKhoa = tenKhoa;
        this.moTa = moTa;
    }

    // ========== GETTER ==========
    public int getId() {
        return id;
    }

    public String getTenNganh() {
        return tenNganh;
    }

    public int getKhoaId() {
        return khoaId;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public String getMoTa() {
        return moTa;
    }

    // ========== SETTER ==========
    public void setId(int id) {
        this.id = id;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh;
    }

    public void setKhoaId(int khoaId) {
        this.khoaId = khoaId;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // ========== toString (debug) ==========
    @Override
    public String toString() {
        return "Nganh_model{" +
                "id=" + id +
                ", tenNganh='" + tenNganh + '\'' +
                ", khoaId=" + khoaId +
                ", tenKhoa='" + tenKhoa + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}