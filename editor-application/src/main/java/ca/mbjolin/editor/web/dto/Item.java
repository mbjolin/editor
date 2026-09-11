package ca.mbjolin.editor.web.dto;

import java.util.Map;

public record Item (String id, Map<String, Object> content, String type) {}
