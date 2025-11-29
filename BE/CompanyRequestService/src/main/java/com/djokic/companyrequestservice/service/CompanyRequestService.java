package com.djokic.companyrequestservice.service;

import com.djokic.companyrequestservice.client.CompanyServiceClient;
import com.djokic.companyrequestservice.client.UserServiceClient;
import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.dto.EditCompanyRequestDTO;
import com.djokic.companyrequestservice.dto.userservicedto.UserDTO;
import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import com.djokic.companyrequestservice.enumeration.userserviceenumeration.PlatformRole;
import com.djokic.companyrequestservice.mapper.CompanyRequestMapper;
import com.djokic.companyrequestservice.model.CompanyRequest;
import com.djokic.companyrequestservice.repository.CompanyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyRequestService {
    private final CompanyRequestRepository companyRequestRepository;
    private final CompanyRequestMapper companyRequestMapper;
    private final UserServiceClient userServiceClient;
    private final CompanyServiceClient companyServiceClient;

    public List<CompanyRequestDTO> findAllCompanyRequests(Long currentUserId) {
        validateIfUserIsAdmin(currentUserId);

        return companyRequestRepository.findAll()
                .stream()
                .map(companyRequestMapper::companyRequestToCompanyRequestDTO)
                .collect(Collectors.toList());
    }

    public CompanyRequestDTO findCompanyRequestById(Long currentUserId, Long companyRequestId) {
        validateIfUserIsAdmin(currentUserId);

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.findById(companyRequestId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format("Company Request with ID %d not found!", companyRequestId)
                        )
                )
        );
    }

    @Transactional
    public CompanyRequestDTO createCompanyRequest(
            Long currentUserId,
            CreateCompanyRequestDTO createCompanyRequestDTO
    ){
        validateUser(currentUserId); // Checks if the currentUserId is valid and if user exists

        CompanyRequest companyRequest = CompanyRequest.builder()
                .requestedByUserId(currentUserId)
                .companyName(normalizeCompanyName(createCompanyRequestDTO.getCompanyName()))
                .companyAddress(normalizeCompanyAddress(createCompanyRequestDTO.getCompanyAddress()))
                .companyCity(normalizeCompanyCity(createCompanyRequestDTO.getCompanyCity()))
                .status(CompanyRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if(createCompanyRequestDTO.getCompanyWebsite() != null) {
            companyRequest.setCompanyWebsite(createCompanyRequestDTO.getCompanyWebsite());
        }

        return companyRequestMapper.companyRequestToCompanyRequestDTO(
                companyRequestRepository.save(companyRequest)
        );
    }

    @Transactional
    public CompanyRequestDTO editCompanyRequest(
            Long currentUserId,
            Long companyRequestId,
            EditCompanyRequestDTO editCompanyRequestDTO
    ){
        validateUser(currentUserId);
        CompanyRequest companyRequest = fetchCompanyRequest(currentUserId, companyRequestId);

        boolean hasChanged = false;

        if(editCompanyRequestDTO.getCompanyName() != null && !editCompanyRequestDTO.getCompanyName().isBlank()) {
            String normalizedCompanyName = normalizeCompanyName(editCompanyRequestDTO.getCompanyName());
            if(!normalizedCompanyName.equals(companyRequest.getCompanyName())) {
                companyRequest.setCompanyName(normalizedCompanyName);
                hasChanged = true;
            }
        }

        if(editCompanyRequestDTO.getCompanyAddress() != null && !editCompanyRequestDTO.getCompanyAddress().isBlank()) {
            String normalizedCompanyAddress = normalizeCompanyAddress(editCompanyRequestDTO.getCompanyAddress());
            if(!normalizedCompanyAddress.equals(companyRequest.getCompanyAddress())) {
                companyRequest.setCompanyAddress(normalizedCompanyAddress);
                hasChanged = true;
            }
        }

        if(editCompanyRequestDTO.getCompanyCity() != null && !editCompanyRequestDTO.getCompanyCity().isBlank()) {
            String normalizedCompanyCity = normalizeCompanyCity(editCompanyRequestDTO.getCompanyCity());
            if(!normalizedCompanyCity.equals(companyRequest.getCompanyCity())) {
                companyRequest.setCompanyCity(normalizedCompanyCity);
                hasChanged = true;
            }
        }

        if(editCompanyRequestDTO.getCompanyWebsite() != null && !editCompanyRequestDTO.getCompanyWebsite().isBlank()) {
            String normalizedCompanyWebsite = normalizeCompanyWebsite(editCompanyRequestDTO.getCompanyWebsite());
            if(!normalizedCompanyWebsite.equals(companyRequest.getCompanyWebsite())) {
                companyRequest.setCompanyWebsite(normalizedCompanyWebsite);
                hasChanged = true;
            }
        }

        if(hasChanged) {
            companyRequest.setUpdatedAt(LocalDateTime.now());
            return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
        }

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequest);
    }

    @Transactional
    public CompanyRequestDTO approveCompanyRequest(Long currentUserId, Long companyRequestId) {
        validateIfUserIsAdmin(currentUserId);

        CompanyRequest companyRequest = companyRequestRepository
                .findById(companyRequestId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Company Request with ID %d not found!", companyRequestId)
                        )
                );

        if(companyRequest.getStatus() != CompanyRequestStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Company Request with ID " + companyRequestId + " has already been processed!"
            );
        }

        companyRequest.setStatus(CompanyRequestStatus.APPROVED);
        companyRequest.setUpdatedAt(LocalDateTime.now());

        // TODO: CALL COMPANY SERVICE TO CREATE A NEW COMPANY
        // companyServiceClient.createCompany(companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequest));
        //

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }

    @Transactional
    public CompanyRequestDTO rejectCompanyRequest(Long currentUserId, Long companyRequestId) {
        validateIfUserIsAdmin(currentUserId);

        CompanyRequest companyRequest = companyRequestRepository
                .findById(companyRequestId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Company Request with ID %d not found!", companyRequestId)
                        )
                );

        if(companyRequest.getStatus() != CompanyRequestStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Company Request with ID " + companyRequestId + " has already been processed!"
            );
        }

        companyRequest.setStatus(CompanyRequestStatus.REJECTED);
        companyRequest.setUpdatedAt(LocalDateTime.now());

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }

    private void validateUser(Long currentUserId) {
        UserDTO userDTO = userServiceClient.findUserById(currentUserId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found!");
        }
    }

    private CompanyRequest fetchCompanyRequest(Long currentUserId, Long companyRequestId) {
        CompanyRequest companyRequest = companyRequestRepository
                .findById(companyRequestId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Company Request with ID " + companyRequestId + " not found!"
                        )
                );

        UserDTO userDTO = userServiceClient.findUserById(currentUserId);
        if(userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found!");
        }

        if(currentUserId.equals(companyRequest.getRequestedByUserId()) || userDTO.getPlatformRole() == PlatformRole.PLATFORM_ADMIN){
            return companyRequest;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Restricted!");
        }
    }

    private void validateIfUserIsAdmin(Long userId) {
        UserDTO userDTO = userServiceClient.findUserById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found!");
        }

        if(userDTO.getPlatformRole() != PlatformRole.PLATFORM_ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Restricted!");
        }
    }

    private String normalizeCompanyName(String companyName) {
        return companyName.trim();
    }

    private String normalizeCompanyAddress(String companyAddress) {
        return companyAddress.trim();
    }

    private String normalizeCompanyCity(String companyCity) {
        return companyCity.trim();
    }

    private String normalizeCompanyWebsite(String companyWebsite) {
        return companyWebsite.trim();
    }

    public void deleteCompanyRequest(Long userId, Long companyRequestId) {
        validateUser(userId);

        CompanyRequest companyRequest = companyRequestRepository.findById(companyRequestId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company Request with ID " + companyRequestId + " not found!"
                )
        );

        if(!userId.equals(companyRequest.getRequestedByUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Restricted!"
            );
        }

        if (companyRequest.getStatus() != CompanyRequestStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only PENDING company requests can be deleted!"
            );
        }

        companyRequestRepository.delete(companyRequest);
    }

    public List<CompanyRequestDTO> findPendingCompanyRequests(Long currentUserId) {
        validateIfUserIsAdmin(currentUserId);

        return companyRequestRepository
                .findCompanyRequestsByStatus(CompanyRequestStatus.PENDING)
                .stream()
                .map(companyRequestMapper::companyRequestToCompanyRequestDTO)
                .collect(Collectors.toList());
    }
}