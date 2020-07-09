package com.xs.spring.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.transform.sax.SAXResult;
import java.io.File;
import java.lang.reflect.Field;
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

   public void parseXml(String xml){

       File file = new File(this.getClass().getResource("/").getPath()+"//"+xml);
       SAXReader reader = new SAXReader();
       try {
           Document document = reader.read(file);
           Element rootElement = document.getRootElement();
           List elementsChild = rootElement.elements();

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


               Object object= clazz.newInstance();
               map.put(beanName,object);
               Iterator<Element> iteratorSecond = elementFirst.elementIterator();
               for (;iteratorSecond.hasNext();) {

                   Element second = iteratorSecond.next();
                   if(second.getName().equals("property")){
                       //得到name
                       Attribute ref = second.attribute("ref");
                       String refValue = ref.getValue();
                       Object injectObject = map.get(refValue);

                       Attribute name = second.attribute("name");
                       String nameValue = name.getValue();
                       Field field = clazz.getField(nameValue);
                       field.setAccessible(true);
                       field.set(object,injectObject);
                   }

               }






           };


       } catch (DocumentException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (NoSuchFieldException e) {
           e.printStackTrace();
       }
   }

   public  Object getBean(String bean){
        return map.get(bean);
   }
}
