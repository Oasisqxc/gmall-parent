package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EsTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void test1(){
        Person person = new Person();
        person.setId(2L);
        person.setFirstName("牛逼");
        person.setLastName("大洋");
        person.setAge(20);
        person.setAddress("西安市唐岩村");

        Person person1 = new Person();
        person1.setId(3L);
        person1.setFirstName("水獭");
        person1.setLastName("小杨");
        person1.setAge(29);
        person1.setAddress("西安市莲湖区");

        Person person2 = new Person();
        person2.setId(3L);
        person2.setFirstName("检索万和无赖");
        person2.setLastName("小杨");
        person2.setAge(19);
        person2.setAddress("西安市小年轻");

        Person person3 = new Person();
        person3.setId(4L);
        person3.setFirstName("检索耍赖");
        person3.setLastName("小杨呵呵");
        person3.setAge(29);
        person3.setAddress("西安市3祥村");
         personRepository.save(person);
         personRepository.save(person1);
         personRepository.save(person2);
         personRepository.save(person3);

        System.out.println("完成........");
    }

    @Test
    void testQuery(){
        List<Person> 西安市 = personRepository.findAllByAddress("西安市");
        for (Person person : 西安市) {
            System.out.println("person = " + person);
        }
    }
}
