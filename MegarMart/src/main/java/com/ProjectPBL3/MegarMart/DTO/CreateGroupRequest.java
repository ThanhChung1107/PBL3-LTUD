package com.ProjectPBL3.MegarMart.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateGroupRequest {
    private String groupname;
    private MultipartFile groupavatar;
}