package com.atguigu.gmall.product.service.impl;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchAttr;
import java.util.Date;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author hp
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2022-08-24 10:58:22
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    SkuInfoMapper skuInfoMapper;

    @Resource
    BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BaseTrademarkService baseTrademarkService;

    @Autowired
    SearchFeignClient searchFeignClient;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo info) {
        //        1.sku基本信息保存到sku_info表
        save(info);
        Long skuId = info.getId();

//        2.sku的图片保存到sku_image
        List<SkuImage> skuImageList = info.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
        }
        skuImageService.saveBatch(skuImageList);

//        3.sku的平台属性名和值的关系保存到sku_attr_value
        List<SkuAttrValue> skuAttrValueList = info.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {

            skuAttrValue.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
//        4.sku的销售属性名和值的关系保存到sku_sale_attr_value

        List<SkuSaleAttrValue> skuSaleAttrValueList = info.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {

            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(info.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
//  把这个skuid放到布隆过滤器中
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        bloomFilter.add(skuId);

    }

    @Override
    public void updateOnSale(Long skuId) {
//        商品上架  //1、改数据库 sku_info 这个skuId的is_sale； 1上架  0下架
        skuInfoMapper.updateIsSale(skuId,1);

        Goods goods=getGoodsBySkuId(skuId);

        searchFeignClient.saveGoods(goods);

    }

    @Override
    public void updateCancelSale(Long skuId) {
//        商品下架
        skuInfoMapper.updateIsSale(skuId,0);
        searchFeignClient.deleteGoods(skuId);
    }

    @Deprecated
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
//       1.查询到商品skuinfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
//      商品（sku）的基本信息【价格、重量、名字...】   sku_info
//        2.把查询到的数据放到skudetailTo中
              detailTo.setSkuInfo(skuInfo);
//        3.商品的图片
           List<SkuImage> imageList= skuImageService.getSkuImage(skuId);
           skuInfo.setSkuImageList(imageList);
//        4.商品的实时价格
        BigDecimal price = get1010Price(skuId);
        detailTo.setPrice(price);
//        5.商品sku所属的完整分类信息 base_category1、base_category2、base_category3
        CategoryViewTo categoryViewTo= baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        detailTo.setCategoryView(categoryViewTo);
//        6.商品sku当时定义的所有销售属性名值组合
        //          spu_sale_attr、spu_sale_attr_value
        //          并标识出当前sku到底spu的那种组合，页面要有高亮框 sku_sale_attr_value
        //查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组
       List<SpuSaleAttr> saleAttrList= spuSaleAttrService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);
       detailTo.setSpuSaleAttrList(saleAttrList);
//        7.商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
//        {"118|120": "50","119|121": 50} 这样的json字符串
        Long spuId = skuInfo.getSpuId();
       String valuejson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
       detailTo.setValuesSkuJson(valuejson);
        return detailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {
        BigDecimal price= skuInfoMapper.getRealPrice(skuId);
        return price;
    }

    @Override
    public SkuInfo getDetailSkuInfo(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return skuInfo;
    }

    @Override
    public List<SkuImage> getDetailSkuImages(Long skuId) {
        List<SkuImage> imageList= skuImageService.getSkuImage(skuId);
        return imageList;
    }

    @Override
    public List<Long> findAllSkuId() {
        // 100w 商品
        // 100w * 8byte = 800w 字节 = 8mb。
        //1亿数据，所有id从数据库传给微服务  800mb的数据量
        //分页查询。分批次查询。

        return skuInfoMapper.getAllSkuId();
    }

    @Override
    public Goods getGoodsBySkuId(Long skuId) {
//        得到某个sku在es中需要存储的所有数据
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        Goods goods = new Goods();
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        goods.setTmId(skuInfo.getTmId());

        BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());

        Long category3Id = skuInfo.getCategory3Id();
        CategoryViewTo view = baseCategory3Mapper.getCategoryView(category3Id);
        goods.setCategory1Id(view.getCategory1Id());
        goods.setCategory1Name(view.getCategory1Name());
        goods.setCategory2Id(view.getCategory2Id());
        goods.setCategory2Name(view.getCategory2Name());
        goods.setCategory3Id(view.getCategory3Id());
        goods.setCategory3Name(view.getCategory3Name());

        goods.setHotScore(0L);// TODO: 2022/9/8 热度分

//        查询当前sku所有平台属性名和值
       List<SearchAttr> attrs= skuAttrValueService.getSkuAttrNameAndValue(skuId);
        goods.setAttrs(attrs);


        return goods;
    }
}




