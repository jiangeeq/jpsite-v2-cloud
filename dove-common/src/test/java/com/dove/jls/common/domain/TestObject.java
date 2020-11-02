package com.dove.jls.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestObject {
    private String name;
    private String desc;
    private Integer id;
}
