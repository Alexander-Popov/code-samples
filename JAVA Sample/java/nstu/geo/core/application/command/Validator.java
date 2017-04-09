package nstu.geo.core.application.command;


import nstu.geo.core.infrastructure.exeption.ValidationException;

public interface Validator<T> {
    public void validate(T command) throws ValidationException;
}
