package com.example.application.data;

import java.util.UUID;

public record Attachment(UUID attachmentId, String fileName, String mimeType, String description) {
    // The actual attachment payload is left out as it is not relevant for this demo
}
