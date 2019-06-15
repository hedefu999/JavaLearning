package com.dpchan.c06;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelBook implements IBook {
    private String name;
    private BigDecimal price;
    private String author;

    @Override
    public BigDecimal getCurrentPrice() {
        return price;
    }
}
