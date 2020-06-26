package com.ashikhman.ssl.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Example topic item DTO.
 */
@Data
@Accessors(chain = true)
public class TopicItem {

    /**
     * Some name.
     */
    private String name;
}
