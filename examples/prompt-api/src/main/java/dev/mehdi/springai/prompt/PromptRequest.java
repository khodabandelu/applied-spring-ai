package dev.mehdi.springai.prompt;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(@NotBlank String prompt) {}
