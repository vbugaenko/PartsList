package ru.javarush.service;

import ru.javarush.db.entity.Part;

import java.util.List;

public interface PartsService
{
    List<Part> getAllParts();
}
