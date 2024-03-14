package com.summitsync.api.status;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatusService {
    private StatusRepository statusRepository;
    public Status findById(long id) {
       var status = this.statusRepository.findById(id);

       if (status.isEmpty()) {
           throw new ResourceNotFoundException("status on id " + id + " not found");
       }

       return status.get();
    }
}
