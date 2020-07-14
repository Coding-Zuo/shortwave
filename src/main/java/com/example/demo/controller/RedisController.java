package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 
 * @ClassName RedisController
 * @Author 鲸落
 * @date 2020.07.13 18:08
 */
@RestController
@RequestMapping("testRedis")
public class RedisController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //Set集合数据操作

    @GetMapping("addDataOfSet")
    public void DataOfSet() {
        Set<String> set1=new HashSet<String>();
        set1.add("TestData1");
        set1.add("TestData2");
        set1.add("TestData3");

        //存入Set集合数据

        redisTemplate.opsForSet().add("setData",set1);

        //获取Set集合数据
        Set<Object> resultSet =redisTemplate.opsForSet().members("setData");

        System.out.println("resultSet:"+resultSet);

    }




    //Map集合数据操作

    @GetMapping("addDataOfMap")
    public void DataOfMap() {

        Map<String,String> map=new HashMap<String,String>();
        map.put("key1","测试值1");
        map.put("key2","测试值2");
        map.put("key3","测试值3");
        map.put("key4","测试值4");
        map.put("key5","测试值5");

        //TODO 存储map数据到redis里面的方法一，以字符串格式存进去，顺便设置了过期时间
        redisTemplate.opsForValue().set("SessionMap", map, 60, TimeUnit.SECONDS);
        //取值method1,对应方法一，把map数据（STRING）格式取出来，赋予给OBJECT，可以转换json对象
        Object StrMapObj=redisTemplate.boundValueOps("SessionMap").get();
        String json= JSON.toJSONString(StrMapObj);
        System.out.println(json);
        //JSONObject jsonObject = JSONObject.parseObject(json);


        // 取值method2，取出来直接赋予给MAP
        Map<String, String>  strMap= (Map<String, String>) redisTemplate.boundValueOps("SessionMap").get();
        System.out.println(strMap.toString());

        //TODO 存入map数据到redis里面的方法二，以map形式存进去，这种情况过期时间默认调的是配置文件里面的

        // redisTemplate.opsForHash().putAll("mapData",map);
        //以下为方法二的取值方式，其实就是操作map
        // Map<Object, Object> resultMap= redisTemplate.opsForHash().entries("mapData");
        // List<Object> resultMapList=redisTemplate.opsForHash().values("mapData");
        // Set<Object> resultMapSet=redisTemplate.opsForHash().keys("mapData");
        //String value=(String)redisTemplate.opsForHash().get("mapData","key1");
        // System.out.println("通过单一的key去取值value:"+value);
        // System.out.println("通过Set集合方式取值resultMapSet:"+resultMapSet);
        // System.out.println("通过Map方式取值resultMap:"+resultMap);
        // System.out.println("通过List方式取值resulreslutMapListtMap:"+resultMapList);

    }

    //List集合数据操作

    @GetMapping("addDataOfList")
    public void DataOfList() {

        List<String> list1=new ArrayList<>();
        list1.add("listA的测试值a1");
        list1.add("listA的测试值a2");
        list1.add("listA的测试值a3");

        List<String> list2=new ArrayList<>();
        list2.add("listB的测试值b1");
        list2.add("listB的测试值b2");
        list2.add("listB的测试值b3");
        redisTemplate.opsForList().leftPush("listDataA",list1);
        redisTemplate.opsForList().rightPush("listDataB",list2);
        //存完后可以看redis数据库，数据已写入
        //以下的取list值方式是通过 直接将列表的元素从左或右取出， 一旦取完，redis数据库对应的键值也会没了，因为用的是leftPop方法，实际上List这个集合redisTemplate提供了非常多的方法

//leftPush(K key, V value)
//index(K key, long index)
//range(K key, long start, long end)
//leftPush(K key, V pivot, V value)
//size
//非常多，有兴趣可以去了解对redis的list数据结构操作方法
        List<String> resultList1= (List<String>) redisTemplate.opsForList().leftPop("listDataA");
        System.out.println(resultList1);
        List<String> resultList2= (List<String>) redisTemplate.opsForList().rightPop("listDataB");
        System.out.println(resultList2);


    }

    //普通key-value值操作

    @GetMapping("addDataOfKeyValue")
    public void DataOfKeyValue() {

        redisTemplate.opsForValue().set("测试key1","测试value1");
        redisTemplate.opsForValue().set("测试key2","测试value2");
        redisTemplate.opsForValue().set("测试key3","测试value3");
        redisTemplate.opsForValue().set("测试key4","测试value4");
        String result1=redisTemplate.opsForValue().get("测试key1").toString();
        String result2=redisTemplate.opsForValue().get("测试key2").toString();
        String result3=redisTemplate.opsForValue().get("测试key3").toString();
        System.out.println("缓存结果为：result："+result1+"  "+result2+"   "+result3);

    }

    //stringRedisTemplate对字符串值得存与取
    @GetMapping("stringRedisTemplate")
    public void stringRedisTemplate() {
        stringRedisTemplate.opsForValue().set("TEST_STRING","stringRedisTemplate");
        String result= stringRedisTemplate.opsForValue().get("TEST_STRING");
        System.out.println("缓存结果为：result："+result);

    }
}
