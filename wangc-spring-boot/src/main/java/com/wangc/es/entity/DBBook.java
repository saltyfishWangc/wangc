package com.wangc.es.entity;

import lombok.Data;
//import org.springframework.data.annotation.Id; 对于@Id，不能引入这个包，会报org.hibernate.AnnotationException: No identifier specified for entity: com.wangc.es.entity.DBBook

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 * @Description: 定义一个索引(类似数据库)
 *  @Document : 用在类上，定义在Elasticsearch中索引信息
 *  @Id : 用在字段上，定义了Elasticsearch的_id
 *  @Field : 用在字段上，定义Elasticsearch的字段
 *      analyzer : 表示要对该字段进行分词，且指定分词器
 *      type : 定义字段类型
 *      format : 定义格式
 * @date 2022/8/31 19:24
 */
@Data
@Entity
@Table(name = "db_book")
public class DBBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String author;

    private Double price;

    private Date createTime;

    private Date updateTime;
}
