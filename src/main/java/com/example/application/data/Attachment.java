package com.example.application.data;

import java.util.UUID;

public record Attachment(UUID attachmentId, String fileName, String mimeType, String description) {
}
