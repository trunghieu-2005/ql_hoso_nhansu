package service;

import dao.hoso_dao;
import model.hoso_model;

import java.util.ArrayList;
import java.util.List;

public class hoso_service {
    private final hoso_dao dao;

    public hoso_service() {
        this(new hoso_dao());
    }

    public hoso_service(hoso_dao dao) {
        this.dao = dao;
    }

    public List<hoso_model> findAll() {
        return dao.findAll();
    }

    public int add(hoso_model model) {
        int newId = dao.insert(model);
        model.setId(newId);
        return newId;
    }

    public boolean update(hoso_model model) {
        if (model.getId() == null) {
            return false;
        }
        return dao.update(model);
    }

    public boolean deleteById(int id) {
        return dao.delete(id);
    }

    public int findFirstIndexByKeyword(String keyword) {
        List<hoso_model> data = findAll();
        List<hoso_model> result = searchAdvanced(keyword, null, null, "", "", "");
        if (result.isEmpty()) {
            return -1;
        }

        Integer firstId = result.get(0).getId();
        if (firstId == null) {
            return -1;
        }

        for (int i = 0; i < data.size(); i++) {
            hoso_model item = data.get(i);
            if (item.getId() != null && item.getId().equals(firstId)) {
                return i;
            }
        }
        return -1;
    }

    public List<hoso_model> searchAdvanced(String keyword, Integer khoaId, Integer nganhId,
                                           String gioiTinh, String trinhDo, String chucVu) {
        String key = normalize(keyword);
        String gioiTinhFilter = normalize(gioiTinh);
        String trinhDoFilter = normalize(trinhDo);
        String chucVuFilter = normalize(chucVu);

        List<hoso_model> result = new ArrayList<>();
        for (hoso_model item : findAll()) {
            if (!matchesKeyword(item, key)) {
                continue;
            }

            if (khoaId != null && !khoaId.equals(item.getKhoaId())) {
                continue;
            }

            if (nganhId != null && !nganhId.equals(item.getNganhId())) {
                continue;
            }

            if (!gioiTinhFilter.isBlank() && !containsText(item.getGioiTinh(), gioiTinhFilter)) {
                continue;
            }

            if (!trinhDoFilter.isBlank() && !containsText(item.getTrinhDo(), trinhDoFilter)) {
                continue;
            }

            if (!chucVuFilter.isBlank() && !containsText(item.getChucVu(), chucVuFilter)) {
                continue;
            }

            result.add(item);
        }
        return result;
    }

    private boolean matchesKeyword(hoso_model item, String key) {
        if (key.isBlank()) {
            return true;
        }

        String idText = item.getId() == null ? "" : String.valueOf(item.getId());
        String khoa = item.getKhoaId() == null ? "" : String.valueOf(item.getKhoaId());
        String nganh = item.getNganhId() == null ? "" : String.valueOf(item.getNganhId());

        return containsText(idText, key)
                || containsText(item.getHoTen(), key)
                || containsText(item.getSoDienThoai(), key)
                || containsText(item.getEmail(), key)
                || containsText(item.getDiaChi(), key)
                || containsText(item.getTrinhDo(), key)
                || containsText(item.getChucVu(), key)
                || containsText(khoa, key)
                || containsText(nganh, key);
    }

    private boolean containsText(String source, String keyword) {
        if (source == null || keyword == null) {
            return false;
        }
        return source.toLowerCase().contains(keyword);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    public int findIndexById(int id) {
        List<hoso_model> data = findAll();
        for (int i = 0; i < data.size(); i++) {
            hoso_model item = data.get(i);
            if (item.getId() != null && item.getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
