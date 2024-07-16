package com.alura.literalura.service;

public interface IConvertData {
    <T> T gettingData(String json, Class<T> clase);
}
