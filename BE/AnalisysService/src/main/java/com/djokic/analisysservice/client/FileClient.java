package com.djokic.analisysservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "file-client", url = "http://localhost:8010")
public interface FileClient {

    @GetMapping("/files/{filename}")
    String fetchFile(@PathVariable("filename") String filename);
}
