package com.wangc.log.annotation;

import com.wangc.selector.ServiceImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义注解用于自动装配的开关，引入ServiceImportSelector
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(ServiceImportSelector.class)
public @interface EnableServiceAutoImport {
}
