package com.incident.utils;

public interface ErrorCodes {
    String ERROR_MODULE_PREFIX = "ERR_IMS_";
    String INTERNAL_SERVER_ERROR = ERROR_MODULE_PREFIX + "500";
    String UN_AUTHORIZED = ERROR_MODULE_PREFIX + "401";
    String BAD_REQUEST = ERROR_MODULE_PREFIX + "400";
    String NOT_FOUND = ERROR_MODULE_PREFIX + "404";
}