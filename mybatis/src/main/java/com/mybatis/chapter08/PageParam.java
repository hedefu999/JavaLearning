package com.mybatis.chapter08;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    private Integer pageIndex;
    private Integer pageSize;
    //是否启用插件
    private Boolean useFlag;
    //是否检测页码有效性，如果为true，而页码大于最大页数，则抛出异常
    private Boolean checkFlag;
    //是否清除最后的order by
    private Boolean cleanOrderBy;
    private Integer total;
    private Integer totalPage;

}
