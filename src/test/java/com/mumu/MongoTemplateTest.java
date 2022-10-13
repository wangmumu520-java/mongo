package com.mumu;

import com.mongodb.client.result.UpdateResult;
import com.mumu.com.mumu.entity.Product;
import com.sun.javafx.logging.PulseLogger;
import javafx.beans.binding.When;
import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;
import java.util.List;

@Slf4j
@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringRunner.class)
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    //文档添加
    @Test
    public void testSave(){
        Product product = new Product();
        product.setId("1005");
        product.setProductName("元气森林");
        product.setPrice(6.00);
        product.setProductNum(36);
        mongoTemplate.save(product);
        // mongoTemplate.findAll(Product.class).forEach(product1 -> {
        //     System.out.println(product1);
        // });
        mongoTemplate.findAll(Product.class).forEach(System.out::println);
    }

    //文档查询
    @Test
    public void testQuery(){
        //查询所有
        // mongoTemplate.findAll(Product.class).forEach(product ->
        // {
        //     System.out.println("所有商品信息："+product);
        // });
        // System.out.println("------------------");
        // mongoTemplate.findAll(Product.class,"products").forEach(product -> {
        //     System.out.println("商品信息1："+product);
        // });

        //基于id查询
        // Product byId = mongoTemplate.findById("1001", Product.class);
        // System.out.println("基于id查询："+byId);

        //添加查询 参数 1: 查询条件   参数 2: 返回类型
        // for (Product product : mongoTemplate.find(new Query(), Product.class)) {
        //     System.out.println(product);
        // }

        //等值查询
        // mongoTemplate.find(Query.query(Criteria.where("productName").is("芬达")),Product.class).forEach(product -> {
        //     System.out.println(product);
        // });

        //> < >= <= 查询
        // for (Product price : mongoTemplate.find(Query.query(Criteria.where("price").lt(3.00)), Product.class)) {
        //     System.out.println(price);
        // }

        //and 查询
        // mongoTemplate.find(Query.query(Criteria.where("productName").is("哇哈哈").and("price").is(2.50)),Product.class).forEach(product -> {
        //     System.out.println(product);
        // });

        //or 查询
        // Criteria criteria = new Criteria();
        // criteria.orOperator(
        //         Criteria.where("productName").is("哇哈哈"),
        //         Criteria.where("price").is(3.00)
        // );
        // mongoTemplate.find(Query.query(criteria),Product.class).forEach(product ->
        // {
        //     System.out.println(product);
        // });

        //and  or 查询
        // Criteria criteria = new Criteria();
        // criteria.and("price").is(3.50).orOperator(
        //         Criteria.where("productName").is("哇哈哈"),
        //         Criteria.where("productName").is("芬达")
        // );
        // mongoTemplate.find(Query.query(criteria),Product.class).forEach(product -> {
        //     System.out.println(product);
        // });

        //排序
        // Query query = new Query();
        // query.with(Sort.by(Sort.Order.asc("price")));
        // mongoTemplate.find(query,Product.class).forEach(product -> {
        //     System.out.println(product);
        // });

        //分页排序查询 skip limit 分页
        // Query querySortPage = new Query();
        // querySortPage.with(Sort.by(Sort.Order.asc("num")))
        //         .skip(0)
        //         .limit(2);
        // mongoTemplate.find(querySortPage,Product.class).forEach(product -> {
        //     System.out.println(product);
        // });

        //总条数
        // long count1 = mongoTemplate.count(new Query(), Product.class);
        // System.out.println("总记录数"+count1);
        // long count = mongoTemplate.count(Query.query(Criteria.where("price").gte(3.00)), Product.class);
        // System.out.println("大于3元商品记录数"+count);

        //去重 distinct
        //参数 1:查询条件 参数 2: 去重字段  参数 3: 操作集合  参数 4: 返回类型
        // List<String> lists = mongoTemplate.findDistinct(new Query(), "productName", Product.class, String.class);
        // lists.forEach(list -> System.out.println(list));

        //使用 json 字符串方式查询
        Query query = new BasicQuery(
               "{$or:[{productName:'哇哈哈'},{productName:'芬达'}]}"
        );
        List<Product> products = mongoTemplate.find(query, Product.class);
        products.forEach(product -> {
            System.out.println(product);
        });

    }

    //文档删除
    @Test
    public void testDrop(){
        //条件删除
        mongoTemplate.remove(Query.query(Criteria.where("productNum").is(36)),Product.class);

        //删除所有
        // mongoTemplate.remove(new Query(),Product.class);
    }

    //文档更新
    @Test
    public void testUpdate(){
        //更新条件
        Query query = new Query(Criteria.where("productNum").is("66"));
        //更新内容
        Update update = new Update();
        update.set("productNum",99);

        //更新符合条件第一条数据
        // mongoTemplate.updateFirst(query,update,Product.class);

        //更新符合d多条
        mongoTemplate.updateMulti(query,update,Product.class);
        
        //没有符合条件数据插入数据
        //返回值均为 updateResult
        // UpdateResult updateResult = mongoTemplate.upsert(query, update, Product.class);
        //System.out.println("匹配条数:" + updateResult.getMatchedCount());
        //System.out.println("修改条数:" + updateResult.getModifiedCount());
        //System.out.println("插入id_:" + updateResult.getUpsertedId());
    }

    //创建集合
    @Test
    public void testCreateCollection(){
        boolean products = mongoTemplate.collectionExists("products");
        if(products) throw new RuntimeException("集合已存在");
        mongoTemplate.createCollection("product");

    }
    //删除集合
    @Test
    public void testDeleteCollection(){
        mongoTemplate.dropCollection("product");
    }

}
