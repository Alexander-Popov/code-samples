package nstu.geo.core.application.command;

public interface CommandBus  {
    public void execute(Command command);
}
