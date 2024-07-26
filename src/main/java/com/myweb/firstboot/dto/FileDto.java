package com.myweb.firstboot.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDto {
    private int id;
    private String file_name;
    private String file_path;
    private String org_file_name;
    private String userid;
    private int post_no;
}
