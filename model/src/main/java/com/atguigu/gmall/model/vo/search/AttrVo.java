package com.atguigu.gmall.model.vo.search;

import lombok.Data;

import java.util.List;

@Data
public class AttrVo {

    private String attrName;
    private Long attrId;
//    每个属性涉及到的所有值集合
    private List<String> attrValueList;
}
