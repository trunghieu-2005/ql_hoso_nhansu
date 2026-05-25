package service;

import java.util.List;

import dao.hoso_dao;
import model.hoso_model;

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
        return dao.searchAdvanced(keyword, khoaId, nganhId, gioiTinh, trinhDo, chucVu);
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
