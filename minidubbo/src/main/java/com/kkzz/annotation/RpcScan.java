package com.kkzz.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//// TODO: 2022/4/26 0026 导入对应的类,进行扫描
@Documented
public @interface RpcScan {
    String[] basePackage();
}
