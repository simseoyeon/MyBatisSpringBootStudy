package com.myweb.firstboot.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GalaryDto {
    private int id;
    private String title;
    private String userid;
    private int hit_cnt;
    private int img_id;
}
