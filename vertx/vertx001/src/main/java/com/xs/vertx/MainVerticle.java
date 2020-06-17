package com.xs.vertx;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
              .putHeader("content-type", "text/plain")
              .end("Hello World!");
    }).listen(8888);
  }
}
