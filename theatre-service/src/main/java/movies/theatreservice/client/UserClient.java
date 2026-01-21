package movies.theatreservice.client;

import movies.theatreservice.dtos.CheckPermissionRequest;
import movies.theatreservice.utils.ApiResponse; // Make sure you have this class
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/api/v1/roles/check-permissions")
    ResponseEntity<ApiResponse<Boolean>> checkPermission(@RequestBody CheckPermissionRequest request);
}