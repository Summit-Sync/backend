package com.summitsync.api.status;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.status.dto.StatusPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatusService {
    private final StatusRepository statusRepository;

    public Status saveStatus(Status status) {
        return this.statusRepository.save(status);
    }

    public List<Status> findAll() {
        return this.statusRepository.findAll();
    }

    public Status findById(long id) {
        var status = this.statusRepository.findById(id);

        if (status.isEmpty()) {
            throw new ResourceNotFoundException("status on id " + id + " not found");
        }

        return status.get();
    }

    public void deleteById(long id) {
        this.statusRepository.deleteById(id);
    }

    public Status updateStatus(long id, StatusPostDto statusPostDto) {
        var status = this.findById(id);

        status.setText(statusPostDto.getText());

        return this.statusRepository.save(status);
    }
}
