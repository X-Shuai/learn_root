package com.xs.spring.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.transform.sax.SAXResult;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @program: learn_root
 * @description: factory
 * @author: xs-shuai.com
 * @create: 2020-07-09 23:11
 **/
public class BeanFactory {


    Map<String,Object> map= new HashMap<String,Object>();
    public BeanFactory(String xml) {
        parseXml(xml);
        System.out.println(map);
    }

   public void parseXml(String xml)  {

       File file = new File(this.getClass().getResource("/").getPath()+"//"+xml);
       SAXReader reader = new SAXReader();

       try {

           boolean flag = false;
           Document document = reader.read(file);
           Element rootElement = document.getRootElement();
           List elementsChild = rootElement.elements();

           Attribute attribute = rootElement.attribute("default-autowire");
           if (attribute !=null){
               flag = true;
           }

           Iterator<Element> iterator = elementsChild.iterator();
           for (;iterator.hasNext();) {

               /**
                * 实例化
                */
               Element elementFirst = iterator.next();
               Attribute attributeId = elementFirst.attribute("id");
               String beanName = attributeId.getValue();

               Attribute attributeClass = elementFirst.attribute("class");
               String className = attributeClass.getValue();
               Class<?> clazz = Class.forName(className);

               /**
                * 维护依赖关系
                * 判断是否含有property  是否注入
                */

               Object object =null;

               Iterator<Element> iteratorSecond = elementFirst.elementIterator();
               for (;iteratorSecond.hasNext();) {

                   Element second = iteratorSecond.next();
                   if(second.getName().equals("property")){
                       //得到name
                       //直接new
                      object = clazz.newInstance();
                       Attribute ref = second.attribute("ref");
                       String refValue = ref.getValue();
                       Object injectObject = map.get(refValue);

                       Attribute name = second.attribute("name");
                       String nameValue = name.getValue();
                       Field field = clazz.getDeclaredField(nameValue);
                       field.setAccessible(true);
                       field.set(object,injectObject);
                   }else {
                       //特殊的构造方法
                       Attribute ref = second.attribute("ref");
                       String refValue = ref.getValue();
                       Object injectObject = map.get(refValue);

                       Class<?> injectObjectClass = injectObject.getClass();
                       //获得的是接口
                       Constructor<?> constructor = clazz.getConstructor(injectObjectClass.getInterfaces()[0]);
                       object = constructor.newInstance(injectObject);

                   }
               }
               //自动装配  如果property装配了,自动装配就不会装配了
               if (object == null) {
                   if (flag) {
                       if (attribute.getValue().equals("byType")) {
                           //判断是否有依赖
                           Field[] fields = clazz.getDeclaredFields();
                           for (Field field : fields) {
                               //属性的类型,  IndexDao
                               Class<?> injectClassType = field.getType();
                               //遍历map中的对象类型  判断类型相同,那么就注入进来
                               int count = 0;
                               Object injectionObject = null;
                               for (String s : map.keySet()) {
                                   Class<?> temp = map.get(s).getClass().getInterfaces()[0];
                                   if (temp.getName().equals(injectClassType.getName())) {
                                       injectionObject = map.get(s);
                                       count++;
                                   }

                               }
                               if (count > 1) {
                                   throw new RuntimeException("只需要一个对象,但是找到了两个");
                               } else {
                                   object = clazz.newInstance();
                                   field.setAccessible(true);
                                   field.set(object, injectionObject);
                               }
                           }
                       }
                   }
               }

               if (object == null){
                   object = clazz.newInstance();
               }
               map.put(beanName,object);

           }


       } catch (DocumentException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InstantiationException e) {
           e.printStackTrace();
       }
       catch (NoSuchFieldException e) {
           e.printStackTrace();
       } catch (NoSuchMethodException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }
   }

   public  Object getBean(String bean){
        return map.get(bean);
   }
}
