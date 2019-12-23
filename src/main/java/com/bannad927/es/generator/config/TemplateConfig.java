package com.bannad927.es.generator.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TemplateConfig {

    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;

    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;

    private String service = ConstVal.TEMPLATE_SERVICE;

    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;

    private String mapper = ConstVal.TEMPLATE_MAPPER;

    private String xml = ConstVal.TEMPLATE_XML;

    private String controller = ConstVal.TEMPLATE_CONTROLLER;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }
}