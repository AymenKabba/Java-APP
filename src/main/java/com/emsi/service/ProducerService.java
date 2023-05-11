package com.emsi.service;

import com.emsi.dao.ProducerDao;
import com.emsi.dao.impl.ProducerDaoImp;
import com.emsi.entities.Producer;

import java.util.List;

public class ProducerService {
    private ProducerDao ProducerDao =new ProducerDaoImp();

    public List<Producer> findAll() {
        return ProducerDao.findAll();
    }

    public void save(Producer owner) {

        ProducerDao.insert(owner);

    }
    public void update(Producer owner) {
        ProducerDao.update(owner);
    }
    public void remove(Producer owner) {
        ProducerDao.deleteById(owner.getId());
    }


}
