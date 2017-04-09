package nstu.geo.core.infrastructure.repository;


import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.repository.segy.SegyFileInMemoryRepository;

import java.util.HashMap;
import java.util.Map;

public final class SegyFileInRamRepository implements SegyFileInMemoryRepository {
    private Map<String, SegyFile> storage = new HashMap<String, SegyFile>();
    static SegyFileInRamRepository instance = null;

    public static SegyFileInRamRepository getInstance(){
        if( instance == null )
            instance = new SegyFileInRamRepository();
        return instance;
    }

    public void add(final String key, final SegyFile segyFile) {
        this.storage.put(key, segyFile);
    }

    public SegyFile find(final String key) {
        return storage.get(key);
    }

    public void delete(final String key) {
        this.storage.remove(key);
    }

    public boolean exists(final String key) {
        return storage.containsKey(key);
    }

}
