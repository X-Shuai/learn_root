package com.xs.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @program: learn_root
 * @description: Mapper
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:08
 **/

public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
