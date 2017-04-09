package nstu.geo.core.application.command;


public interface Handler<T> {
    public void handle(T command);
}
