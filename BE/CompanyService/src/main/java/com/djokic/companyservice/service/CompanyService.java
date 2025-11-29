package com.djokic.companyservice.service;

import com.djokic.companyservice.mapper.CompanyMapper;
import com.djokic.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
}
