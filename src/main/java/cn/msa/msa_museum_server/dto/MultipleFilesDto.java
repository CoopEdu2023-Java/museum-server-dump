package cn.msa.msa_museum_server.dto;

import lombok.Data;

import java.util.List;

@Data
public class MultipleFilesDto {
    private List<String> uploadedFiles;
    private List<String> failedFiles;
}
