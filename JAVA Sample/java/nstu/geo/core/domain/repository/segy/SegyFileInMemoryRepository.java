package nstu.geo.core.domain.repository.segy;


import nstu.geo.core.domain.model.segy.SegyFile;

public interface SegyFileInMemoryRepository {
    public void add(final String key, final SegyFile segyFile);
    public SegyFile find(final String key);
    public void delete(final String key);
    public boolean exists(final String key);
}
