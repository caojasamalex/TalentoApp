package com.djokic.companyrequestservice.service;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import com.djokic.companyrequestservice.mapper.CompanyRequestMapper;
import com.djokic.companyrequestservice.model.CompanyRequest;
import com.djokic.companyrequestservice.repository.CompanyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyRequestService {
    private final CompanyRequestRepository companyRequestRepository;
    private final CompanyRequestMapper companyRequestMapper;

    public List<CompanyRequestDTO> findAllCompanyRequests() {
        return companyRequestRepository.findAll()
                .stream()
                .map(companyRequestMapper::companyRequestToCompanyRequestDTO)
                .collect(Collectors.toList());
    }

    public CompanyRequestDTO findCompanyRequestById(Long id) {
        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public CompanyRequestDTO createCompanyRequest(CreateCompanyRequestDTO createCompanyRequestDTO){
        if(createCompanyRequestDTO.getRequestedByUserId() == null || createCompanyRequestDTO.getRequestedByUserId() <= 0L){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested By UserId is required");
        }

        if(createCompanyRequestDTO.getCompanyName() == null || createCompanyRequestDTO.getCompanyName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Name is required");
        }

        if(createCompanyRequestDTO.getCompanyAddress() == null || createCompanyRequestDTO.getCompanyAddress().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Address is required");
        }

        if(createCompanyRequestDTO.getCompanyCity() == null || createCompanyRequestDTO.getCompanyCity().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company City is required");
        }

        CompanyRequest companyRequest = CompanyRequest
                .builder()
                .requestedByUserId(createCompanyRequestDTO.getRequestedByUserId())
                .companyName(createCompanyRequestDTO.getCompanyName())
                .companyAddress(createCompanyRequestDTO.getCompanyAddress())
                .companyCity(createCompanyRequestDTO.getCompanyCity())
                .companyWebsite(createCompanyRequestDTO.getCompanyWebsite())
                .status(CompanyRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }

    public CompanyRequestDTO approveCompanyRequest(Long companyRequestId) {
        CompanyRequest companyRequest = companyRequestRepository.findById(companyRequestId)
                .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Request Not Found")
                );

        companyRequest.setStatus(CompanyRequestStatus.APPROVED);
        companyRequest.setUpdatedAt(LocalDateTime.now());
        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }
}