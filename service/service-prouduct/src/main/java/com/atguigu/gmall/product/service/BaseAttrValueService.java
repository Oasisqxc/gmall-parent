package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hp
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2022-08-24 10:58:21
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    List<BaseAttrValue> getAttrValueList(Long attrId);
}
