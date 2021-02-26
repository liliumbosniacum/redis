package com.lilium.redis.controller;

import com.lilium.redis.dto.PersonDTO;
import com.lilium.redis.dto.RangeDTO;
import com.lilium.redis.service.RedisListCache;
import com.lilium.redis.service.RedisValueCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final RedisValueCache valueCache;
    private final RedisListCache listCache;

    @Autowired
    public PersonController(final RedisValueCache valueCache, final RedisListCache listCache) {
        this.valueCache = valueCache;
        this.listCache = listCache;
    }

    @PostMapping
    public void cachePerson(@RequestBody final PersonDTO dto) {
        valueCache.cache(dto.getId(), dto);
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable final String id) {
        return (PersonDTO) valueCache.getCachedValue(id);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable final String id) {
        valueCache.deleteCachedValue(id);
    }

    @PostMapping("/list/{key}")
    public void cachePersons(@PathVariable final String key, @RequestBody final List<PersonDTO> persons) {
        listCache.cachePersons(key, persons);
    }

    @GetMapping("/list/{key}")
    public List<PersonDTO> getPersonsInRange(@PathVariable final String key, @RequestBody final RangeDTO range) {
        return listCache.getPersonsInRange(key, range);
    }

    @GetMapping("/list/last/{key}")
    public PersonDTO getLastElement(@PathVariable final String key) {
        return listCache.getLastElement(key);
    }

    @DeleteMapping("/list/{key}")
    public void trim(@PathVariable final String key, @RequestBody final RangeDTO range) {
        listCache.trim(key, range);
    }
}
