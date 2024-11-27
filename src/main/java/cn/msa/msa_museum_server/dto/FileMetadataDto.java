package cn.msa.msa_museum_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadataDto {
    private String id;
    private String name;
    private String type;
    private String size;
    private String url;
}