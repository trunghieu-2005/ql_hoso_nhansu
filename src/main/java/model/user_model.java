package model;

public class user_model {
    private int id;
    private String username;
    private String password;
    private String role; // "admin", "can_bo_nhan_su", "can_bo_nhan_vien"
    private String hoTen;
    private String email;

    public user_model() {}

    public user_model(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public user_model(int id, String username, String password, String role, String hoTen, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.hoTen = hoTen;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRoleDisplay() {
        if (role == null) return "Không xác định";
        switch (role) {
            case "admin": return "Quản trị viên";
            case "can_bo_nhan_su": return "Cán bộ nhân sự";
            case "can_bo_nhan_vien": return "Cán bộ nhân viên";
            default: return role;
        }
    }

    @Override
    public String toString() {
        return "user_model{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}