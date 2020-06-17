package com.xs.vertx;

import io.vertx.core.Vertx;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-06-18 00:50
 **/
public class VertMain {
    public static void main(String[] args){
      Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(MainVerticle.class.getName());
  }
}
