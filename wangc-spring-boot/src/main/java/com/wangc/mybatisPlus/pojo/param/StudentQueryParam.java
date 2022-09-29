package com.wangc.mybatisPlus.pojo.param;

import lombok.*;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2022/9/28 16:13
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class StudentQueryParam {

    private Integer id;

    private String name;

    private Integer age;

    private List<Integer> ids;
}
