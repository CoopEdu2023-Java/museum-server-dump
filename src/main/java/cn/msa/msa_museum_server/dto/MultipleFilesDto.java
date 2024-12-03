package cn.msa.msa_museum_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleFilesDto {
    private List<String> uploadedFiles;
    private List<String> failedFiles;
}
