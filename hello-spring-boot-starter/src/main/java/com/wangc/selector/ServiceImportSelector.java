package com.wangc.selector;

import com.wangc.service.AService;
import com.wangc.service.BService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author
 * @Description: 自定义ImportSelector，指定引入类，结合@Import使用来实现自动装载
 * 实现selectImports方法，这个方法返回的类名会被自动装配到IOC中
 * @date 2022/10/29 10:37
 */
public class ServiceImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{AService.class.getName(), BService.class.getName()};
    }
}
