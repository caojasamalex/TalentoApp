package com.djokic.companyrequestservice.service;

import com.djokic.companyrequestservice.client.CompanyServiceClient;
import com.djokic.companyrequestservice.client.UserServiceClient;
import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.dto.userservicedto.UserDTO;
import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import com.djokic.companyrequestservice.enumeration.userserviceenumeration.PlatformRole;
import com.djokic.companyrequestservice.mapper.CompanyRequestMapper;
import com.djokic.companyrequestservice.model.CompanyRequest;
import com.djokic.companyrequestservice.repository.CompanyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Company Request with ID %d not found", companyRequestId))));
    }

    @Transactional
    public CompanyRequestDTO createCompanyRequest(Long userId, CreateCompanyRequestDTO dto){

        // TODO: Sredi validaciju uslova

        if (userId == null || userId <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CurrentUserId");
        }

        Long requestedByUserId = dto.getRequestedByUserId();
        if (requestedByUserId == null || requestedByUserId <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested By UserId is required");
        }

        if (!userId.equals(requestedByUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current UserId and UserId from the request differ");
        }

        if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Name is required");
        }
        if (dto.getCompanyAddress() == null || dto.getCompanyAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Address is required");
        }
        if (dto.getCompanyCity() == null || dto.getCompanyCity().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company City is required");
        }

        CompanyRequest companyRequest = CompanyRequest.builder()
                .requestedByUserId(requestedByUserId)
                .companyName(dto.getCompanyName())
                .companyAddress(dto.getCompanyAddress())
                .companyCity(dto.getCompanyCity())
                .companyWebsite(dto.getCompanyWebsite())
                .status(CompanyRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return companyRequestMapper.companyRequestToCompanyRequestDTO(
                companyRequestRepository.save(companyRequest)
        );
    }

    @Transactional
    public CompanyRequestDTO editCompanyRequest(Long currentUserId, Long companyRequestId, CompanyRequestDTO companyRequestDTO){

        // TODO: Sredi validaciju uslova -> Moze bolje, ovo je samo da radi

        if(currentUserId == null || currentUserId <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CurrentUserId");
        }

        if(companyRequestId == null || companyRequestId <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CompanyRequestID is required");
        }

        if(companyRequestDTO == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CompanyRequestDTO is required");
        }

        if(!companyRequestId.equals(companyRequestDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CompanyRequestIDs differ");
        }

        CompanyRequest saved = companyRequestRepository.findById(companyRequestId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Request with ID " + companyRequestId + " not found")
        );

        if(!saved.getRequestedByUserId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current UserId and UserId from the request differ");
        }

        if(saved.getStatus() != CompanyRequestStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Request cannot be edited after being processed!");
        }

        boolean hasChanged = false;

        if(!Objects.equals(saved.getCompanyName(),companyRequestDTO.getCompanyName())) {
            saved.setCompanyName(companyRequestDTO.getCompanyName());
            hasChanged = true;
        }
        if(!Objects.equals(saved.getCompanyAddress(),companyRequestDTO.getCompanyAddress())) {
            saved.setCompanyAddress(companyRequestDTO.getCompanyAddress());
            hasChanged = true;
        }
        if(!Objects.equals(saved.getCompanyCity(),companyRequestDTO.getCompanyCity())) {
            saved.setCompanyCity(companyRequestDTO.getCompanyCity());
            hasChanged = true;
        }
        if (!Objects.equals(saved.getCompanyWebsite(), companyRequestDTO.getCompanyWebsite())) {
            saved.setCompanyWebsite(companyRequestDTO.getCompanyWebsite());
            hasChanged = true;
        }

        if(hasChanged) {
            saved.setUpdatedAt(LocalDateTime.now());
            return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(saved));
        } else {
            return companyRequestMapper.companyRequestToCompanyRequestDTO(saved);
        }
    }

    @Transactional
    public CompanyRequestDTO approveCompanyRequest(Long currentUserId, Long companyRequestId) {
        CompanyRequest companyRequest = validateAdminAndGetCompanyRequest(currentUserId, companyRequestId);

        companyRequest.setStatus(CompanyRequestStatus.APPROVED);
        companyRequest.setUpdatedAt(LocalDateTime.now());

        companyServiceClient.createCompany(companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequest));

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }

    @Transactional
    public CompanyRequestDTO rejectCompanyRequest(Long currentUserId, Long companyRequestId) {
        CompanyRequest companyRequest = validateAdminAndGetCompanyRequest(currentUserId, companyRequestId);

        companyRequest.setStatus(CompanyRequestStatus.REJECTED);
        companyRequest.setUpdatedAt(LocalDateTime.now());

        return companyRequestMapper.companyRequestToCompanyRequestDTO(companyRequestRepository.save(companyRequest));
    }

    private CompanyRequest validateAdminAndGetCompanyRequest(Long currentUserId, Long companyRequestId) {
        validateIfUserIsAdmin(currentUserId);

        CompanyRequest companyRequest = companyRequestRepository.findById(companyRequestId)
                .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Request Not Found")
                );

        if(companyRequest.getStatus() != CompanyRequestStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company Request Status has already been processed");
        }

        return companyRequest;
    }

    private void validateIfUserIsAdmin(Long userId) {
        if(userId == null || userId <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CurrentUserId");
        }

        UserDTO userDTO = userServiceClient.getUserById(userId);
        if(userDTO.getPlatformRole() != PlatformRole.PLATFORM_ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized!");
        }
    }
}