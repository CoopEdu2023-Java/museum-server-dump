package cn.msa.msa_museum_server.dto;

public enum FileContentRequestTypeDto {
    /**
     * Request with the Range header.
     */
    RANGE,
    /**
     * Request without the Range header.
     */
    FULL
}