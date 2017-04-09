package nstu.geo.core.application.command;

public final class SimpleCommandBus implements CommandBus {

    private Handler handler;

    public void execute(final Command command)
    {
            this.resolveHandler(command);
            this.handler.handle(command);
    }

    private void resolveHandler(final Command command)
    {
        String commandClassName = command.getClass().getName();
        String handlerClassName = commandClassName.substring(0, commandClassName.length() - 7) +  "Handler";
        try {
            this.handler = (Handler) Class.forName(handlerClassName).newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
