package com.tiza.gw.support.dao;


import com.tiza.gw.support.entity.OnlineEntity;

import java.util.List;

/**
 * Created by admin on 2017/4/26.
 */
public interface OnlineCheckDao {

    public List<OnlineEntity> selectOnline();
}
