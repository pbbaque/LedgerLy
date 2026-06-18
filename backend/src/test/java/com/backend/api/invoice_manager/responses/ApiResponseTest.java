package com.backend.api.invoice_manager.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void storesResponseMetadataAndPayload() {
        ApiResponse<String> response = new ApiResponse<>("Created", "payload", 201, true);

        assertEquals("Created", response.getMessage());
        assertEquals("payload", response.getData());
        assertEquals(201, response.getStatusCode());
        assertTrue(response.getSuccess());
    }

    @Test
    void allowsUpdatingResponseFields() {
        ApiResponse<String> response = new ApiResponse<>("Initial", null, 400, false);

        response.setMessage("OK");
        response.setData("updated");
        response.setStatusCode(200);
        response.setSuccess(true);

        assertEquals("OK", response.getMessage());
        assertEquals("updated", response.getData());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getSuccess());
    }
}
