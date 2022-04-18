package com.kkzz.to;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
