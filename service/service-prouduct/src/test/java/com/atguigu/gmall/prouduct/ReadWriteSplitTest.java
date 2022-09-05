package com.atguigu.gmall.prouduct;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.ProuductMainApplication;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ProuductMainApplication.class)
public class ReadWriteSplitTest {

    @Resource
    BaseTrademarkMapper baseTrademarkMapper;

    @Test
    void testWrite(){
        BaseTrademark trademark = baseTrademarkMapper.selectById(4L);
        System.out.println("trademark = " + trademark);

        trademark.setTmName("小米-plus");
        baseTrademarkMapper.updateById(trademark);

        //改完后，再去查询，很可能查不到最新结果

        //让刚改完的下次查询强制走主库

        HintManager.getInstance().setWriteRouteOnly();//强制走主库
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);
        System.out.println("改完后查到的是 = " + baseTrademark);
    }

    @Test
    void testRead(){

        BaseTrademark baseTrademark1 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark1 = " + baseTrademark1);

        BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark2 = " + baseTrademark2);

        BaseTrademark baseTrademark3 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark3 = " + baseTrademark3);

        BaseTrademark baseTrademark4 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark4 = " + baseTrademark4);


    }
}
