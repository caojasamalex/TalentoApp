package com.djokic.companyservice.service;

import com.djokic.companyservice.client.CompanyRequestServiceClient;
import com.djokic.companyservice.client.UserServiceClient;
import com.djokic.companyservice.dto.CompanyDTO;
import com.djokic.companyservice.dto.EditCompanyDTO;
import com.djokic.companyservice.dto.companyrequestservicedto.CompanyRequestDTO;
import com.djokic.companyservice.dto.userservicedto.UserDTO;
import com.djokic.companyservice.enumeration.EmployeeRole;
import com.djokic.companyservice.enumeration.companyrequestserviceenumeration.CompanyRequestStatus;
import com.djokic.companyservice.enumeration.userserviceenumeration.PlatformRole;
import com.djokic.companyservice.mapper.CompanyMapper;
import com.djokic.companyservice.model.Company;
import com.djokic.companyservice.repository.CompanyRepository;
import com.djokic.companyservice.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserServiceClient userServiceClient;
    private final CompanyRequestServiceClient companyRequestServiceClient;
    private final EmployeeRepository employeeRepository;

    public List<CompanyDTO> findAllCompanies() {
        List<CompanyDTO> companies = companyRepository
                .findAll()
                .stream()
                .map(companyMapper::companyToCompanyDTO)
                .collect(Collectors.toList());

        return companies;
    }


    public CompanyDTO findCompanyById(Long companyId) {
        CompanyDTO companyDTO = companyRepository
                .findById(companyId)
                .map(companyMapper::companyToCompanyDTO)
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Company with CompanyID " + companyId + " not found!")
                );

        return companyDTO;
    }

    public List<CompanyDTO> findAllCompaniesByCompanyName(String companyName) {
        if(companyName == null || companyName.isBlank()) {
            return findAllCompanies();
        }

        String normalizedCompanyName = companyName.toLowerCase().trim();
        List<Company> companies = companyRepository.findCompaniesByCompanyNameContainsIgnoreCase(normalizedCompanyName);

        return companies
                .stream()
                .map(companyMapper::companyToCompanyDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyDTO createCompany(
            Long currentUserId,
            Long companyRequestId
    ) {
        UserDTO userDTO = fetchUserById(currentUserId);

        if(userDTO.getPlatformRole() != PlatformRole.PLATFORM_ADMIN){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Restricted!"
            );
        }

        CompanyRequestDTO companyRequestDTO = companyRequestServiceClient.findCompanyRequestById(currentUserId, companyRequestId);
        if(companyRequestDTO == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Company Request with ID " + companyRequestId + " not found!"
            );
        }

        if(companyRequestDTO.getStatus() != CompanyRequestStatus.APPROVED){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Company Request is not approved!"
            );
        }

        Company company = Company
                .builder()
                .companyName(companyRequestDTO.getCompanyName())
                .companyAddress(companyRequestDTO.getCompanyAddress())
                .companyCity(companyRequestDTO.getCompanyCity())
                .companyWebsite(companyRequestDTO.getCompanyWebsite())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return companyMapper.companyToCompanyDTO(companyRepository.save(company));
    }

    @Transactional
    public CompanyDTO editCompany(
            Long currentUserId,
            Long companyId,
            EditCompanyDTO editCompanyDTO
    ) {
        fetchUserById(currentUserId);

        Company company = companyRepository
                .findById(companyId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Company with ID " + companyId + " not found!"
                        )
                );

        if(!employeeRepository.existsByUserIdAndCompanyIdAndCompanyRole(currentUserId, companyId, EmployeeRole.COMPANY_MANAGER)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Restricted!"
            );
        }

        boolean hasChanged = false;

        String normalizedCompanyName = normalizeString(editCompanyDTO.getCompanyName());
        String normalizedCompanyAddress = normalizeString(editCompanyDTO.getCompanyAddress());
        String normalizedCompanyCity = normalizeString(editCompanyDTO.getCompanyCity());
        String normalizedCompanyWebsite = normalizeString(editCompanyDTO.getCompanyWebsite());
        String normalizedCompanyPhotoUrl = normalizeString(editCompanyDTO.getCompanyPhotoUrl());
        String normalizedCompanyDescription = normalizeString(editCompanyDTO.getCompanyDescription());

        if(stringHasChanged(normalizedCompanyName, company.getCompanyName())){
            hasChanged = true;
            company.setCompanyName(normalizedCompanyName);
        }
        if(stringHasChanged(normalizedCompanyAddress, company.getCompanyAddress())){
            hasChanged = true;
            company.setCompanyAddress(normalizedCompanyAddress);
        }
        if(stringHasChanged(normalizedCompanyCity, company.getCompanyCity())){
            hasChanged = true;
            company.setCompanyCity(normalizedCompanyCity);
        }
        if(stringHasChanged(normalizedCompanyWebsite, company.getCompanyWebsite())){
            hasChanged = true;
            company.setCompanyWebsite(normalizedCompanyWebsite);
        }
        if(stringHasChanged(normalizedCompanyPhotoUrl, company.getCompanyPhotoUrl())){
            hasChanged = true;
            company.setCompanyPhotoUrl(normalizedCompanyPhotoUrl);
        }
        if(stringHasChanged(normalizedCompanyDescription, company.getCompanyDescription())){
            hasChanged = true;
            company.setCompanyDescription(normalizedCompanyDescription);
        }

        if(hasChanged){
            company.setUpdatedAt(LocalDateTime.now());
            return companyMapper.companyToCompanyDTO(companyRepository.save(company));
        }

        return companyMapper.companyToCompanyDTO(company);
    }

    private UserDTO fetchUserById(Long userId) {
        UserDTO userDTO = userServiceClient.findUserById(userId);
        if(userDTO == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with ID " + userId + " not found!"
            );
        }

        return userDTO;
    }

    private String normalizeString(String stringToNormalize) {
        if(stringToNormalize == null){
            return null;
        }
        return stringToNormalize.trim();
    }

    private boolean stringHasChanged(String newString, String oldString) {
        if(newString == null){
            return false;
        }

        if(newString.isBlank()){
            return false;
        }

        if(oldString == null){
            return true;
        }

        return !newString.equalsIgnoreCase(oldString);
    }
}