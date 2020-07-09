package com.xs.spring.service;

import com.xs.spring.dao.IndexDao;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-09 23:25
 **/
public class IndexServiceImpl  implements IndexService{

    IndexDao indexDao;
    public String query() {
        System.out.println("service...................");
         indexDao.query();
         return "";
    }

//    public IndexServiceImpl(IndexDao indexDao) {
//        this.indexDao = indexDao;
//    }

//    public IndexDao getIndexDao() {
//        return indexDao;
//    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }
}
