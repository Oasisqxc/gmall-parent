package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuImageMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hp
 * @description 针对表【spu_info(商品表)】的数据库操作Service实现
 * @createDate 2022-08-24 10:58:21
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
        implements SpuInfoService {

    @Resource
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Transactional//开启事务
    @Override
    public void saveSpuInfo(SpuInfo info) {
//1.把spu基本信息保存到spu_Info表中
        spuInfoMapper.insert(info);
//  1.1拿到spuinfo保存后的自增id
        Long spuId = info.getId();
//        、2.把spu的图片保存到spu_image表中

        List<SpuImage> imageList = info.getSpuImageList();
        for (SpuImage image : imageList) {
//            回填spu_id
            image.setSpuId(spuId);
        }
//        批量保存图片
        spuImageService.saveBatch(imageList);

//        、3.保存销售属性名到spu_sale_attr中
        List<SpuSaleAttr> saleAttrList = info.getSpuSaleAttrList();
        for (SpuSaleAttr attr : saleAttrList) {

//            回填spuid
            attr.setSpuId(spuId);
//        4.拿到销售属性名所对应的销售属性值集合

            List<SpuSaleAttrValue> valueList = attr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : valueList) {
//            回填spuid
                value.setSpuId(spuId);
                String saleAttrName = attr.getSaleAttrName();
//                回填销售属性名
                value.setSaleAttrName(saleAttrName);


            }
//            保存销售属性值到数据库
            spuSaleAttrValueService.saveBatch(valueList);
        }
//        保存销售属性名到数据库
        spuSaleAttrService.saveBatch(saleAttrList);

    }
}




