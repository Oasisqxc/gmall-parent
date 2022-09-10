package com.atguigu.gmall.model.vo.search;

import lombok.Data;

@Data
public class SearchParamVo {

    Long category1Id;
    Long category2Id;
    Long category3Id;
    String trademark;
    String keyword;
    String order = "1:desc";
    String[] props;
    Integer pageNo=1;
}
